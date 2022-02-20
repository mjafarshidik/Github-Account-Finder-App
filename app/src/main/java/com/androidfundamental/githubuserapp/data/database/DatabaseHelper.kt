package com.androidfundamental.githubuserapp.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.AVATAR_URL
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.USERNAME

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_favorite"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_FAVORITE =
            "CREATE TABLE ${DatabaseContract.TABLE_NAME} " +
                    " (${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    " $USERNAME TEXT NOT NULL," +
                    " $AVATAR_URL TEXT NOT NULL);"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.TABLE_NAME}")
        onCreate(db)
    }
}