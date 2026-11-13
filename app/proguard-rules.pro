# Retrofit & OkHttp
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Kotlinx Serialization
-keepattributes *Annotation*,Signature,InnerClasses
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}

# Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Hilt / Dagger
-keep class * extends dagger.hilt.**