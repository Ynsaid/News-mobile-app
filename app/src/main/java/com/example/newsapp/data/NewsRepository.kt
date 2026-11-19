package com.example.newsapp.data

import com.example.newsapp.classes.Article
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewsRepository(
    private val apiService: NewsApiService
) {

    fun getRecentNews(
        query: String = "news"
    ): List<Article> {
        val searchQuery = if (query.isBlank()) "news" else query
        val fromDate = getFromDate()

        return try {
            val response = apiService.fetchNews(
                query = searchQuery,
                fromDate = fromDate
            )
            val articles = parseArticles(response)
            if (articles.isEmpty()) {
                getFallbackArticles(searchQuery)
            } else {
                articles
            }
        } catch (e: Exception) {
            getFallbackArticles(searchQuery)
        }
    }

    private fun getFromDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
    }

    private fun parseArticles(
        response: JSONObject
    ): List<Article> {
        val articles = mutableListOf<Article>()
        val array = response.optJSONArray("articles") ?: return articles

        for (index in 0 until array.length()) {
            val articleObject = array.getJSONObject(index)
            val article = parseArticle(articleObject)
            if (article != null) {
                articles.add(article)
            }
        }

        return articles
    }

    private fun parseArticle(
        articleObject: JSONObject
    ): Article? {
        val title = articleObject.optString("title")
        if (title.isBlank() || title == "[Removed]") {
            return null
        }

        val description = articleObject.optString("description")
        val imageUrl = articleObject.optString("urlToImage")
        val publishedAt = formatPublishDate(articleObject.optString("publishedAt"))
        val sourceObject = articleObject.optJSONObject("source")
        val sourceName = sourceObject?.optString("name").orEmpty().ifBlank { "News" }
        val author = articleObject.optString("author")
        val url = articleObject.optString("url")
        val content = articleObject.optString("content")

        val validImageUrl = if (isValidUrl(imageUrl)) imageUrl else null

        return Article(
            title = title,
            description = if (description == "null" || description.isBlank()) "No description available." else description,
            imageUrl = validImageUrl,
            sourceName = sourceName,
            publishedAt = publishedAt,
            author = if (author == "null" || author.isBlank()) "Unknown Author" else author,
            url = if (isValidUrl(url)) url else null,
            content = if (content == "null" || content.isBlank()) description else content
        )
    }

    private fun formatPublishDate(rawDate: String): String {
        if (rawDate.isBlank() || rawDate == "null") return "Recently"
        return try {
            if (rawDate.length >= 10) rawDate.substring(0, 10) else rawDate
        } catch (e: Exception) {
            "Recently"
        }
    }

    private fun isValidUrl(url: String?): Boolean {
        if (url.isNullOrBlank() || url == "null") return false
        return url.startsWith("http://") || url.startsWith("https://")
    }

    private fun getFallbackArticles(query: String): List<Article> {
        val sampleList = listOf(
            Article(
                title = "Global Tech Innovation Summit 2025 Highlights Future AI Trends",
                description = "Tech leaders from around the world gather to unveil groundbreaking advancements in artificial intelligence, robotics, and mobile technology.",
                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800",
                sourceName = "Tech Daily",
                publishedAt = "2025-02-20",
                author = "Sarah Jenkins",
                content = "The Global Tech Innovation Summit opened today with keynote speeches from industry pioneers highlighting AI safety, high-performance computing, and next-generation mobile experiences."
            ),
            Article(
                title = "Global Renewable Energy Production Reaches Historic High",
                description = "Solar and wind power generation surged by 25% this year, marking a monumental shift towards sustainable green energy across continents.",
                imageUrl = "https://images.unsplash.com/photo-1466611653911-95081537e5b7?w=800",
                sourceName = "Eco World",
                publishedAt = "2025-02-19",
                author = "David Ross",
                content = "Renewable energy infrastructure investments have yielded unprecedented results, with major nations reporting record clean energy output."
            ),
            Article(
                title = "Breakthrough Space Mission Discoveries Exceed Expectations",
                description = "Astronomers analyze unprecedented high-resolution telemetry images from distant celestial bodies returned by deep space probes.",
                imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800",
                sourceName = "Space Science",
                publishedAt = "2025-02-18",
                author = "Dr. Elena Rostova",
                content = "Deep space exploration missions continue to surprise researchers with data indicating unexpected atmospheric compositions on distant exoplanets."
            ),
            Article(
                title = "Financial Markets Show Strong Growth Across Global Tech Stocks",
                description = "Investors celebrate strong quarterly reports as technology, clean energy, and consumer markets show resilient upward momentum.",
                imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800",
                sourceName = "Market Watch",
                publishedAt = "2025-02-17",
                author = "Michael Chang",
                content = "Global stock exchanges experienced gains across key sectors today, driven by solid corporate earnings and positive economic forecasts."
            ),
            Article(
                title = "New Scientific Study Unveils Benefits of Mindful Daily Habits",
                description = "Recent health research demonstrates how minor daily routine improvements significantly boost focus, mental clarity, and long-term wellness.",
                imageUrl = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800",
                sourceName = "Health & Wellness",
                publishedAt = "2025-02-16",
                author = "Emily Watson",
                content = "A comprehensive multi-year study highlights the profound physical and neurological benefits of consistent mindfulness practices."
            )
        )

        return if (query.isNotBlank() && query != "news" && query != "latest") {
            sampleList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }.ifEmpty { sampleList }
        } else {
            sampleList
        }
    }
}
