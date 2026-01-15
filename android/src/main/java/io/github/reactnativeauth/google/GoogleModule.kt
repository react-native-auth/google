package io.github.reactnativeauth.google

import android.app.Activity
import android.content.Intent
import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.*
import kotlinx.coroutines.*

@ReactModule(name = GoogleModule.NAME)
class GoogleModule(
    reactContext: ReactApplicationContext,
) : ReactContextBaseJavaModule(reactContext),
    ActivityEventListener {
    private var legacySignInPromise: Promise? = null
    private var googleSignInClient: GoogleSignInClient? = null

    init {
        reactContext.addActivityEventListener(this)
    }

    override fun getName(): String = NAME

    @ReactMethod
    fun oneTap(
        options: ReadableMap,
        promise: Promise,
    ) {
        signInInternal(
            options,
            filterByAuthorizedAccounts = true,
            promise,
        )
    }

    @ReactMethod
    fun signIn(
        options: ReadableMap,
        promise: Promise,
    ) {
        signInInternal(
            options,
            filterByAuthorizedAccounts = false,
            promise,
        )
    }

    @ReactMethod
    fun legacySignIn(
        options: ReadableMap,
        promise: Promise,
    ) {
        val currentActivity = getCurrentActivity()
        val activity: Activity =
            currentActivity
                ?: return promise.reject(
                    "NO_ACTIVITY",
                    "No current Activity",
                )

        val clientId =
            options.getString("clientId")
                ?: return promise.reject(
                    "NO_CLIENT_ID",
                    "Missing clientId",
                )

        if (legacySignInPromise != null) {
            promise.reject(
                "SIGN_IN_IN_PROGRESS",
                "Sign-in already in progress",
            )
            return
        }

        val builder =
            GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()

        val prompt = options.getString("prompt")
        if (!prompt.isNullOrEmpty()) {
            builder.requestProfile()
        }

        val gso = builder.build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
        legacySignInPromise = promise

        val signInIntent = googleSignInClient?.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @ReactMethod
    fun signOut(promise: Promise) {
        val currentActivity = getCurrentActivity()
        val activity: Activity =
            currentActivity
                ?: return promise.reject(
                    "NO_ACTIVITY",
                    "No current Activity",
                )

        try {
            val gso =
                GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .build()

            googleSignInClient = GoogleSignIn.getClient(activity, gso)

            googleSignInClient?.signOut()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    promise.resolve(null)
                } else {
                    promise.reject(
                        "SIGN_OUT_FAILED",
                        "Sign-out failed",
                        task.exception,
                    )
                }
            }
        } catch (e: Exception) {
            promise.reject("SIGN_OUT_ERROR", e.message, e)
        }
    }

    override fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleLegacySignInResult(task)
        }
    }

    override fun onNewIntent(intent: Intent) {
        // Not needed for Google Sign-In
    }

    private fun handleLegacySignInResult(completedTask: Task<GoogleSignInAccount>) {
        val promise = legacySignInPromise
        legacySignInPromise = null

        if (promise == null) {
            return
        }

        try {
            val account = completedTask.getResult(ApiException::class.java)
            val map = Arguments.createMap()
            map.putString("idToken", account.idToken)
            promise.resolve(map)
        } catch (e: ApiException) {
            promise.reject("SIGN_IN_FAILED", "Sign-in failed: ${e.statusCode}", e)
        }
    }

    private fun signInInternal(
        options: ReadableMap,
        filterByAuthorizedAccounts: Boolean,
        promise: Promise,
    ) {
        val currentActivity = getCurrentActivity()
        val activity: Activity =
            currentActivity
                ?: return promise.reject(
                    "NO_ACTIVITY",
                    "No current Activity",
                )

        val clientId =
            options.getString("clientId")
                ?: return promise.reject(
                    "NO_CLIENT_ID",
                    "Missing clientId",
                )

        val credentialManager =
            CredentialManager.create(activity)

        val googleOption =
            GetGoogleIdOption
                .Builder()
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(
                    filterByAuthorizedAccounts,
                ).build()

        val request =
            GetCredentialRequest
                .Builder()
                .addCredentialOption(googleOption)
                .build()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result =
                    credentialManager.getCredential(
                        context = activity,
                        request = request,
                    )
                handleGetCredentialSuccess(result, promise)
            } catch (e: GetCredentialException) {
                promise.reject("SIGN_IN_FAILED", e.message, e)
            } catch (e: Exception) {
                promise.reject("SIGN_IN_ERROR", e.message, e)
            }
        }
    }

    private fun handleGetCredentialSuccess(
        result: GetCredentialResponse,
        promise: Promise,
    ) {
        val credential = result.credential

        if (
            credential is CustomCredential &&
            credential.type ==
            GoogleIdTokenCredential
                .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleCred =
                GoogleIdTokenCredential.createFrom(
                    credential.data,
                )

            val map = Arguments.createMap()
            map.putString(
                "idToken",
                googleCred.idToken,
            )

            promise.resolve(map)
        } else {
            promise.reject(
                "INVALID_CREDENTIAL",
                "Not Google credential",
            )
        }
    }

    companion object {
        const val NAME = "Google"
        private const val RC_SIGN_IN = 9001
    }
}
