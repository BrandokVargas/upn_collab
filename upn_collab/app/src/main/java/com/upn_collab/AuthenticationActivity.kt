package com.upn_collab

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.upn_collab.databinding.ActivityMainBinding
import com.upn_collab.storage.LocalStorage
import com.upn_collab.ui.view.DisingMenuLateral
import com.upn_collab.ui.view.HomeActivity
import com.upn_collab.ui.view.HomeFragment
import com.upn_collab.ui.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class AuthenticationActivity:AppCompatActivity() {

    @Inject
    lateinit var localStorage: LocalStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(localStorage.isAuthenticated()){
            val intent = Intent(this,DisingMenuLateral::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}