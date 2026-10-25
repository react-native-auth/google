-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keep,allowobfuscation @interface com.facebook.proguard.annotations.DoNotStrip
-keep,allowobfuscation @interface com.facebook.proguard.annotations.KeepGettersAndSetters
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

-keep @com.facebook.proguard.annotations.DoNotStrip class *
-keep @com.facebook.common.internal.DoNotStrip class *

-keepclassmembers class * {
    @com.facebook.proguard.annotations.DoNotStrip *;
    @com.facebook.common.internal.DoNotStrip *;
}
-keep class * extends com.facebook.react.bridge.NativeModule { *; }
-keep class * extends com.facebook.react.bridge.JavaScriptModule { *; }
-keep class com.facebook.react.turbomodule.** { *; }
-keep class * extends com.facebook.react.uimanager.ViewManager { *; }
-keep class * extends com.facebook.react.uimanager.ReactShadowNode { *; }
-keep class * { @com.facebook.react.uimanager.annotations.ReactProperty <methods>; }
-keep class * { @com.facebook.react.uimanager.annotations.ReactPropGroup <methods>; }

-keep class com.google.android.gms.auth.** { *; }