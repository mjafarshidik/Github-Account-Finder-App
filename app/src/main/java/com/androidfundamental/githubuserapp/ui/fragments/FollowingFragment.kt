package com.androidfundamental.githubuserapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidfundamental.githubuserapp.adapter.DetailAdapter
import com.androidfundamental.githubuserapp.databinding.FragmentFollowingBinding
import com.androidfundamental.githubuserapp.ui.activities.DetailActivity
import com.androidfundamental.githubuserapp.ui.viewmodels.FollowingViewModel

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(FollowingViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            showLoading(true)
            followingViewModel.setDataFollowing(
                requireArguments().getString(DetailActivity.EXTRA_FRAGMENT).toString()
            )
        }
        recyclerViewAddData()
    }

    private fun showLoading(state: Boolean) {
        binding.followingProgressBar.visibility = if(state) View.VISIBLE else View.GONE
    }

    private fun recyclerViewAddData() {
        with(binding) {
            rvFollowing.layoutManager = LinearLayoutManager(context)
            val adapter = DetailAdapter()
            rvFollowing.adapter = adapter

            followingViewModel.getDataFollowing().observe(viewLifecycleOwner) {
                it?.let {
                    if (it.size == 0) {
                        showImageVisibility(true)
                        showLoading(false)
                    } else {
                        adapter.setDetailUser(it)
                        showImageVisibility(false)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showImageVisibility(state: Boolean) {
        if (state) {
            binding.imgNotFound.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.VISIBLE
        } else {
            binding.imgNotFound.visibility = View.GONE
            binding.tvNotFound.visibility = View.GONE
        }
    }
}