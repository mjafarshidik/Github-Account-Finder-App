package com.androidfundamental.githubuserapp.data.helper

import android.database.Cursor
import android.provider.BaseColumns
import com.androidfundamental.githubuserapp.data.model.UserResponse
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.AVATAR_URL
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.USERNAME

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<UserResponse> {
        val usersList = ArrayList<UserResponse>()

        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
                usersList.add(UserResponse(username, avatarUrl, id))
            }
        }
        return usersList
    }

}