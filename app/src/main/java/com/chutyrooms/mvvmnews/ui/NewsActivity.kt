package com.chutyrooms.mvvmnews.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chutyrooms.mvvmnews.R
import com.chutyrooms.mvvmnews.db.ArticleDatabase
import com.chutyrooms.mvvmnews.repository.NewsRepository
import com.chutyrooms.mvvmnews.repository.NewsViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class NewsActivity : AppCompatActivity() {

    lateinit var viewModel:NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        //nav controller is an object that manages app navigation within a NavHost
        val navController=findNavController(R.id.newsNavHostFragment)

        val newsRepository=NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory=NewsViewModelProviderFactory(newsRepository)
        viewModel=ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(navController)




    }


}