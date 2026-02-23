plugins {
    alias(libs.plugins.smartstep.android.library.compose)
}

android {
    namespace = "com.seenu.dev.android.smartstep.home.home_presentation"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(projects.core.designSystem)
    implementation(projects.core.domain)
    implementation(projects.feature.home.homeDomain)
}