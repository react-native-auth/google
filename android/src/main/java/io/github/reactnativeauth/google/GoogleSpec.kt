package io.github.reactnativeauth.google

import com.facebook.react.bridge.*
import com.facebook.react.turbomodule.core.interfaces.TurboModule

interface GoogleSpec : TurboModule {
    fun oneTap(
        options: ReadableMap,
        promise: Promise,
    )

    fun signIn(
        options: ReadableMap,
        promise: Promise,
    )

    fun signOut(promise: Promise)
}
