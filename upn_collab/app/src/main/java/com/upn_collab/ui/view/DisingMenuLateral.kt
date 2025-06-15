package com.upn_collab.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.upn_collab.R
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.upn_collab.network.services.UserService
import com.upn_collab.storage.LocalStorage
import com.upn_collab.storage.SharePreferencesStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class DisingMenuLateral : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var localStorage: LocalStorage

    @Inject
    lateinit var userService: UserService

    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navegationView: NavigationView

    private lateinit var nameUser: TextView
    private lateinit var emailUser: TextView
    private lateinit var profileImageUser: ImageView
    private var nameNivel:String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dising_menu_lateral)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        navegationView = findViewById(R.id.nav_view)
        navegationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.black)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val headerView = navegationView.getHeaderView(0)
        nameUser = headerView.findViewById(R.id.nameUser)
        emailUser = headerView.findViewById(R.id.emailUser)
        profileImageUser = headerView.findViewById(R.id.profileImageUser)

        adjustMenuBasedOnRole()
        loadUserProfile()
        /*if(savedInstanceState==null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,HomeFragment()).commit()
            navegationView.setCheckedItem(R.id.navHome)
        }*/
        val fragment = intent.getStringExtra("openFragment")
        if (fragment != null) {
            openFragment(fragment)
        } else {
            // Si no hay fragmento en el intent, abre el HomeFragment por defecto
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navegationView.setCheckedItem(R.id.navHome)
        }
    }
    private fun openFragment(fragmentName: String) {
        val fragment: Fragment = when (fragmentName) {
            "ForumFragment" -> ForumFragment()
            "FavoriteFragment" -> FavoriteFragment()
            "AdminHomeFragment" -> AdminHomeFragment()
            else -> HomeFragment() // Fragmento por defecto
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun loadUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = userService.getProfileUserAuthenticate()
                if (response.isSuccessful) {
                    val profile = response.body()

                    withContext(Dispatchers.Main) {
                        profile?.let {
                            nameUser.text = it.name
                            emailUser.text = it.email_upn
                            nameNivel = it.nivel_name

                            addPorfileImageNivel(nameNivel)

                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        showToast("Error al cargar el perfil.")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showToast("Error al cargar el perfil.")
                }
            }
        }
    }

    private fun addPorfileImageNivel(level:String){
        when (level) {
            "INV. APRENDIZ" -> profileImageUser.setImageResource(R.drawable.aprendiz)
            "INV. PERSPICAZ" -> profileImageUser.setImageResource(R.drawable.perspicaz)
            "INV. PROMETEDOR" -> profileImageUser.setImageResource(R.drawable.prometedor)
            "INV. PLATINIUM" -> profileImageUser.setImageResource(R.drawable.platinium)
            "INV. EXPERTO" -> profileImageUser.setImageResource(R.drawable.experto)
            else -> profileImageUser.setImageResource(R.drawable.aprendiz)
        }
    }

    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHome -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.navForum -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ForumFragment()).commit()
            R.id.navFavorite -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FavoriteFragment()).commit()
            R.id.navAdmin -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AdminHomeFragment()).commit()
            R.id.navExit -> logout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }*/
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHome -> openFragment("HomeFragment")
            R.id.navForum -> openFragment("ForumFragment")
            R.id.navFavorite -> openFragment("FavoriteFragment")
            R.id.navAdmin -> openFragment("AdminHomeFragment")
            R.id.navExit -> logout()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        localStorage.clearToken()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        this.finish()
    }


    private fun adjustMenuBasedOnRole() {
        val userRole = (localStorage as SharePreferencesStorage).getUserRole()
        val navMenu = navegationView.menu
        val adminItem = navMenu.findItem(R.id.navAdmin)

        adminItem.isVisible = userRole.contains("ROLE_ADMIN")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}