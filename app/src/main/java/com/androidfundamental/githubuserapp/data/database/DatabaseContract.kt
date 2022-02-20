package com.androidfundamental.githubuserapp.data.database

import android.net.Uri
import android.provider.BaseColumns

class DatabaseContract : BaseColumns {
    companion object {
        const val AUTHORITY = "com.androidfundamental.githubuserapp"
        private const val SCHEMA = "content"

        const val TABLE_NAME = "table_favorite"
        const val USERNAME = "username"
        const val AVATAR_URL = "avatar_url"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEMA)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
    }
}