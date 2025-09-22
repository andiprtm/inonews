package com.cpp.inonews.ui.adapter

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cpp.inonews.data.local.entity.ArticleEntity
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem
import com.cpp.inonews.databinding.ItemNewsBinding

class NewsAdapter(private val onItemCLickCallback: (ArticleEntity) -> Unit):
    PagingDataAdapter<ArticleEntity, NewsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ArticleEntity, onItemClickCallback: (ArticleEntity) -> Unit) {
            with(binding) {
                Glide.with(root.context)
                    .load(article.urlToImage)
                    .transform(RoundedCorners(32))
                    .into(pic)
                title.text = article.title
                desc.text = article.description
                root.setOnClickListener {
                    onItemClickCallback(article)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        if (article != null) {
            holder.bind(article, onItemCLickCallback)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleEntity>() {
            override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ArticleEntity,
                newItem: ArticleEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}