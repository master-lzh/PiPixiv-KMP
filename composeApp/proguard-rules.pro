# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 保持不被混淆的类和成员
#-keep class androidx.** { *; }
#-keep interface androidx.** { *; }
#-dontwarn androidx.**

# 对于使用了 @Keep 注解的类和成员，不进行混淆处理
-keep @androidx.annotation.Keep class * {

}

# Jetpack Compose 相关规则
#-keep class androidx.compose.** { *; }
#-dontwarn androidx.compose.**

# 由于 Jetpack Compose 使用了 Kotlin 反射，需要保留相关的类和成员
#-keep class kotlin.reflect.** { *; }
#-keep class kotlin.Metadata { *; }
#-dontwarn kotlin.reflect.**

# 保持 Kotlin 标准库不被混淆
-keep class kotlin.concurrent.** { *; }
-keep class kotlin.coroutines.** { *; }
-dontwarn kotlin.**

# 如果使用了 Kotlin 协程
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# 保持 protobuf 生成的消息类不被混淆
#-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
#    <fields>;
#    <methods>;
#}


-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder
