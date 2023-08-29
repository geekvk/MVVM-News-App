package com.example.newsapp_2.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp_2.R
import com.example.newsapp_2.databinding.FragmentArticleBinding
import com.example.newsapp_2.ui.activity.MainActivity
import com.example.newsapp_2.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    private lateinit var binding:FragmentArticleBinding

    val args:ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArticleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)

        }

        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved", Snackbar.LENGTH_SHORT).show()
        }
    }
    
}