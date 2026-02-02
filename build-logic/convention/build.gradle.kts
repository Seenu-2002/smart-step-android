import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.seenu.dev.android.smartstep.build_logic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "smartstep.android.application"
            implementationClass =
                "com.seenu.dev.android.build_logic.convention.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "smartstep.android.application.compose"
            implementationClass =
                "com.seenu.dev.android.build_logic.convention.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "smartstep.android.library"
            implementationClass =
                "com.seenu.dev.android.build_logic.convention.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "smartstep.android.library.compose"
            implementationClass =
                "com.seenu.dev.android.build_logic.convention.AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureUi") {
            id = "smartstep.android.feature.ui"
            implementationClass =
                "com.seenu.dev.android.build_logic.convention.AndroidFeatureUiConventionPlugin"
        }
        register("androidRoom") {
            id = "smartstep.android.room"
            implementationClass =
                "com.seenu.dev.android.build_logic.convention.AndroidRoomConventionPlugin"
        }
        register("jvmLibrary") {
            id = "smartstep.jvm.library"
            implementationClass =
                "com.seenu.dev.android.build_logic.convention.JvmLibraryConventionPlugin"
        }
    }
}