package com.seenu.dev.android.smartstep.home.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seenu.dev.android.smartstep.domain.PermissionRepository
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val permissionRepository: PermissionRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    private val eventChannel = Channel<HomeEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        checkActivityRecognitionPermission()
    }

    fun onAction(homeAction: HomeAction) {
        when (homeAction) {
            is HomeAction.OnActivityRecognitionPermissionUpdate -> {
                _uiState.update {
                    it.copy(activityRecognitionPermissionGranted = homeAction.granted)
                }
                if (!homeAction.granted) {
                    _uiState.update {
                        val nextStep = increaseDenialStep(it.permissionDenialStep)
                        it.copy(permissionDenialStep = nextStep)
                    }
                } else {
                    checkBackgroundPermission()
                }
            }

            HomeAction.OnRequireActivityRecognitionPermission -> sendActivityRecognitionPermissionRequiredEvent()

            HomeAction.RationaleRequired -> {
                viewModelScope.launch {
                    preferenceManager.markActivityRecognitionRationaleShowed()
                }
            }

            HomeAction.OnPermissionUpdateRequired -> {
                val hasPermission = permissionRepository.hasActivityRecognitionPermission()
                _uiState.update {
                    it.copy(activityRecognitionPermissionGranted = hasPermission)
                }
                if (hasPermission) {
                    checkBackgroundPermission()
                }
            }
        }
    }

    private fun checkBackgroundPermission() {
        viewModelScope.launch {
            if (!preferenceManager.backgroundPermissionRequired.first()) {
                preferenceManager.markBackgroundPermissionRequired()
                eventChannel.send(HomeEvent.OnBackgroundPermissionRequired)
            }
        }
    }

    private fun checkActivityRecognitionPermission() {
        viewModelScope.launch {
            val hasPermission = permissionRepository.hasActivityRecognitionPermission()
            _uiState.update {
                it.copy(activityRecognitionPermissionGranted = hasPermission)
            }
            if (!hasPermission) {
                if (preferenceManager.activityRecognitionRationaleShowed.first()) {
                    _uiState.update {
                        it.copy(permissionDenialStep = DenialStep.SECOND_DENIAL)
                    }
                } else {
                    sendActivityRecognitionPermissionRequiredEvent()
                }
            }
        }
    }

    private fun sendActivityRecognitionPermissionRequiredEvent() {
        viewModelScope.launch {
            eventChannel.send(HomeEvent.OnActivityRecognitionPermissionRequired)
        }
    }

    private fun checkIsFirstInstall() {
        viewModelScope.launch {
            val isFirstInstall = preferenceManager.isFirstInstall.first()
            _uiState.value = _uiState.value.copy(isFirstInstall = isFirstInstall)
            if (isFirstInstall) preferenceManager.markFirstInstallCompleted()
        }
    }

}