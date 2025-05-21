package com.example.newsapp
import java.io.Serializable
data class Article(
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val sourceName: String?,
    val publishedAt: String?,
    val author: String?
) : Serializable