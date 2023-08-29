package com.example.newsapp_2.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp_2.R
import com.example.newsapp_2.adapters.NewsAdapter
import com.example.newsapp_2.databinding.FragmentBreakingNewsBinding
import com.example.newsapp_2.ui.activity.MainActivity
import com.example.newsapp_2.ui.viewModel.NewsViewModel
import com.example.newsapp_2.utils.Resource

class BreakingNewsFragment : Fragment() {
    private lateinit var binding:FragmentBreakingNewsBinding
    lateinit var newsAdapter: NewsAdapter
    private lateinit var viewModel : NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()

        newsAdapter.setOnItemClickListener {article->
            val bundle = Bundle().apply {
                putSerializable("article", article)

            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar(true)
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar(true)
                    response.message?.let {message ->
                        Log.e("BRFragment", "Error : $message")
                    }
                }
                is Resource.Loading -> {
                    hideProgressBar(false)
                }
            }
        }
    }

    private fun setupRecycleView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    private fun hideProgressBar(isVisible : Boolean){
        if (isVisible){
            binding.paginationProgressBar.visibility = View.INVISIBLE
        }else{
            binding.paginationProgressBar.visibility = View.VISIBLE
        }
    }
    
}