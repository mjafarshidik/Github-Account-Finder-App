package com.androidfundamental.githubuserapp.data.model

data class DetailUserResponse(
    val login: String,
    val username: String,
    val location: String,
    val company: String,
    val avatarUrl: String,
    val repository: Int,
    val followers: Int,
    val following: Int
)
