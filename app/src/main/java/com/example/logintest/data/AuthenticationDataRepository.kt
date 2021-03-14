package com.example.logintest.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await


/* Class possible with kotlinx.coroutines.tasks.await*/

class AuthenticationDataRepository constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun authenticate(        email: String,        password: String    ): FirebaseUser? {
        firebaseAuth.signInWithEmailAndPassword(email, password).await() /*this is a good abstraction*/
        return firebaseAuth.currentUser ?:        throw FirebaseAuthException("", "")
    }
}