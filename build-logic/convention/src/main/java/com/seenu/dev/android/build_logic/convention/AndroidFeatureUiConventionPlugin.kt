package com.seenu.dev.android.build_logic.convention

import com.seenu.dev.android.build_logic.convention.extensions.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            with(pluginManager) {
                apply("smartstep.android.library.compose")
            }

            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }

}