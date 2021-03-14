package com.example.logintest.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.logintest.data.LoginRepository
import com.example.logintest.data.Result
import com.google.firebase.auth.FirebaseAuth
import com.example.logintest.R
import com.example.logintest.data.model.LoggedInUser
import com.google.android.gms.common.api.Response
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult


     fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        println("antes de lanzarse")

         viewModelScope.launch {
             println("inside viewmodel scope launch")

             var result = loginRepository.login(username, password)

             if (result is Result.Success) {
                 println("exito")

                 _loginResult.value =
                     LoginResult(success = LoggedInUserView(displayName = result.data.displayName))

             } else if (result is Result.Error) {
                 println("fallo")
                 _loginResult.value = LoginResult(error = R.string.login_failed)
             }
             println("saliendo de funcion")
         }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
