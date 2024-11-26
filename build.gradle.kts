// build.gradle.kts (Project-level)
plugins {
    // Plugin Android và Google Services sẽ được áp dụng ở app-level, không áp dụng ở project-level
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
buildscript {
    dependencies {
        // Đây là cách khai báo classpath cho các plugin trong Kotlin DSL
        classpath("com.google.gms:google-services:4.4.2") // Firebase services plugin
        // Các dependencies khác nếu cần
    }
}
