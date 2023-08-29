package com.example.newsapp_2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp_2.databinding.ItemArticlePreviewBinding
import com.example.newsapp_2.models.Article

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(val binding : ItemArticlePreviewBinding) : ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
            )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(article.urlToImage)
            .into(holder.binding.ivArticleImage)

//        holder.binding.tvSource.text = article.source.name
        holder.binding.tvTitle.text = article.title
        holder.binding.tvDescription.text = article.description
        holder.binding.tvPublishedAt.text = article.publishedAt

        holder.itemView.setOnClickListener {
            onItemClickListner?.let {
                it(article)
            }
        }
    }
    private var onItemClickListner: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listner: (Article) -> Unit){
        onItemClickListner = listner
    }
}