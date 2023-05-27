package com.chutyrooms.mvvmnews.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.chutyrooms.mvvmnews.R
import com.chutyrooms.mvvmnews.adapters.NewsAdapter
import com.chutyrooms.mvvmnews.databinding.FragmentSearchNewsBinding
import com.chutyrooms.mvvmnews.ui.NewsActivity
import com.chutyrooms.mvvmnews.ui.NewsViewModel
import com.chutyrooms.mvvmnews.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.chutyrooms.mvvmnews.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    private  val TAG = "SearchNewsFragment"
    lateinit var newsAdapter: NewsAdapter
    private var _binding: FragmentSearchNewsBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel= (activity as NewsActivity).viewModel!!
        setupRecyclerView()

        var job: Job?=null

        _binding?.etSearch?.addTextChangedListener { editable ->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)

                editable?.let {
                    if(editable.toString().isNotEmpty())
                    {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }




        viewModel.searchNews.observe(viewLifecycleOwner){ response ->

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


    }

    private fun hideProgressBar() {
        _binding!!.paginationProgressBar?.visibility=View.INVISIBLE
    }
    private fun showProgressBar() {
        _binding!!.paginationProgressBar?.visibility=View.VISIBLE
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  val layout =  inflater.inflate(R.layout.fragment_breaking_news, container, false)
        //rvBreakingNews=layout.findViewById(R.id.rvBreakingNews)
        // paginationProgressBar=layout.findViewById(R.id.paginationProgressBar)
        // return layout
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        _binding!!.rvSearchNews?.apply{
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)

        }



    }


}