package com.chutyrooms.mvvmnews.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chutyrooms.mvvmnews.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        //nav controller is an object that manages app navigation within a NavHost
        val navController=findNavController(R.id.newsNavHostFragment)

        bottomNavigationView.setupWithNavController(navController)
    }


}