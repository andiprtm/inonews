package com.cpp.inonews

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem
import com.cpp.inonews.databinding.ActivityMainBinding
import com.cpp.inonews.ui.MainViewModel
import com.cpp.inonews.ui.adapter.NewsAdapter
import com.cpp.inonews.utils.helper.viewmodel.ObtainViewModelFactory
import com.cpp.inonews.data.remote.Result
import com.cpp.inonews.ui.adapter.LoadingStateAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val newsAdapter = NewsAdapter { item ->
        showSelectedStory(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUpRecycleView()
        setUpViewModel()
        getNews()

    }

    private fun getNews() {
        viewModel.getAllNews().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.mainProgressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.mainProgressBar.visibility = View.GONE
                    newsAdapter.submitData(lifecycle, result.data) // pagingData disubmit ke adapter
                }
                is Result.Error -> {
                    binding.mainProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpViewModel() {
        viewModel = ObtainViewModelFactory.obtain(this)
    }

    private fun setUpRecycleView() {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    newsAdapter.retry()
                }
            )
        }
    }

    private fun showSelectedStory(item: ArticlesItem) {
        Toast.makeText(this, "Article ${item.title}", Toast.LENGTH_LONG).show()
    }
}
