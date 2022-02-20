package com.androidfundamental.githubuserapp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androidfundamental.githubuserapp.ui.fragments.FollowersFragment
import com.androidfundamental.githubuserapp.ui.fragments.FollowingFragment

class DetailPagerAdapter(
    activity: AppCompatActivity,
    private val itemArray: Array<String>,
    private val username: Bundle
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> FollowersFragment()
        }
        fragment.arguments = username
        return fragment
    }

    override fun getItemCount(): Int = itemArray.size
}