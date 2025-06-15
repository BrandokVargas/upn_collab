package com.upn_collab.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upn_collab.R
import com.upn_collab.core.TokenExpiredException
import com.upn_collab.databinding.FragmentForumBinding

import com.upn_collab.network.dto.response.MyForumsResponse
import com.upn_collab.network.services.ForumFavoriteService
import com.upn_collab.network.services.MyForumsService
import com.upn_collab.ui.adapters.myForumList.MyForumDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class ForumFragment : Fragment(),SearchView.OnQueryTextListener {

    @Inject
    lateinit var myForumsService: MyForumsService

    @Inject
    lateinit var forumFavoriteService : ForumFavoriteService

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!
    private lateinit var myForumDataAdapter: MyForumDataAdapter
    private val data = mutableListOf<MyForumsResponse>()
    private val filteredData = mutableListOf<MyForumsResponse>()

    private var isLastPage = false
    private var isLoading = false
    private var currentPage = 0
    private val pageSize = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchMyForum.setOnQueryTextListener(this)
        initRecyclerMyForums()
        val next : ImageButton = view.findViewById(R.id.addForum)
        next.setOnClickListener{
            val fragment = CreatedForumFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container,fragment)?.commit()
        }
        binding.myForumsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visibleItemCount = layoutManager?.childCount ?: 0
                val totalItemCount = layoutManager?.itemCount ?: 0
                val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0

                if (!isLoading && !isLastPage && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= pageSize) {
                    getMyForumsList(currentPage)
                }
            }
        })

        getMyForumsList(currentPage)
    }

    private fun initRecyclerMyForums(){
        myForumDataAdapter = MyForumDataAdapter(data)
        binding.myForumsList.layoutManager = LinearLayoutManager(context)
        binding.myForumsList.adapter = myForumDataAdapter
    }





    private fun getMyForumsList(page:Int) {

        isLoading = true
        binding.progressBarMyForums.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = myForumsService.getMyForums(page=page,size=pageSize)
                if (response.isSuccessful) {
                    val dataForums = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        dataForums?.let {
                            data.addAll(dataForums)
                            myForumDataAdapter.notifyDataSetChanged()
                            currentPage++
                            isLastPage = dataForums.size < pageSize
                            if (it.isEmpty()) {
                                _binding?.myForumsList?.visibility = View.GONE
                                _binding?.emptyView?.visibility = View.VISIBLE
                            } else {
                                _binding?.myForumsList?.visibility = View.VISIBLE
                                _binding?.emptyView?.visibility = View.GONE
                            }

                        }
                    }
                } else {
                    Log.e("ForumListAllFragment", "Error getting my forums: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ForumListAllFragment", "Exception getting my forums", e)
            }finally {
                withContext(Dispatchers.Main) {
                    _binding?.progressBarMyForums?.visibility = View.GONE
                    isLoading = false
                }
            }
        }
    }

    private fun searchMyForumByTesisOrArticulo(text:String){
        //isSearching = true
        //searchText = text
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = myForumsService.searchtMyForumsByTesisOrArticulo(text)
                if (response.isSuccessful) {
                    val dataForums = response.body()
                    withContext(Dispatchers.Main) {
                        data.clear()
                        dataForums?.let {
                            data.addAll(it)
                            myForumDataAdapter.notifyDataSetChanged()
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
    }


    override fun onQueryTextSubmit(text: String): Boolean {
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        if (!text.isNullOrEmpty()) {
            searchMyForumByTesisOrArticulo(text)
        } else {
            //isSearching = false // Salir del modo de búsqueda
            data.clear() // Limpiar la lista de datos antes de cargar la paginación
            currentPage = 0
            getMyForumsList(currentPage)
        }
        return true
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}