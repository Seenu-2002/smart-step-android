package com.seenu.dev.android.smartstep.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.seenu.dev.android.smartstep.domain.repository.PermissionRepository

class PermissionRepositoryImpl(private val context: Context) : PermissionRepository {
    override fun hasActivityRecognitionPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context, 
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Automatically granted below API 29
        }
    }
}