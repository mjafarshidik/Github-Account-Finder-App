package com.androidfundamental.githubuserapp.ui.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidfundamental.githubuserapp.data.model.DetailUserResponse
import com.androidfundamental.githubuserapp.data.token.Token
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    val userDetail = MutableLiveData<DetailUserResponse>()

    internal fun getDetailData(): LiveData<DetailUserResponse> = userDetail

    internal fun setDetailData(username: String) {
        AsyncHttpClient().apply { addHeader("User-Agent", "request") }
            .apply { addHeader("Authorization", Token.TOKEN_GITHUB_API) }
            .get(" https://api.github.com/users/$username", object :
                AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    try {
                        JSONObject(String(responseBody!!)).run {
                            userDetail.postValue(
                                DetailUserResponse(
                                    this.getString("name"),
                                    this.getString("login"),
                                    this.getString("location"),
                                    this.getString("company"),
                                    this.getString("avatar_url"),
                                    this.getInt("public_repos"),
                                    this.getInt("followers"),
                                    this.getInt("following")
                                )
                            )
                        }
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
                    Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}