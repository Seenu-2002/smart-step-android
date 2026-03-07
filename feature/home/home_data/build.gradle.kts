plugins {
    alias(libs.plugins.smartstep.android.library.compose)
//    alias(libs.plugins.smartstep.android.room)
    alias(libs.plugins.google.ksp)
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

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.datastore.preferences)
    ksp(libs.androidx.room.compiler)
}