package com.example.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityItemBinding

class ItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get article from intent
        val article = intent.getSerializableExtra("article") as? Article

        article?.let {
            binding.titleOfnews.text = it.title
            binding.contentOfnews.text = it.description

            Glide.with(this)
                .load(it.urlToImage)
                .placeholder(R.drawable.ic_img)
                .into(binding.imgOfitem)
        }

        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}