package com.slapshotapps.swimyardagetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection



class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this); // Call before super!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}
