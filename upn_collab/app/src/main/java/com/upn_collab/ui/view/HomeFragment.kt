package com.upn_collab.ui.view

import android.content.ContentValues
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.upn_collab.R
import com.upn_collab.network.dto.auth.DeviceDTO
import com.upn_collab.network.services.DeviceService
import com.upn_collab.network.services.UserService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var deviceService: DeviceService

    @Inject
    lateinit var userService: UserService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerDeviceNotification()
        loadNivelUser()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val next : LinearLayout = view.findViewById(R.id.forumClick)
        next.setOnClickListener{
            val fragment = ForumListAllFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }

    }

    private fun loadNivelUser(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = userService.addNivelUpdateUser()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                    }
                } else {
                    Log.e("Error", "Error al actualizar el nivel ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Exception", e)
            }
        }
    }

    private fun registerDeviceNotification(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result

                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                val tokenSave = preferences.getString("DEVICE_ID", "")

                if (token != null && token != tokenSave) {

                    val dispositivo = DeviceDTO(token)

                    lifecycleScope.launch {
                        val response = deviceService.registerDevice(dispositivo)
                        if (response.isSuccessful) {
                            val dResponse = response.body()
                            if (dResponse?.rpta == 1) {
                                val dispositivoSaved = dResponse.body
                                val editor = preferences.edit()
                                editor.putString("DEVICE_ID", dispositivoSaved?.token_device)
                                editor.apply()
                            }
                        } else {
                            // Manejo del error de la respuesta
                            Log.e("RegisterDevice", "Error: ${response.errorBody()?.string()}")
                        }
                    }
                }
            }
    }

}