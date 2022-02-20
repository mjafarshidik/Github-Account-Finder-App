package com.androidfundamental.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidfundamental.githubuserapp.data.model.UserResponse
import com.androidfundamental.githubuserapp.databinding.ItemUserBinding
import com.androidfundamental.githubuserapp.util.UserDiffCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var detailUser = ArrayList<UserResponse>()

    class DetailViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResponse) {
            with(binding) {
                Glide.with(itemView.context).load(user.avatarUrl)
                    .apply(RequestOptions().override(80, 80)).into(imgUser)
                tvUsername.text = user.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(detailUser[position])
    }

    override fun getItemCount(): Int = detailUser.size

    internal fun setDetailUser(value: ArrayList<UserResponse>) {
        detailUser.clear()
        val diffCallback = UserDiffCallback(
            this.detailUser,
            value
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        this.detailUser = value
        detailUser.addAll(value)
    }
}