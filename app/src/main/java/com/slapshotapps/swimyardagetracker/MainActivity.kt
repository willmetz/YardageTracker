package com.slapshotapps.swimyardagetracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this); // Call before super!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupNavigation()
    }


    private fun setupNavigation() {
        val navController = findNavController(this, R.id.main_nav_fragment)
        setupWithNavController(bottom_nav, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.homeFragment){
                bottom_nav.visibility = View.VISIBLE
            }else{
                bottom_nav.visibility = View.GONE
            }
        }
    }
}
