package com.upn_collab.ui.view

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.upn_collab.R
import com.upn_collab.databinding.ActivityMainBinding
import com.upn_collab.network.dto.auth.DeviceDTO
import com.upn_collab.network.dto.auth.LoginDTO
import com.upn_collab.network.services.AuthServices
import com.upn_collab.network.services.DeviceService
import com.upn_collab.storage.LocalStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var authServices: AuthServices

    @Inject
    lateinit var localStorage: LocalStorage


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



    fun redirectRegister(v:View){
        val redericRegister: Intent = Intent(this, RegisterActivity::class.java)
        startActivity(redericRegister)
    }

    fun btnLogin(v:View){
        authetication()
    }

    //PETICION LOGIN HTTP
    private fun authetication() {

        val gmail: String = binding.gmailUpn.text.toString()
        val password: String = binding.password.text.toString()


        GlobalScope.launch {
            val loginDto = LoginDTO(gmail, password)
            val response = authServices.login(loginDto)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    localStorage.saveToken(data.jwt)
                    withContext(Dispatchers.Main){
                        val redericHomeAuthentication = Intent(this@MainActivity, DisingMenuLateral::class.java)
                        startActivity(redericHomeAuthentication)
                        finish()
                    }
                }
            }else{
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "No registrado", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}