import java.util.Properties

plugins {
    alias(libs.plugins.smartstep.android.library.compose)
}

android {
    namespace = "com.seenu.dev.android.smartstep.ai_coach"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { properties.load(it) }
        }

        val geminiApiKey = (properties.getProperty("GEMINI_API_KEY") 
            ?: project.findProperty("GEMINI_API_KEY")?.toString() 
            ?: "")

        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
    }
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

    // Gemini AI SDK
    implementation(libs.generativeai)

    implementation(projects.core.designSystem)
    implementation(projects.core.domain)
}
