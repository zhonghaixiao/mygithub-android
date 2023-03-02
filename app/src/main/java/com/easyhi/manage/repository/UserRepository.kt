package com.easyhi.manage.repository

import android.util.Log
import com.easyhi.manage.data.local.AppDatabase
import com.easyhi.manage.data.network.User
import com.easyhi.manage.data.network.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService,
    private val appDatabase: AppDatabase
) {

    private val TAG = UserRepository::class.simpleName

    fun getCurrentUser() = appDatabase.userDao().getUser()

    suspend fun getUserInfoFromRemote(){
        withContext(Dispatchers.IO) {
            val user = try{
                userService.findUserInfo()
            }catch (e: Exception) {
                Log.d(TAG, "getUserInfoFromRemote 遇到错误 e = $e")
                null
            }
            if (user != null) {
                appDatabase.userDao().insert(user)
            }
        }
    }

}