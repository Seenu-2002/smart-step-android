package com.seenu.dev.android.smartstep.home.home_presentation

sealed interface HomeAction {
    data class OnActivityRecognitionPermissionUpdate(val granted: Boolean) : HomeAction
    object OnRequireActivityRecognitionPermission : HomeAction
    object RationaleRequired : HomeAction
    object OnPermissionUpdateRequired : HomeAction
    object OnBackgroundAccessRecommendedDismiss : HomeAction
    object OnBackgroundAccessRecommendedContinue : HomeAction
    object CheckIsIgnoringBatteryOptimizations : HomeAction
    object OnFixStopCountingStepIssueClick : HomeAction
    object ShowExitConfirmationDialog : HomeAction
    object DismissExitConfirmationDialog : HomeAction
    object ShowStepGoalSheet : HomeAction
    object DismissStepGoalSheet : HomeAction
    data class UpdateStepGoal(val stepGoal: Int) : HomeAction
}