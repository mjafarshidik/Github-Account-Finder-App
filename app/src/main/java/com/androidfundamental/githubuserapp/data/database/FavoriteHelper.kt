package com.androidfundamental.githubuserapp.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.TABLE_NAME
import java.sql.SQLException

class FavoriteHelper(context: Context) {

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        mySqlLiteDatabase = databaseHelper.writableDatabase
    }

    fun queryAll(): Cursor =
        mySqlLiteDatabase.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} ASC"
        )

    fun queryById(idUser: String): Cursor =
        mySqlLiteDatabase.query(
            DATABASE_TABLE,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(idUser),
            null,
            null,
            null,
            null
        )

    fun insert(values: ContentValues): Long =
        mySqlLiteDatabase.insert(DATABASE_TABLE, null, values)

    fun delete(id: String): Int =
        mySqlLiteDatabase.delete(DATABASE_TABLE, "${BaseColumns._ID} = ?", arrayOf(id))

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }

        private lateinit var mySqlLiteDatabase: SQLiteDatabase
    }
}