package com.androidfundamental.githubuserapp.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidfundamental.githubuserapp.data.model.UserResponse
import com.androidfundamental.githubuserapp.data.token.Token
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersViewModel(application: Application) : AndroidViewModel(application) {
    val listUserFollowers = MutableLiveData<ArrayList<UserResponse>>()

    internal fun getDataFollowers(): LiveData<ArrayList<UserResponse>> = listUserFollowers

    internal fun setDataFollowers(userLogin: String) {
        AsyncHttpClient().apply { addHeader("User-Agent", "request") }
            .apply { addHeader("Authorization", Token.TOKEN_GITHUB_API) }
            .get(
                " https://api.github.com/users/$userLogin/followers",
                object : AsyncHttpResponseHandler() {
                    override fun onSuccess(
                        statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?
                    ) {
                        try {
                            val listUser = ArrayList<UserResponse>()
                            JSONArray(String(responseBody!!)).run {
                                for (i in 0 until this.length()) {
                                    this.getJSONObject(i).run {
                                        listUser.add(
                                            UserResponse(
                                                this.getString("login"),
                                                this.getString("avatar_url"),
                                                this.getInt("id")
                                            )
                                        )
                                    }
                                }
                            }
                            listUserFollowers.postValue(listUser)
                        } catch (e: Exception) {
                            Toast.makeText(
                                getApplication(),
                                e.printStackTrace().toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(
                        statusCode: Int,
                        headers: Array<out Header>?,
                        responseBody: ByteArray?,
                        error: Throwable?
                    ) {
                        val errorMessage = when (statusCode) {
                            401 -> "$statusCode : Bad Request"
                            403 -> "$statusCode : Forbidden"
                            404 -> "$statusCode : Not Found"
                            else -> "$statusCode : ${error?.message}"
                        }
                        Toast.makeText(
                            getApplication(),
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
    }
}