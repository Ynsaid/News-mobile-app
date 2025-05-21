package com.example.newsapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope

import com.example.newsapp.databinding.ActivityHomePageBinding
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class HomePageActivity : AppCompatActivity() {

    private lateinit var newsList: RecyclerView
    private val apiKey = BuildConfig.API_KEY
    private val client = OkHttpClient()
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        newsList = findViewById(R.id.news_list)
        newsList.layoutManager = LinearLayoutManager(this)
        // Set click listener for search button



            binding.searchBtn.setOnClickListener {
                val query = binding.searchBar.text.toString().trim()
                if (query.isNotEmpty()) {
                    fetchNews(query)
                } else {
                    Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
                }
            }

             // or use the last used query

        fetchNews("ai")
       // Load default data
        // Use onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this) {
            finishAffinity()
        }
    }


    private fun fetchNews(query: String) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val fromDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

        val url = "https://newsapi.org/v2/everything?q=$query&from=$fromDate&sortBy=publishedAt&apiKey=$apiKey"

        lifecycleScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    runGETRequest(url)
                }

                if (result != null) {
                    val articles = parseArticles(result)
                    val adapter = NewsAdapter(articles)
                    newsList.adapter = adapter
                } else {
                    Toast.makeText(this@HomePageActivity, "Failed to fetch news", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@HomePageActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun runGETRequest(url: String): JSONObject? {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")
            val body = response.body?.string() ?: throw Exception("Empty response")
            return JSONObject(body)
        }
    }
    private fun parseArticles(response: JSONObject): List<Article> {
        val articles = mutableListOf<Article>()
        val array = response.getJSONArray("articles")

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            val title = obj.getString("title")
            val description = obj.optString("description")
            val urlToImage = obj.optString("urlToImage") // Get optional image URL
            val publishedAt = obj.optString("publishedAt")
            val sourceObj = obj.getJSONObject("source")
            val sourceName = sourceObj.optString("name")
            val author = obj.optString("author")

            // Validate urlToImage
            val validImageUrl = if (urlToImage.isNullOrEmpty() || !isValidUrl(urlToImage)) {
                null // Use null for invalid URLs
            } else {
                urlToImage
            }

            articles.add(
                Article(title, description, validImageUrl, sourceName, publishedAt, author)
            )
        }

        return articles
    }
    // Helper function to validate URLs
    private fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }
}