package com.example.newsapp.classes

import java.io.Serializable

data class Article(
    val title: String,
    val description: String,
    val imageUrl: String?,
    val sourceName: String,
    val publishedAt: String,
    val author: String,
    val url: String? = null,
    val content: String? = null
) : Serializable
