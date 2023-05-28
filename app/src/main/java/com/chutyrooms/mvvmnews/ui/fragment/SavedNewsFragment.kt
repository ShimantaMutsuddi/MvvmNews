package com.chutyrooms.mvvmnews.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chutyrooms.mvvmnews.R
import com.chutyrooms.mvvmnews.adapters.NewsAdapter
import com.chutyrooms.mvvmnews.databinding.FragmentSavedNewsBinding
import com.chutyrooms.mvvmnews.databinding.FragmentSearchNewsBinding
import com.chutyrooms.mvvmnews.ui.NewsActivity
import com.chutyrooms.mvvmnews.ui.NewsViewModel


class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private  val TAG = "SearchNewsFragment"
    private var _binding: FragmentSavedNewsBinding?=null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= (activity as NewsActivity).viewModel!!
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        _binding!!.rvSavedNews?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
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
            _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
            return binding.root
        }


}