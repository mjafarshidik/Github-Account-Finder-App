package com.androidfundamental.githubuserapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidfundamental.githubuserapp.databinding.ItemUserBinding
import com.androidfundamental.githubuserapp.data.model.UserResponse
import com.androidfundamental.githubuserapp.ui.activities.DetailActivity
import com.androidfundamental.githubuserapp.util.UserDiffCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MainAdapter :
    RecyclerView.Adapter<MainAdapter.UsersViewHolder>() {
    internal var listUser = ArrayList<UserResponse>()

    class UsersViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResponse) {
            with(binding) {
                Glide.with(itemView.context).load(user.avatarUrl)
                    .apply(RequestOptions.overrideOf(80, 80)).into(imgUser)
                tvUsername.text = user.username
            }

            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(itemView.context, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_DETAIL, user)
                            itemView.context.startActivity(intent)
                        }
                    }
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    fun setListUser(value: ArrayList<UserResponse>) {
        listUser.clear()
        val diffCallback = UserDiffCallback(
            this.listUser,
            value
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.listUser = value
        listUser.addAll(value)
    }
}

