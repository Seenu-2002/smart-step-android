plugins {
    alias(libs.plugins.smartstep.android.library)
}

android {
    namespace = "com.seenu.dev.android.smartstep.core.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)

    implementation(project(":core:domain"))
}