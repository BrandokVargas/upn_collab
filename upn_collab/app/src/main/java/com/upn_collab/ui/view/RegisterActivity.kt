package com.upn_collab.ui.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import com.upn_collab.R
import com.upn_collab.databinding.ActivityRegisterBinding
import com.upn_collab.network.dto.auth.CarrersDTO
import com.upn_collab.network.dto.auth.RegisterDTO
import com.upn_collab.network.dto.response.CareersResponse
import com.upn_collab.network.services.AuthServices
import com.upn_collab.network.services.CareerServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var authServices: AuthServices

    private val selectedCareers = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            btnRegister(it)
        }


        getAllCareers()
        carrersMemory()
    }


    private fun getRetrofit():Retrofit{
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


    fun redirectLogin(v:View){
        val redirectLogin: Intent = Intent(this, MainActivity::class.java)
        startActivity(redirectLogin)
    }

    fun addCareersUpnCollab(dataCareers: List<CareersResponse>){
        val careersName = dataCareers.map { it.name }
        val adapter = ArrayAdapter(this , android.R.layout.simple_list_item_1, careersName)
        binding.careers.setAdapter(adapter)
    }

    private fun carrersMemory() {
        binding.addCareerMemory.setOnClickListener {
            val selectedCareer = binding.careers.text.toString()

            if (selectedCareer.isNotEmpty() && !selectedCareers.contains(selectedCareer) && selectedCareers.size <= 1 ) {
                selectedCareers.add(selectedCareer)
                addCareerLayout(selectedCareer)
            }else{
                Toast.makeText(this, "No puedes colocar mas de 2 carreras", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addCareerLayout(career: String) {
        val button = Button(this).apply {
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

        binding.containerCareers.addView(button,layoutParams)
    }


    private fun register(){
        val cod_upn: String = binding.codUpn.text.toString()
        val name: String = binding.name.text.toString()
        val lastname: String = binding.lastname.text.toString()
        val email: String = binding.gmailUpn.text.toString()
        val carrersList: List<String> = selectedCareers

        val careerName = CarrersDTO(nameCareers = carrersList)

        val registerDTo = RegisterDTO(
            cod_upn = cod_upn,
            name = name,
            lastname = lastname,
            email = email,
            careersRequest = careerName
        )
        sendRegister(registerDTo)


    }

    private fun sendRegister(registerDTO: RegisterDTO) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = authServices.register(registerDTO)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Usuario registrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "No te haz podido registrar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun btnRegister(view: View){
        register()
    }





}