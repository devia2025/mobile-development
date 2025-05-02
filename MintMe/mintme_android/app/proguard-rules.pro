# Keep the Application class
-keep public class * extends android.app.Application

# Keep your main activity (replace with your actual main activity name)
-keep public class com.official.mintme.Main {
    public <init>(...);
}

# Keep WebView and its methods
-keep class android.webkit.WebView { *; }

# Keep any custom classes used in WebView (if applicable)
-keep class com.official.mintme.** { *; }

# Keep all public methods in classes used by WebView
-keepclassmembers class * {
    public *;
}

# Keep any annotations (if used)
-keepattributes *Annotation*

# Ensure proper handling of URL loading
-keep class * extends android.webkit.WebViewClient
-keep class * extends android.webkit.WebChromeClient

# Keep any specific libraries you are using (if applicable)
# For example, if you use a library for network calls or JSON parsing, keep its classes
-keep class com.squareup.okhttp.** { *; }
-keep class com.google.gson.** { *; }

# Keep the default constructor for Gson, if used
-keepclassmembers class * {
    public <init>(...);
}

# Keep Application Classes: Ensure your application classes are not obfuscated or removed
-keep class com.official.mintme.** { *; }

# Keep Android Components: Prevent ProGuard from removing essential Android components
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Keep Your Application’s Entry Point: Ensure your main activity is not obfuscated
# Additional Security Measures
-keep class com.official.mintme.Main { *; }
-keep class com.official.mintme.Source.utils.Constants { *; }

# Keep Static Methods: If you have static methods that are referenced via reflection, keep them
-keepclassmembers class * {
    public static <fields>;
    public static <methods>;
}