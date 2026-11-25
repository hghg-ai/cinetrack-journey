# 1. Bảo vệ Retrofit & OkHttp
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# 2. Bảo vệ Kotlinx Serialization
-keepattributes *Annotation*,Signature,InnerClasses
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
# 3. Bảo vệ Room Database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# 4. Bảo vệ Hilt / Dagger Dependency Injection
-keep class * extends dagger.hilt.**
