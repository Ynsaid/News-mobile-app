package com.example.newsapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.newsapp.R
import com.example.newsapp.classes.Article
import com.example.newsapp.ui.theme.RedPrimary



@Composable
fun NewsItem(
    article: Article,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color(0xFF111827)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            NewsImage(
                imageUrl = article.imageUrl,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                NewsTitle(title = article.title)

                NewsDescription(description = article.description)

                NewsMeta(
                    sourceName = article.sourceName,
                    publishedAt = article.publishedAt
                )

                NewsAuthor(author = article.author)
            }
        }
    }
}

@Composable
private fun NewsImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE5E7EB))
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            placeholder = painterResource(R.drawable.ic_img),
            error = painterResource(R.drawable.ic_img)
        )
    }
}

@Composable
private fun NewsTitle(
    title: String
) {
    Text(
        text = title,
        color = Color(0xFF111827),
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun NewsDescription(
    description: String
) {
    Text(
        text = description,
        color = Color(0xFF6B7280),
        fontSize = 12.sp,
        lineHeight = 18.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Left,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun NewsMeta(
    sourceName: String,
    publishedAt: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sourceName,
            color = RedPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "•",
            color = Color(0xFF6B7280),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = publishedAt,
            color = Color(0xFF6B7280),
            fontSize = 12.sp,
            textAlign = TextAlign.Left
        )
    }
}

@Composable
private fun NewsAuthor(
    author: String
) {
    if (author.isBlank()) {
        return
    }

    Text(
        text = "By $author",
        color = Color(0xFF9CA3AF),
        fontSize = 11.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Left
    )
}
