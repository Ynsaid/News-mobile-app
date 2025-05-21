package com.example.newsapp

data class NewsItem(
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val author: String?,
    val source: Source?
)

data class Source(
    val name: String?
)