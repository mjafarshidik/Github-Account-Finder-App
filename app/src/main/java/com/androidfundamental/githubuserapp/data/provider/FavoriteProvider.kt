package com.androidfundamental.githubuserapp.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.AUTHORITY
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.CONTENT_URI
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.TABLE_NAME
import com.androidfundamental.githubuserapp.data.database.FavoriteHelper


class FavoriteProvider : ContentProvider() {
    private lateinit var favoriteHelper: FavoriteHelper

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query: $uri")
        return when (myUriMather.match(uri)) {
            USERS -> favoriteHelper.queryAll()
            USER_ID -> {
                Log.d(TAG, "query: ${uri.lastPathSegment.toString()}")
                favoriteHelper.queryById(uri.lastPathSegment.toString())
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USERS) {
            myUriMather.match(uri) -> favoriteHelper.insert(values!!)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            myUriMather.match(uri) -> favoriteHelper.delete(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    companion object {
        private const val USERS = 1
        private const val USER_ID = 2
        private const val TAG = "UserFavoriteProvider"

        private val myUriMather = UriMatcher(UriMatcher.NO_MATCH)

        init {
            myUriMather.addURI(AUTHORITY, TABLE_NAME, USERS)
            myUriMather.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }
}