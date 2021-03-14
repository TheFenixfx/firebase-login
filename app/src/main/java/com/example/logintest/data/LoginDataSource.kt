package com.example.logintest.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.model.LoggedInUser
import com.example.logintest.ui.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.Response
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.lang.Exception
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

     private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun loginAsync(username: String, password: String): Result<LoggedInUser>? {
        var repository = AuthenticationDataRepository(mAuth)
        var success = Result.Success( LoggedInUser(UUID.randomUUID().toString(), "Default success")  )
        var fail = Result.Error(Exception(("Default fail")))
        var path  = false
        try {
                     repository.authenticate(username, password)?.let {
                         success = Result.Success(
                                 LoggedInUser(
                                     it.uid, it.email.toString()
                                 )
                             )
                         path = true

                     } ?: run {
                             fail = Result.Error(Exception(("Denied")))
                             path = false
                     }

                 } catch (e: FirebaseAuthException) {
                         fail = Result.Error(Exception(("catch error")))
                         path = false
        }

        if (path){return success}else{return fail}
    }


  fun logout() {

      // TODO: revoke authentication
  }

}

