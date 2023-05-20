package com.chutyrooms.mvvmnews.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chutyrooms.mvvmnews.R
import com.chutyrooms.mvvmnews.adapters.NewsAdapter

import com.chutyrooms.mvvmnews.ui.NewsActivity
import com.chutyrooms.mvvmnews.ui.NewsViewModel
import com.chutyrooms.mvvmnews.utils.Resource


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var rvBreakingNews: RecyclerView
    lateinit var paginationProgressBar: ProgressBar

    //private  var fragmentBreakingNewsBinding : FragmentBreakingNewsBinding? = null
    private  val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // val binding = FragmentBreakingNewsBinding.bind(view)
        //fragmentBreakingNewsBinding = binding


        viewModel= (activity as NewsActivity).viewModel!!
        setupRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner){ response ->

            when(response)
            {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, "An error occured: $message ")

                    }
                }

                is Resource.Loading ->{
                    showProgressBar()
                }
            }

        }



        //rvBreakingNews=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

    }
    /*override fun onDestroyView() {
        // Consider not storing the binding instance in a field
        // if not needed.
        fragmentBreakingNewsBinding = null
        super.onDestroyView()
    }*/

    private fun hideProgressBar() {
        paginationProgressBar?.visibility=View.INVISIBLE
    }
    private fun showProgressBar() {
       paginationProgressBar?.visibility=View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout =  inflater.inflate(R.layout.fragment_breaking_news, container, false)
        rvBreakingNews=layout.findViewById(R.id.rvBreakingNews)
        paginationProgressBar=layout.findViewById(R.id.paginationProgressBar)



        return layout
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvBreakingNews?.apply{
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)

        }



    }

}