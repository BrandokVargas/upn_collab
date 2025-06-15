package com.upn_collab.ui.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.upn_collab.databinding.FragmentAdminChangeStateBinding
import com.upn_collab.network.dto.auth.NotificationDTO
import com.upn_collab.network.dto.auth.TypeStateDTO
import com.upn_collab.network.dto.response.ForumAllPendingResponse
import com.upn_collab.network.services.DeviceService
import com.upn_collab.network.services.ForumListPendingService
import com.upn_collab.network.services.UpdateStateForumService
import com.upn_collab.ui.adapters.forumListPending.ForumPendingDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class AdminChangeStateFragment : Fragment() {

    @Inject
    lateinit var forumListPendingService: ForumListPendingService

    @Inject
    lateinit var updateStateForumService: UpdateStateForumService

    @Inject
    lateinit var deviceService: DeviceService

    private var _binding: FragmentAdminChangeStateBinding? = null
    private val binding get() = _binding!!

    private lateinit var forumPendingDataAdapter: ForumPendingDataAdapter
    private val data = mutableListOf<ForumAllPendingResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminChangeStateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerForumsPending()
        getAllForumsPending()
    }

    private fun initRecyclerForumsPending(){
        forumPendingDataAdapter = ForumPendingDataAdapter(data,
            acceptClickListener = { forumId,id_user -> updateForumState(forumId, 2,id_user) },
            refusedClickListener = { forumId,id_user -> updateForumState(forumId, 3,id_user) },
            redirectIntentUrl = { url -> redirectUrlAdmin(url) }
        )
        binding.forumList.layoutManager = LinearLayoutManager(context)
        binding.forumList.adapter = forumPendingDataAdapter
    }

    private fun redirectUrlAdmin(url:String){

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("http://"+url)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Url no http", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateForumState(forumId: Long, state: Long,id_user:Long) {

        val typeStateDTO = TypeStateDTO(state)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = updateStateForumService.updateForumState(forumId, typeStateDTO)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        fetchNotificationUser(id_user,state)
                        getAllForumsPending()
                    }
                } else {
                    Log.e("Error", "Error al actualizar ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Exception", e)
            }
        }
    }


    private fun fetchNotificationUser(id_user: Long, state: Long) {
        val notficationBodyUser: NotificationDTO = if (state == 2L) {
            NotificationDTO(
                title = "Foro aceptado",
                message = "Hola tu foro ha sido aprobado"
            )
        } else {
            NotificationDTO(
                title = "Foro Rechazado",
                message = "Hola tu foro ha sido rechazado"
            )
        }

        val notficationBodyAll: NotificationDTO = NotificationDTO(
            title = "Han publicado un nuevo foro",
            message = "Un usuario ha subido un nuevo foro revisalo"
        )

        lifecycleScope.launch {
            coroutineScope {
                val userNotificationJob = async(Dispatchers.IO) {
                    sendNotificationStateForum(id_user, notficationBodyUser)
                }
                val allNotificationJob = async(Dispatchers.IO) {
                    sendNotificationAll(notficationBodyAll)
                }
                userNotificationJob.await()
                allNotificationJob.await()
            }
        }
    }



    private suspend fun sendNotificationStateForum(id_user:Long,notficationBody:NotificationDTO){

        try {
            val response = deviceService.sendNotification(id_user,notficationBody)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {

                }
            } else {
                Log.e("Error", "Error al eviar notificacion ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Error", "Exception", e)
        }
    }

    private suspend fun sendNotificationAll(notficationBody: NotificationDTO){

        try {
            val response = deviceService.sendNotificationAll(notficationBody)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {

                }
            } else {
                Log.e("Error", "Error al eviar notificacion ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Error", "Exception", e)
        }

    }


    private fun getAllForumsPending() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = forumListPendingService.getAllForumsPending()
                if (response.isSuccessful) {
                    val dataForums = response.body()
                    withContext(Dispatchers.Main) {
                        dataForums?.let {
                            data.clear()
                            data.addAll(it)
                            forumPendingDataAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    Log.e("ForumListAllFragment", "Error al obtener foro: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ForumListAllFragment", "Exception forums", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}