plugins {
    alias(libs.plugins.smartstep.android.library)
}

android {
    namespace = "com.seenu.dev.android.smartstep.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)

    implementation(project(":core:domain"))
}