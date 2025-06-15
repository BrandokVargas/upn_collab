package com.upn_collab.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.upn_collab.R

import com.upn_collab.databinding.FragmentCreatedForumBinding
import com.upn_collab.network.dto.auth.CarrersDTO
import com.upn_collab.network.dto.auth.ForumCreatedDTO
import com.upn_collab.network.dto.auth.TypeForumDTO
import com.upn_collab.network.dto.response.CareersResponse
import com.upn_collab.network.dto.response.TypeForumResponse
import com.upn_collab.network.services.CareerServices
import com.upn_collab.network.services.CreatedForumService
import com.upn_collab.network.services.TypeForumService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@AndroidEntryPoint
class CreatedForumFragment : Fragment() {


    @Inject
    lateinit var typeForumService: TypeForumService
    @Inject
    lateinit var createdForumService: CreatedForumService

    private var _binding: FragmentCreatedForumBinding? = null
    private val binding get() = _binding!!
    private val selectedCareers = mutableListOf<String>()

    private var typeForumMap = mutableMapOf<Long, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatedForumBinding.inflate(inflater,container,false)
        return binding.root

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.101.6:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    private fun getAllCareers(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(CareerServices::class.java).getCareers()
            val dataCareers = call.body()
            if(call.isSuccessful && dataCareers!=null){
                //show comboboxo
                withContext(Dispatchers.Main){
                    addCareersUpnCollab(dataCareers)
                }
            }else{
                //show error
            }
        }
    }

    fun addCareersUpnCollab(dataCareers: List<CareersResponse>){
        val careersName = dataCareers.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, careersName)
        binding.careersAfines.setAdapter(adapter)
    }

    private fun carrersMemory() {
        binding.addCareerMemory.setOnClickListener {
            val selectedCareer = binding.careersAfines.text.toString()

            if (selectedCareer.isNotEmpty() && !selectedCareers.contains(selectedCareer) && selectedCareers.size <= 3 ) {
                selectedCareers.add(selectedCareer)
                addCareerLayout(selectedCareer)
            }else{
                Toast.makeText(requireContext(), "No puedes colocar mas de 4 carreras", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addCareerLayout(career: String) {
        val button = Button(requireContext()).apply {
            text = career
            textSize = 16f
            setPadding(8, 8, 8, 8)
            setBackgroundResource(R.drawable.btn_selection_carrers)
        }

        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            marginStart = 8
            marginEnd = 8
            topMargin = 8
            bottomMargin = 8
        }

        binding.selectedCareersContainer.addView(button,layoutParams)
    }


    ///TYPE FORUM LIST: CARGANDO COMBOBOX BD
    private fun getAllTypeForum(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = typeForumService.getAllTypeForum()
            val typeForums = call.body()
            if(call.isSuccessful && typeForums!=null){
                //show comboboxo
                withContext(Dispatchers.Main){
                    addTypeForumList(typeForums)
                }
            }else{
                //show error
            }
        }
    }

    fun addTypeForumList(dataTypeForums: List<TypeForumResponse>){
        typeForumMap = dataTypeForums.associate { it.id_type_foro to it.nameTypeForum }.toMutableMap()
        val typeForumsList = dataTypeForums.map { it.nameTypeForum }
        val adapter = ArrayAdapter(requireContext() , android.R.layout.simple_list_item_1, typeForumsList)
        binding.typeForumCreated.setAdapter(adapter)
    }



    private fun addForum(){
        val title: String = binding.titleForum.text.toString()
        val urlForm: String = binding.url.text.toString()
        val content: String = binding.description.text.toString()
        val typeForum: String = binding.typeForumCreated.text.toString()
        val carrersList: List<String> = selectedCareers

        val invertedTypeForumMap = typeForumMap.entries.associate { it.value to it.key }
        val typeForumId: Long? = invertedTypeForumMap[typeForum]

        if (typeForumId != null) {
            val typeForumDTO = TypeForumDTO(typeForumId)

            val careerName = CarrersDTO(nameCareers = carrersList)

            val forumCreatedDTO = ForumCreatedDTO(
                title = title,
                url = urlForm,
                content = content,
                typeForumEntity = typeForumDTO,
                careersRequest = careerName
            )
            saveForum(forumCreatedDTO)
        } else {
            Toast.makeText(requireContext(), "Seleccione un tipo de foro válido", Toast.LENGTH_SHORT).show()
        }

    }

    private fun saveForum(forumCreatedDTO: ForumCreatedDTO) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = createdForumService.createdForum(forumCreatedDTO)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Foro creado con éxito, espera a que un administrador apruebe tu foro", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val nextFragment = ForumListAllFragment()
                        val fragmentManager = requireActivity().supportFragmentManager
                        val transaction = fragmentManager.beginTransaction()
                        transaction.replace(R.id.fragment_container, nextFragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }, 3000)
                } else {
                    Toast.makeText(requireContext(), "Error al crear el foro", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddForum.setOnClickListener {
            btnAddForum(it)
        }

        carrersMemory()
        getAllCareers()
        getAllTypeForum()
    }

    //ADD FORUM
    fun btnAddForum(view: View){
        addForum()
    }

}