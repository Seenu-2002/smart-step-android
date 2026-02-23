plugins {
    alias(libs.plugins.smartstep.android.library.compose)
}

android {
    namespace = "com.seenu.dev.android.smartstep.home.home_data"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    // Koin
    implementation(libs.koin.android)

    implementation(projects.core.designSystem)
    implementation(projects.feature.home.homeDomain)
}