package com.binarystack01.pix.presentation.viewmodel.permissionsviewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionsViewModel : ViewModel() {

    private val _permissionState = MutableStateFlow(false)
    val permissionState: StateFlow<Boolean> = _permissionState.asStateFlow()


    fun onCameraPermission(permission: String, isGranted: Boolean) {
        if (isGranted) {
            Log.d("PERMISSION", "onPermissionResult: $permission $isGranted")
            _permissionState.value = true
        }
    }

    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}