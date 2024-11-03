// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
// In der Datei build.gradle auf Projektebene
buildscript {
    dependencies {
        // Füge den Google Services Classpath so hinzu
        classpath(libs.google.services)
    }
}
