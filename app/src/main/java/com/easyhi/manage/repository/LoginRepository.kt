package com.easyhi.manage.repository

import android.util.Log
import androidx.datastore.core.DataStore
import com.easyhi.manage.data.network.AuthService
import com.easyhi.manage.data.network.AuthTokenResult
import com.easyhi.manage.serialize.AuthTokenP
import com.easyhi.manage.util.GITHUB_CLIENT_ID
import com.easyhi.manage.util.GITHUB_CLIENT_SECRET
import com.easyhi.manage.util.GITHUB_REDIRECT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val TAG = "LoginRepository"


class LoginRepository @Inject constructor(
    private val authService: AuthService,
    private val authTokenDataStore: DataStore<AuthTokenP>
) {

    suspend fun getAuthToken(code: String): AuthTokenResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val token = authService.getAuthToken(
                    GITHUB_CLIENT_ID,
                    GITHUB_CLIENT_SECRET,
                    code,
                    GITHUB_REDIRECT_URI
                )
                Log.d(TAG, "token = $token")
                authTokenDataStore.updateData { old ->
                    old.toBuilder()
                        .setTokenType(token.tokenType)
                        .setAccessToken(token.accessToken)
                        .addAllScopes(token.scope.split(",").toList())
                        .build()
                }
                AuthTokenResult(token = token)
            } catch (e: Exception) {
                Log.d(TAG, "getAuthToken 遇到错误 = $e")
                e.printStackTrace()
                AuthTokenResult(errorMessage = "$e")
            }
        }
    }

}