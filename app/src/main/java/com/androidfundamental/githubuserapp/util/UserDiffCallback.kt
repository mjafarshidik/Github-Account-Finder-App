package com.androidfundamental.githubuserapp.util

import androidx.recyclerview.widget.DiffUtil
import com.androidfundamental.githubuserapp.data.model.UserResponse

open class UserDiffCallback(
    private val oldListUser: ArrayList<UserResponse>,
    private val newListUser: ArrayList<UserResponse>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldListUser.size
    }

    override fun getNewListSize(): Int {
        return newListUser.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldListUser[oldItemPosition] == newListUser[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldListUser[oldItemPosition] == newListUser[newItemPosition]
    }
}