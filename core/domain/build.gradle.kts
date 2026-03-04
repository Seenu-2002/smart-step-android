plugins {
    alias(libs.plugins.smartstep.android.library)
}

android {
    namespace = "com.seenu.dev.android.smartstep.core.domain"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
}