package com.chutyrooms.mvvmnews.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.setupWithNavController
import com.chutyrooms.mvvmnews.R
import com.chutyrooms.mvvmnews.db.ArticleDatabase
import com.chutyrooms.mvvmnews.repository.NewsRepository
import com.chutyrooms.mvvmnews.repository.NewsViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class NewsActivity : AppCompatActivity() {

     var viewModel:NewsViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        //nav controller is an object that manages app navigation within a NavHost
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
       // val navController= NavHostFragment.findNavController(R.id.newsNavHosContFragment)

        val newsRepository=NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory=NewsViewModelProviderFactory(application,newsRepository)
        viewModel= ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]
        bottomNavigationView.setupWithNavController(navController)




    }


}