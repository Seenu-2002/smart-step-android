plugins {
    alias(libs.plugins.smartstep.android.library.compose)
    alias(libs.plugins.smartstep.android.feature.ui)
}

android {
    namespace = "com.seenu.dev.android.smartstep.profile_setup"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)
    implementation(projects.core.designSystem)
}