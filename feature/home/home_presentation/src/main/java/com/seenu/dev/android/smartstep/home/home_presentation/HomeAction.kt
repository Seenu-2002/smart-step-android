package com.seenu.dev.android.smartstep.home.home_presentation

sealed interface HomeAction {
    data class OnActivityRecognitionPermissionUpdate(val granted: Boolean): HomeAction
    object OnRequireActivityRecognitionPermission: HomeAction
    object RationaleRequired : HomeAction
    object OnPermissionUpdateRequired: HomeAction
    object OnBackgroundAccessRecommendedDismiss: HomeAction
    object OnBackgroundAccessRecommendedContinue: HomeAction
}