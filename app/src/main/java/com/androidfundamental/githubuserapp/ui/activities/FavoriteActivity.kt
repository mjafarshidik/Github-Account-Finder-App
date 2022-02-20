package com.androidfundamental.githubuserapp.ui.activities

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidfundamental.githubuserapp.R
import com.androidfundamental.githubuserapp.adapter.MainAdapter
import com.androidfundamental.githubuserapp.data.model.UserResponse
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.CONTENT_URI
import com.androidfundamental.githubuserapp.databinding.ActivityFavoriteBinding
import com.androidfundamental.githubuserapp.data.helper.MappingHelper
import kotlinx.coroutines.*


class FavoriteActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserData()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadData()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) loadData() else savedInstanceState.getParcelableArrayList<UserResponse>(
            EXTRA_LIST_USER
        )?.also { adapter.listUser = it }
    }


    @DelicateCoroutinesApi
    private fun loadData() {
        with(binding) {
            GlobalScope.launch(Dispatchers.Main) {
                val users = async(Dispatchers.IO) {
                    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                val listUsers = users.await()
                if (listUsers.size == 0) {
                    showImageVisibility(true)
                    tvNotFound.text = getString(R.string.not_found)
                    adapter.listUser = listUsers
                    showLoading(false)
                } else {
                    showImageVisibility(false)
                    adapter.listUser = listUsers
                    showLoading(false)
                }
            }
        }
    }

    private fun showImageVisibility(state: Boolean) {
        if (state) {
            binding.imgNotFound.visibility = View.VISIBLE
            binding.tvNotFound.visibility = View.VISIBLE
        } else {
            binding.imgNotFound.visibility = View.GONE
            binding.tvNotFound.visibility = View.GONE
        }
    }

    private fun getUserData() {
        with(binding) {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = MainAdapter()
            rvFavorite.adapter = adapter
            showLoading(true)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.favoriteProgressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_LIST_USER, adapter.listUser)
    }

    companion object {
        private const val EXTRA_LIST_USER = "extra_list_user"
    }
}