package com.cpp.inonews.ui.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.cpp.inonews.R
import com.cpp.inonews.data.local.entity.ArticleEntity
import com.cpp.inonews.databinding.FragmentHomeBinding
import com.cpp.inonews.ui.MainViewModel
import com.cpp.inonews.ui.adapter.NewsAdapter
import com.cpp.inonews.utils.helper.viewmodel.ObtainViewModelFactory
import com.cpp.inonews.data.remote.Result
import com.cpp.inonews.data.remote.responses.topheadlines.ArticlesItem
import com.cpp.inonews.ui.adapter.LoadingStateAdapter
import com.cpp.inonews.utils.helper.asResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private val newsAdapter = NewsAdapter { item ->
        showSelectedStory(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        setUpViewModel()
        getNews()
//        observeAdapterState()
    }

    private fun getNews() {
        viewModel.getAllNews().observe(viewLifecycleOwner) { data ->
            newsAdapter.submitData(lifecycle, data)
        }
    }

    private fun setUpViewModel() {
        viewModel = ObtainViewModelFactory.obtain(requireActivity())
    }

    private fun setUpRecyclerView() {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    newsAdapter.retry()
                }
            )
        }
    }

    private fun showSelectedStory(item: ArticleEntity) {
        val bundle = Bundle().apply {
            putParcelable("article", item)
        }
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
    }

    private fun observeAdapterState() {
        viewLifecycleOwner.lifecycleScope.launch {
            newsAdapter.loadStateFlow.collectLatest { loadState ->
                when (val result = loadState.refresh.asResult()) {
                    is Result.Loading -> {
                        binding.mainProgressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.mainProgressBar.visibility = View.GONE

                        // cek kalau list kosong
                        val isListEmpty = newsAdapter.itemCount == 0
                        binding.tvEmptyState.visibility = if (isListEmpty) View.VISIBLE else View.GONE
                        binding.rvMain.visibility = if (isListEmpty) View.GONE else View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.mainProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}