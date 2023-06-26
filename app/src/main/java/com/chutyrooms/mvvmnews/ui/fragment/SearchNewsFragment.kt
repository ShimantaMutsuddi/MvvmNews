package com.chutyrooms.mvvmnews.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chutyrooms.mvvmnews.R
import com.chutyrooms.mvvmnews.adapters.NewsAdapter
import com.chutyrooms.mvvmnews.databinding.FragmentSearchNewsBinding
import com.chutyrooms.mvvmnews.ui.NewsActivity
import com.chutyrooms.mvvmnews.ui.NewsViewModel
import com.chutyrooms.mvvmnews.utils.Constants
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
    var isLoading=false
    var isLastPage=false
    var isScrolling=false
    var isError=false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel= (activity as NewsActivity).viewModel!!
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            Log.d(TAG, "onClick ->SearchNews: "+it.toString())
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )

        }



        var job: Job?=null

        binding?.etSearch?.addTextChangedListener { editable ->
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



        binding.itemErrorMessage.btnRetry.setOnClickListener {
            if (binding.etSearch.text.toString().isNotEmpty()) {
                viewModel.searchNews(binding.etSearch.text.toString())
            } else {
                hideErrorMessage()
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner){ response ->

            when(response)
            {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles?.toList())
                        val totalPages= newsResponse.totalResults!! / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage= viewModel.searchNewsPage==totalPages
                        if (isLastPage)
                        {
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, "An error occured: $message ")
                        showErrorMessage(message)

                    }
                }

                is Resource.Loading ->{
                    showProgressBar()
                }
            }

        }


    }
    private fun hideErrorMessage() {
        binding.itemErrorMessage.root.visibility = View.INVISIBLE
        isError = false
    }

    private fun hideProgressBar() {
        _binding!!.paginationProgressBar?.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar() {
        _binding!!.paginationProgressBar?.visibility=View.VISIBLE
        isLoading=true
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
    private fun showErrorMessage(message: String) {
        binding.itemErrorMessage.root.visibility = View.VISIBLE
        binding.itemErrorMessage.tvErrorMessage.text = message
        isError = true
    }


    val scrollListener=object : RecyclerView.OnScrollListener()
     {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling=true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPase=!isLoading && !isLastPage
            val isAtLastItem= firstVisibleItemPosition+visibleItemCount >=totalItemCount

            val isNotAtBegining= firstVisibleItemPosition >= 0
            val isTotalMorethanVisible= totalItemCount>= Constants.QUERY_PAGE_SIZE

            val shouldPaginate= isNotLoadingAndNotLastPase && isAtLastItem && isNotAtBegining
                    && isTotalMorethanVisible && isScrolling
            if(shouldPaginate)
            {
                viewModel.searchNews(binding.etSearch.text.toString())
                isScrolling=false
            }


        }
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        _binding!!.rvSearchNews?.apply{
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)

        }



    }


}