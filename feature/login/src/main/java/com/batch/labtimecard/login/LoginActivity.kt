package com.batch.labtimecard.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.batch.labtimecard.common.navigator.Navigator
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseAuth.getInstance().currentUser?.let {
            navigator.run { navigateToMember() }
            finish()
            return
        }

        loginWithGoogle()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Timber.d(FirebaseAuth.getInstance().currentUser?.uid)
                navigator.run { navigateToMember() }
                finish()
            } else {
                loginWithGoogle()
                Timber.e(response?.error)
            }
        }
    }

    private fun loginWithGoogle() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    companion object {
        private const val RC_SIGN_IN = 1
    }
}
