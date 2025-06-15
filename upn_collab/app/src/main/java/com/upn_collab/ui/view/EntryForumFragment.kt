package com.upn_collab.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.upn_collab.R
import com.upn_collab.core.TokenExpiredException
import com.upn_collab.databinding.FragmentEntryForumBinding

import com.upn_collab.network.dto.auth.ReactionCommentDTO

import com.upn_collab.network.dto.response.CommentsResponse
import com.upn_collab.network.dto.response.EntryForumResponse
import com.upn_collab.network.dto.response.ReactionsResponse
import com.upn_collab.network.services.CommentService
import com.upn_collab.ui.adapters.commentsList.CommentDataAdapter

import com.upn_collab.ui.adapters.entryForum.EntryCommentDataAdapter

import com.upn_collab.ui.adapters.reactionAdapter.ReactionDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class EntryForumFragment : Fragment() {

    @Inject
    lateinit var commentService: CommentService

    private var forumId: Long? = null

    private var _binding: FragmentEntryForumBinding? = null
    private val binding get() = _binding!!

    private lateinit var commentDataAdapter: CommentDataAdapter
    private lateinit var entryCommentDataAdapter: EntryCommentDataAdapter
    private lateinit var reactionDataAdapter: ReactionDataAdapter


    private val data = mutableListOf<CommentsResponse>()
    private val data_entry = mutableListOf<EntryForumResponse>()
    private val data_reaction = mutableListOf<ReactionsResponse>()

    private lateinit var dialog :BottomSheetDialog
    private lateinit var recycleView: RecyclerView

    private var isLastPage = false
    private var isLoading = false
    private var currentPage = 0
    private val pageSize = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            forumId = it.getLong("FORUM_ID")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleAll()
        fetchData()
        binding.viewCommentAdd.setOnClickListener{
            showBottomSheetComment()
        }


        binding.listComments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visibleItemCount = layoutManager?.childCount ?: 0
                val totalItemCount = layoutManager?.itemCount ?: 0
                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0

                if (!isLoading && !isLastPage && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    getCommentForForums(currentPage)
                }
            }
        })
        getCommentForForums(currentPage)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntryForumBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun initRecycleAll() {
        binding.listComments.layoutManager = LinearLayoutManager(context)
        commentDataAdapter = CommentDataAdapter(data)
        binding.listComments.adapter = commentDataAdapter

        binding.itemEntryForum.layoutManager = LinearLayoutManager(context)
        entryCommentDataAdapter = EntryCommentDataAdapter(data_entry) { url -> redirectUrlIntent(url) }
        binding.itemEntryForum.adapter = entryCommentDataAdapter
    }

    private fun showBottomSheetComment(){
        reactions()
        val dialogView = layoutInflater.inflate(R.layout.comment_sheet,null)
        dialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        recycleView = dialogView.findViewById(R.id.listReactions)

        recycleView.layoutManager = GridLayoutManager(context,2)
        reactionDataAdapter = ReactionDataAdapter(data_reaction) { id_reaction -> submitComment(id_reaction)}

        recycleView.adapter = reactionDataAdapter
        dialog.show()
    }

    private fun submitComment(id_reaction: Long) {
       val reactionCommentDTO = ReactionCommentDTO(forumId,id_reaction)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = commentService.addComment(reactionCommentDTO)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            refreshComments()
                        } else {
                            showToast("Error al cargar los datos comments.")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Error al cargar los datos.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos.")
                }
            }
        }
    }

    private fun refreshComments() {
        currentPage = 0
        data.clear()
        getCommentForForums(currentPage)
    }


    private fun reactions(){
        lifecycleScope.launch {
            val reactionjob = async(Dispatchers.IO) {
                getReactions()
            }
            reactionjob.await()
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            val commentsJob = async(Dispatchers.IO) {
                getCommentForForumsEntry()
            }
            commentsJob.await()
        }
    }

    private fun redirectUrlIntent(url:String){
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,"Echa un vistazo a este foro: $url")
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE,"Comparte este foro a tus contactos")
        }
        val shareIntent = Intent.createChooser(intent,null)
        startActivity(shareIntent)
    }

    private suspend fun getCommentForForumsEntry() {

            try {
                val response = commentService.getEntryForumComment(forumId)
                if (response.isSuccessful) {
                    val dataForums = response.body()
                    withContext(Dispatchers.Main) {
                        dataForums?.let {
                            data_entry.clear()
                            data_entry.addAll(it)
                            entryCommentDataAdapter.notifyDataSetChanged()

                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Error al cargar los datos.")
                    }
                }
            } catch (e: TokenExpiredException) {
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos.")
                }
            }

    }

    private suspend fun getReactions() {
            try {
                val response = commentService.getAllReactionsComments()
                if (response.isSuccessful) {
                    val dataForums = response.body()
                    withContext(Dispatchers.Main) {
                        dataForums?.let {
                            data_reaction.clear()
                            data_reaction.addAll(it)
                            reactionDataAdapter.notifyDataSetChanged()

                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Error al cargar los datos reaction 1.")
                    }
                }
            } catch (e: TokenExpiredException) {
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos reaction 2.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos reaction 3.")
                }
            }

    }


    private fun getCommentForForums(page: Int) {

        isLoading = true
        binding.progressBarComment.visibility = View.VISIBLE

        lifecycleScope.launch {

            try {
                val response = commentService.getCommentForId(forumId, page = page, size = pageSize)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val dataForums = response.body() ?: emptyList()
                        if (page == 0) {
                            data.clear()
                        }
                        data.addAll(dataForums)
                        commentDataAdapter.notifyDataSetChanged()
                        currentPage++
                        isLastPage = dataForums.size < pageSize
                    } else {
                        showToast("Error al cargar los datos 1.")
                    }
                }
            } catch (e: TokenExpiredException) {
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos 2.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar los datos 3.")
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.progressBarComment.visibility = View.GONE
                    isLoading = false
                }
            }
        }
    }





    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}