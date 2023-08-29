package com.example.newsapp_2.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp_2.R
import com.example.newsapp_2.adapters.NewsAdapter
import com.example.newsapp_2.databinding.FragmentBreakingNewsBinding
import com.example.newsapp_2.databinding.FragmentSearchNewsBinding
import com.example.newsapp_2.ui.activity.MainActivity
import com.example.newsapp_2.ui.viewModel.NewsViewModel
import com.example.newsapp_2.utils.Constants
import com.example.newsapp_2.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    private lateinit var binding: FragmentSearchNewsBinding
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
        binding = FragmentSearchNewsBinding.inflate(inflater)
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
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }


        var job: Job? = null // corotutine job
        //whenever text is changed,
        binding.etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
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
        binding.rvSearchNews.apply {
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