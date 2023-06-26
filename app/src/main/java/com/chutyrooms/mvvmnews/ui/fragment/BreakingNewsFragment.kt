package com.chutyrooms.mvvmnews.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chutyrooms.mvvmnews.R
import com.chutyrooms.mvvmnews.adapters.NewsAdapter
import com.chutyrooms.mvvmnews.databinding.FragmentBreakingNewsBinding

import com.chutyrooms.mvvmnews.ui.NewsActivity
import com.chutyrooms.mvvmnews.ui.NewsViewModel
import com.chutyrooms.mvvmnews.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.chutyrooms.mvvmnews.utils.Resource


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    //lateinit var rvBreakingNews: RecyclerView
   // lateinit var paginationProgressBar: ProgressBar
    private var _binding: FragmentBreakingNewsBinding?=null
    private val binding get() = _binding!!
    var isLoading=false
    var isLastPage=false
    var isScrolling=false
    var isError = false


    //private  var fragmentBreakingNewsBinding : FragmentBreakingNewsBinding? = null
    private  val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // val binding = FragmentBreakingNewsBinding.bind(view)
        //fragmentBreakingNewsBinding = binding


        viewModel= (activity as NewsActivity).viewModel!!
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {

            Log.d(TAG, "onClick ->BreakingNews: "+it.toString())
            var bundle=Bundle().apply {
                putSerializable("article",it)
            }

            /*findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )*/
             findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )

        }

        viewModel.breakingNews.observe(viewLifecycleOwner){ response ->

            when(response)
            {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles?.toList())
                        val totalPages= newsResponse.totalResults!! / QUERY_PAGE_SIZE + 2
                        isLastPage= viewModel.breakingNewsPage==totalPages

                        if (isLastPage)
                        {
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, "An error occured: $message ")
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)

                    }
                }

                is Resource.Loading ->{
                    showProgressBar()
                }
            }

        }
        binding.itemErrorMessage.btnRetry.setOnClickListener {
            viewModel.getBreakingNews("us")
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
        _binding!!.paginationProgressBar?.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar() {
        _binding!!.paginationProgressBar?.visibility=View.VISIBLE
        isLoading=true
    }
    private fun showErrorMessage(message: String) {
        _binding?.itemErrorMessage?.root?.visibility = View.VISIBLE
        _binding?.itemErrorMessage?.tvErrorMessage?.text = message
        isError = true
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*val layout =  inflater.inflate(R.layout.fragment_breaking_news, container, false)
        rvBreakingNews=layout.findViewById(R.id.rvBreakingNews)
        paginationProgressBar=layout.findViewById(R.id.paginationProgressBar)
        return layout*/
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    val scrollListener=object : RecyclerView.OnScrollListener()
    {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
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
            val isTotalMorethanVisible= totalItemCount>= QUERY_PAGE_SIZE

            val shouldPaginate= isNotLoadingAndNotLastPase && isAtLastItem && isNotAtBegining
                    && isTotalMorethanVisible && isScrolling
            if(shouldPaginate)
            {
                viewModel.getBreakingNews("us")
                isScrolling=false
            }


        }
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        _binding?.rvBreakingNews?.apply{
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)

        }



    }

}