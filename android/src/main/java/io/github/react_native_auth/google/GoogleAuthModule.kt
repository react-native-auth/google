package io.github.react_native_auth.google

import android.app.Activity
import android.content.Intent

import androidx.credentials.*
import androidx.credentials.exceptions.GetCredentialException

import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap

import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task

import com.google.android.libraries.identity.googleid.*

import java.lang.reflect.Modifier
import kotlinx.coroutines.*

class GoogleAuthModule(private val reactContext: ReactApplicationContext) :
    NativeGoogleAuthSpec(reactContext), ActivityEventListener {

    private var legacySignInPromise: Promise? = null
    private var authorizationPromise: Promise? = null
    private var googleSignInClient: GoogleSignInClient? = null

    private val RC_SIGN_IN = 9001
    private val RC_AUTHORIZE = 9002
    
    init {
        reactContext.addActivityEventListener(this)
    }

    override fun oneTap(options: ReadableMap, promise: Promise) {
        signInInternal(options, filterByAuthorizedAccounts = true, promise)
    }

    override fun signIn(options: ReadableMap, promise: Promise) {
        signInInternal(options, filterByAuthorizedAccounts = false, promise)
    }

    override fun legacySignIn(options: ReadableMap, promise: Promise) {
        val activity: Activity = reactContext.currentActivity
            ?: return promise.reject("NO_ACTIVITY", "ERROR: NO ACTIVITY")

        val clientId = options.getString("clientId")
            ?: return promise.reject("NO_CLIENT_ID", "ERROR: NO CLIENT_ID PROVIDED")

        if (legacySignInPromise != null) {
            promise.reject("SIGN_IN_IN_PROGRESS", "ERROR: SIGN_IN ALREADY IN PROGRESS")
            return
        }

        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()

        val prompt = options.getString("prompt")
        if (!prompt.isNullOrEmpty()) {
            builder.requestProfile()
        }

        if (options.hasKey("scopes")) {
            val scopesArray = options.getArray("scopes")
            if (scopesArray != null && scopesArray.size() > 0) {
                val validScopes = mutableListOf<Scope>()
                for (i in 0 until scopesArray.size()) {
                    scopesArray.getString(i)?.let { scopeStr ->
                        validScopes.add(Scope(scopeStr))
                    }
                }

                if (validScopes.isNotEmpty()) {
                    val firstScope = validScopes[0]
                    val remainingScopes = validScopes.drop(1).toTypedArray()
                    builder.requestScopes(firstScope, *remainingScopes)
                }
            }
        }

        val gso = builder.build()
        val client = GoogleSignIn.getClient(activity, gso)
        googleSignInClient = client
        legacySignInPromise = promise

        activity.startActivityForResult(client.signInIntent, RC_SIGN_IN)
    }

    override fun requestScopes(scopesArray: ReadableArray, promise: Promise) {
        val activity: Activity = reactContext.currentActivity
            ?: return promise.reject("NO_ACTIVITY", "ERROR: NO ACTIVITY")

        if (authorizationPromise != null) {
            return promise.reject("AUTHORIZATION_IN_PROGRESS", "Authorization is already in progress")
        }

        val scopes = mutableListOf<Scope>()
        for (i in 0 until scopesArray.size()) {
            scopesArray.getString(i)?.let { scopes.add(Scope(it)) }
        }

        val request = AuthorizationRequest.builder()
            .setRequestedScopes(scopes)
            .build()

        Identity.getAuthorizationClient(activity)
            .authorize(request)
            .addOnSuccessListener { authorizationResult ->
                if (authorizationResult.hasResolution()) {
                    authorizationPromise = promise
                    try {
                        val pendingIntent = authorizationResult.pendingIntent
                        activity.startIntentSenderForResult(
                            pendingIntent?.intentSender,
                            RC_AUTHORIZE,
                            null, 0, 0, 0, null
                        )
                    } catch (e: Exception) {
                        authorizationPromise = null
                        promise.reject("INTENT_SENDER_ERROR", e.message, e)
                    }
                } else {
                    val resultMap = convertObjectToMap(authorizationResult)
                    val map = Arguments.createMap()
                    map.putMap("result", resultMap)
                    promise.resolve(map)
                }
            }
            .addOnFailureListener { e ->
                promise.reject("AUTHORIZATION_FAILED", "Failed to authorize", e)
            }
    }
    
    override fun signOut(promise: Promise) {
        val activity: Activity = reactContext.currentActivity
            ?: return promise.reject("NO_ACTIVITY", "ERROR: NO ACTIVITY")

        try {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val credentialManager = CredentialManager.create(activity)
                    credentialManager.clearCredentialState(ClearCredentialStateRequest())
                } catch (e: Exception) {
                }
            }

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val client = GoogleSignIn.getClient(activity, gso)
            googleSignInClient = client

            client.signOut().addOnCompleteListener { task: Task<Void> ->
                if (task.isSuccessful) {
                    promise.resolve("SIGN_OUT SUCCESS")
                } else {
                    promise.reject("SIGN_OUT_FAILED", "ERROR: SIGN_OUT", task.exception)
                }
            }
        } catch (e: Exception) {
            promise.reject("SIGN_OUT_ERROR", "SIGN_OUT ERROR: ${e.message}", e)
        }
    }

    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {        
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleLegacySignInResult(task)
        } else if (requestCode == RC_AUTHORIZE) {
            val promise = authorizationPromise
            authorizationPromise = null
            if (promise == null) return
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    val authorizationResult = Identity.getAuthorizationClient(activity).getAuthorizationResultFromIntent(data)
                    val accountMap = convertObjectToMap(authorizationResult)
                    val map = Arguments.createMap()
                    map.putMap("result", accountMap)
                    promise.resolve(map)
                } catch (e: ApiException) {
                    promise.reject("AUTHORIZATION_API_ERROR", "Authorization failed: ${e.statusCode}", e)
                }
            } else {
                promise.reject("AUTHORIZATION_CANCELLED", "User cancelled authorization")
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
    }
    
    private fun handleLegacySignInResult(completedTask: Task<GoogleSignInAccount>) {
        val promise = legacySignInPromise
        legacySignInPromise = null
        if (promise == null) return

        try {
            val account = completedTask.getResult(ApiException::class.java)
            val accountMap = convertObjectToMap(account)
            val map = Arguments.createMap()
            map.putMap("account", accountMap) 
            promise.resolve(map)
        } catch (e: ApiException) {
            promise.reject("SIGN_IN_FAILED", "SIGN_IN FAILURE: ${e.statusCode}", e)
        }
    }

    private fun signInInternal(
        options: ReadableMap,
        filterByAuthorizedAccounts: Boolean,
        promise: Promise
    ) {
        val activity: Activity = reactContext.currentActivity
            ?: return promise.reject("NO_ACTIVITY", "NO ACTIVITY FOUND")

        val clientId = options.getString("clientId")
            ?: return promise.reject("NO_CLIENT_ID", "NO CLIENT ID PROVIDED")

        val credentialManager = CredentialManager.create(activity)

        val googleOption = GetGoogleIdOption.Builder()
            .setServerClientId(clientId)
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(
                    context = activity,
                    request = request
                )
                handleGetCredentialSuccess(result, promise)
            } catch (e: GetCredentialException) {
                promise.reject("SIGN_IN_FAILED", e.message, e)
            } catch (e: Exception) {
                promise.reject("SIGN_IN_ERROR", e.message, e)
            }
        }
    }

    private fun handleGetCredentialSuccess(result: GetCredentialResponse, promise: Promise) {
        val credential = result.credential

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            
            val accountMap = convertObjectToMap(googleCredential)
            val map = Arguments.createMap()
            map.putMap("account", accountMap)
            promise.resolve(map)
        } else {
            promise.reject("INVALID_CREDENTIAL", "INVALID CREDENTIAL")
        }
    }

    private fun convertObjectToMap(obj: Any?): WritableMap {
        val map = Arguments.createMap()
        if (obj == null) return map

        val methods = obj.javaClass.methods
        for (method in methods) {
            if (Modifier.isPublic(method.modifiers) &&
                method.name.startsWith("get") &&
                method.parameterTypes.isEmpty() &&
                method.name != "getClass"
            ) {
                try {
                    val key = method.name.substring(3).replaceFirstChar { it.lowercase() }
                    val value = method.invoke(obj)

                    when (value) {
                        is String -> map.putString(key, value)
                        is Int -> map.putInt(key, value)
                        is Double -> map.putDouble(key, value)
                        is Boolean -> map.putBoolean(key, value)
                        null -> map.putNull(key)
                        else -> map.putString(key, value.toString())
                    }
                } catch (e: Exception) {

                }
            }
        }
        return map
    }

    companion object {
        const val NAME = NativeGoogleAuthSpec.NAME
    }
}