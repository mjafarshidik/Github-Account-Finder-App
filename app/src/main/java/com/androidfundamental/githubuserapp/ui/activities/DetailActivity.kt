package com.androidfundamental.githubuserapp.ui.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.lifecycle.ViewModelProvider
import com.androidfundamental.githubuserapp.R
import com.androidfundamental.githubuserapp.adapter.DetailPagerAdapter
import com.androidfundamental.githubuserapp.databinding.ActivityDetailBinding
import com.androidfundamental.githubuserapp.data.model.UserResponse
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.AVATAR_URL
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.CONTENT_URI
import com.androidfundamental.githubuserapp.data.database.DatabaseContract.Companion.USERNAME
import com.androidfundamental.githubuserapp.data.helper.MappingHelper
import com.androidfundamental.githubuserapp.ui.viewmodels.DetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var uriWithId: Uri
    private var userState: Boolean = false
    private lateinit var userDetail: UserResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                DetailViewModel::class.java
            )
        showLoadingVisibility(true)
        setDetailUserData()
        addDetailUserData()

        uriWithId = Uri.parse("$CONTENT_URI/${userDetail.id}")
        checkUser(uriWithId)
        binding.addFavorite.setOnClickListener { insertToFavorite() }
    }

    private fun checkUser(uri: Uri) {
        val userFind = contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )

        val getUser = MappingHelper.mapCursorToArrayList(userFind)
        if (getUser.size > 0) {
            userState = false
            setStateFab(true)
        } else {
            userState = true
            setStateFab(false)
        }
    }

    private fun addDetailUserData() {
        with(binding) {
            detailViewModel.getDetailData().observe(this@DetailActivity) {
                it?.let {
                    showLoadingVisibility(false)
                    tvDetailName.text = it.username
                    textDetailUsername.text = it.login
                    userInfo.tvLocation.text = if (it.location == "null") {
                        getString(R.string.not_found)
                    } else it.location
                    userInfo.tvCompany.text = if (it.company == "null") {
                        getString(R.string.not_found)
                    } else it.company
                    userAmount.run {
                        tvRepositoryDetail.text = it.repository.toString()
                        tvFollowerDetail.text = it.followers.toString()
                        tvFollowingDetail.text = it.following.toString()
                    }

                    Glide.with(this@DetailActivity).load(it.avatarUrl).into(imgDetailUser)
                }

            }
        }
    }

    private fun setDetailUserData() {
        val tabTitle = resources.getStringArray(R.array.tab_title)
        userDetail = intent.getParcelableExtra<UserResponse>(EXTRA_DETAIL) as UserResponse

        userDetail.username.let {
            val usernameLogin = Bundle()
            usernameLogin.putString(EXTRA_FRAGMENT, userDetail.username)
            detailViewModel.setDetailData(it)
            binding.vpDetail.adapter =
                DetailPagerAdapter(this@DetailActivity, tabTitle, usernameLogin)
        }
        TabLayoutMediator(binding.tabLayoutDetail, binding.vpDetail) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
    }

    private fun insertToFavorite() {
        Log.d(TAG, "insertFavorite: ${userDetail.id} & $CONTENT_URI")
        if (userState) {
            Log.d(TAG, "!userState: ${userDetail.id} & $CONTENT_URI")
            val contentValues = contentValuesOf(
                BaseColumns._ID to userDetail.id,
                USERNAME to userDetail.username,
                AVATAR_URL to userDetail.avatarUrl
            )
            contentResolver.insert(CONTENT_URI, contentValues)
            userState = false
            setStateFab(true)
            Toast.makeText(this, getString(R.string.add_to_favorite), Toast.LENGTH_LONG).show()
        } else {
            Log.d(TAG, "else: ${userDetail.id} & $CONTENT_URI")
            contentResolver.delete(uriWithId, null, null)
            userState = true
            setStateFab(false)
            Toast.makeText(this, getString(R.string.remove_to_favorite), Toast.LENGTH_LONG).show()
        }
    }

    private fun showLoadingVisibility(state: Boolean) {
        if (!state) {
            binding.lytProgressBar.visibility = View.GONE
            binding.addFavorite.visibility = View.VISIBLE
        } else {
            binding.lytProgressBar.visibility = View.VISIBLE
            binding.addFavorite.visibility = View.INVISIBLE
        }
    }

    private fun setStateFab(state: Boolean) =
        if (state) binding.addFavorite.setImageResource(R.drawable.ic_favorite)
        else binding.addFavorite.setImageResource(R.drawable.ic_favorite_border)

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_FRAGMENT = "extra_fragment"
        private const val TAG = "DetailActivity"
    }
}

