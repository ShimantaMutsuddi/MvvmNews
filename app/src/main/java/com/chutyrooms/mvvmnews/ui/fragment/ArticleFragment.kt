package com.chutyrooms.mvvmnews.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.chutyrooms.mvvmnews.R
import com.chutyrooms.mvvmnews.databinding.FragmentArticleBinding
import com.chutyrooms.mvvmnews.ui.NewsActivity
import com.chutyrooms.mvvmnews.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
     var _binding: FragmentArticleBinding?=null
    private val binding get() = _binding!!
    private  val TAG = "ArticleFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= (activity as NewsActivity).viewModel!!

        val article=args.article

        binding?.webView.apply {
            this!!.webViewClient = WebViewClient()

            Log.d(TAG, "onViewCreated: "+article.url)

            loadUrl(article.url.toString())
        }

        binding?.fab?.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article saved successfully",Snackbar.LENGTH_SHORT)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

}