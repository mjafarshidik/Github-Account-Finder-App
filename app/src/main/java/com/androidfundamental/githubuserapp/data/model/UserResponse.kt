package com.androidfundamental.githubuserapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserResponse(
    val username: String,
    val avatarUrl: String,
    val id :Int
) : Parcelable