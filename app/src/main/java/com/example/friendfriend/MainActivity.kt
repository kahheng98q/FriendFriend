package com.example.friendfriend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.friendfriend.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
//    lateinit var bundle:Bundle

    lateinit var selfName: String
    lateinit var selfEmail: String
    lateinit var selfImage: String
    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)

        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
//        selfEmail ="AAAAAAA"
//        selfName="BBBBBBBBBBB"
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        getCurrentUser()
//        Toast.makeText(this, selfName,Toast.LENGTH_SHORT).show()
//        bundle.putString("gg","ggg")
//        home.arguments=bundle
//        val bundle = Bundle()
//        bundle.putString("key", "value")

//        val fragment =Home()
//        fragment.arguments = bundle
//bundle.getString("key")
//        Toast.makeText(this,bundle.getString("key"),Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
    override fun onBackPressed() {
        //the code is beautiful enough without comments
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun getCurrentUser(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {

//            selfEmail = user.email.toString()
//            selfName=user.displayName.toString()


        }
    }

}
