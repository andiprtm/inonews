package com.cpp.inonews.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.cpp.inonews.R
import com.cpp.inonews.databinding.FragmentDetailBinding
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem
import java.text.SimpleDateFormat
import java.util.Locale

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var article: ArticlesItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArticleFromBundle()
        populateArticleData()
        setupReadFullArticleButton()
    }

    private fun getArticleFromBundle() {
        arguments?.let { bundle ->
            article = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("article", ArticlesItem::class.java) ?: ArticlesItem()
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable("article") ?: ArticlesItem()
            }
        }
    }

    private fun setupReadFullArticleButton() {
        binding.btnReadFullArticle.setOnClickListener {
            navigateToWebView()
        }
    }

    private fun navigateToWebView() {
        article.url?.let { url ->
            if (url.isNotEmpty()) {
                val bundle = Bundle().apply {
                    putString("article_url", url)
                }
                findNavController().navigate(R.id.action_detailFragment_to_fullArticleFragment, bundle)
            }
        }
    }

    private fun populateArticleData() {
        with(binding) {
            title.text = article.title
            author.text = article.author ?: "Unknown Author"

            // Format and display the published date
            val publishedDate = formatPublishedDate(article.publishedAt)
            publish.text = publishedDate

            // Display the article description
            desc.text = article.description ?: article.content ?: "No description available"

            // Load image with Glide
            article.urlToImage?.let { imageUrl ->
                Glide.with(this@DetailFragment)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_broken_image)
                    .into(image)
            }
        }
    }

    private fun formatPublishedDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Unknown Date"

        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            return date?.let { outputFormat.format(it) } ?: "Unknown Date"
        } catch (e: Exception) {
            return dateString
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}