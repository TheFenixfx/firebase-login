package com.example.logintest.data

import com.example.logintest.data.model.LoggedInUser
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

     suspend fun login(username: String, password: String): Result<LoggedInUser> {

             val result = dataSource.loginAsync(username, password)?:Result.Error(Exception(("login second level")))
             // Using setLoggedInUser here
             if (result is Result.Success) {
                 setLoggedInUser(result.data)
                 return result
             } else {
                 return result
             }
     }



    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
