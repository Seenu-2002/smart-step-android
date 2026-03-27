package com.seenu.dev.android.smartstep.domain.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isOnline: Flow<Boolean>
}
