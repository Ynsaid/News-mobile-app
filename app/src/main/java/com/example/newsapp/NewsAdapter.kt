package com.example.newsapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class NewsAdapter(private val articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    // Total number of items
    override fun getItemCount() = articles.size

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)

        // Set click listener
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ItemActivity::class.java).apply {
                putExtra("article", article)
            }
            context.startActivity(intent)
        }
    }

    // ViewHolder class to hold references to views in each item
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.news_title)
        private val subtitle: TextView = itemView.findViewById(R.id.news_subtitle)
        private val source: TextView = itemView.findViewById(R.id.news_source)
        private val date: TextView = itemView.findViewById(R.id.news_date)
        private val author: TextView = itemView.findViewById(R.id.news_author)
        private val imageView: ImageView = itemView.findViewById(R.id.img)

        fun bind(article: Article) {
            title.text = article.title ?: "No Title"
            subtitle.text = article.description ?: "No Description"
            source.text = article.sourceName ?: "Unknown Source"
            author.text = if (!article.author.isNullOrEmpty()) article.author else "Unknown Author"

            // Format date: take first 10 characters (YYYY-MM-DD)
            val formattedDate = article.publishedAt?.take(10) ?: "Unknown Date"
            date.text = formattedDate

            // Load image with Glide
            if (article.urlToImage != null) {
                Glide.with(itemView.context)
                    .load(article.urlToImage)
                    .thumbnail(0.1f)
                    .override(100, 100)
                    .placeholder(R.drawable.ic_img)
                    .error(R.drawable.ic_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)

            } else {
                // Use a default image if urlToImage is null
                Glide.with(itemView.context)
                    .load(R.drawable.ic_img) // Replace with your default image resource
                    .into(imageView)
            }
        }
    }
}