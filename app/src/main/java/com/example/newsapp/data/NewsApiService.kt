package com.example.newsapp.data

import com.example.newsapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class NewsApiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    fun fetchNews(
        query: String,
        fromDate: String
    ): JSONObject {
        val url = buildNewsUrl(
            query = query,
            fromDate = fromDate
        )

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException(
                    "Unexpected response code: ${response.code}"
                )
            }

            val body = response.body?.string()
                ?: throw IOException("Empty response body")

            return JSONObject(body)
        }
    }

    private fun buildNewsUrl(
        query: String,
        fromDate: String
    ): String {
        val encodedQuery = java.net.URLEncoder.encode(
            query,
            "UTF-8"
        )

        return "https://newsapi.org/v2/everything" +
                "?q=$encodedQuery" +
                "&from=$fromDate" +
                "&sortBy=publishedAt" +
                "&apiKey=${BuildConfig.API_KEY}"
    }
}