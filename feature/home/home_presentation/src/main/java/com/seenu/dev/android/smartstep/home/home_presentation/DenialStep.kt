package com.seenu.dev.android.smartstep.home.home_presentation

enum class DenialStep {
    FIRST_DENIAL,
    SECOND_DENIAL
}

fun increaseDenialStep(step: DenialStep?): DenialStep {
    return when (step) {
        null -> DenialStep.FIRST_DENIAL
        else -> DenialStep.SECOND_DENIAL
    }
}