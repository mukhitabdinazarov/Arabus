package com.jibergroup.arabus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.jibergroup.arabus.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = host.`navController`
        openSearchFragment()

        openSearchFragment()
        bottom_navigation_view.setNavigationChangeListener { _, position ->
            when (position) {
                0 -> openSearchFragment()
                1 -> mNavController.navigate(R.id.favoritesFragment)
            }
        }
    }

    private fun openSearchFragment() {
        mNavController.navigate(R.id.searchFragment)
    }

    override fun onBackPressed() {
        if(mNavController.currentDestination == null
            || mNavController.currentDestination!!.id == R.id.searchFragment){
            super.onBackPressed()
        }
        else{
            mNavController.navigateUp()
        }
    }
}