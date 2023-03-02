package com.easyhi.manage.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easyhi.manage.R
import com.easyhi.manage.databinding.FragmentRepoBinding
import com.easyhi.manage.ui.adapter.RepoPagingAdapter
import com.easyhi.manage.ui.viewmodel.RepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoFragment : Fragment() {

    private var _binding: FragmentRepoBinding? = null
    private val binding: FragmentRepoBinding
        get() = _binding!!

    private val viewModel by viewModels<RepoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcRepos.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = RecyclerView.VERTICAL
        }

        val adapter = RepoPagingAdapter()
        binding.rcRepos.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.getRepoPagingData("netty").collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

        adapter.addLoadStateListener { status ->
            Toast.makeText(requireContext(), "$status", Toast.LENGTH_SHORT).show()
        }

        binding.swipeMain.setOnRefreshListener {
            binding.rcRepos.swapAdapter(adapter, true)
            adapter.refresh()
        }


//        viewModel.getRepoPagingData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}