package com.androidfundamental.githubuserapp.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidfundamental.githubuserapp.adapter.DetailAdapter
import com.androidfundamental.githubuserapp.databinding.FragmentFollowersBinding
import com.androidfundamental.githubuserapp.ui.activities.DetailActivity
import com.androidfundamental.githubuserapp.ui.viewmodels.FollowersViewModel

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(FollowersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            showLoading(true)
            followersViewModel.setDataFollowers(
                requireArguments().getString(DetailActivity.EXTRA_FRAGMENT).toString()
            )
        }
        recyclerViewAddData()
    }

    private fun showLoading(state: Boolean) {
        binding.followersProgressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun recyclerViewAddData() {
        with(binding) {
            rvFollowers.layoutManager = LinearLayoutManager(context)
            val adapter = DetailAdapter()
            rvFollowers.adapter = adapter

            followersViewModel.getDataFollowers().observe(viewLifecycleOwner) {
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