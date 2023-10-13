package com.hepipat.bookish.helper.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class PermissionCore(activity: FragmentActivity) {

    private val activityReference = activity
    private var permissions: List<String> = ArrayList()
    private var acceptedCallback: ResponsePermissionCallback? = null
    private var deniedCallback: ResponsePermissionCallback? = null
    private var foreverDeniedCallback: ResponsePermissionCallback? = null

    fun permissions(permission: List<String>): PermissionCore {
        permissions = permission
        return this@PermissionCore
    }

    fun onAccepted(callback: (List<String>) -> Unit): PermissionCore {
        this.acceptedCallback = object : ResponsePermissionCallback {
            override fun onResult(permissionResult: List<String>) {
                callback(permissionResult)
            }
        }
        return this@PermissionCore
    }

    fun onAccepted(callback: ResponsePermissionCallback): PermissionCore {
        this.acceptedCallback = callback
        return this@PermissionCore
    }

    fun onDenied(callback: (List<String>) -> Unit): PermissionCore {
        this.deniedCallback = object : ResponsePermissionCallback {
            override fun onResult(permissionResult: List<String>) {
                callback(permissionResult)
            }
        }
        return this@PermissionCore
    }

    fun onDenied(callback: ResponsePermissionCallback): PermissionCore {
        this.deniedCallback = callback
        return this@PermissionCore
    }

    fun onForeverDenied(callback: (List<String>) -> Unit): PermissionCore {
        this.foreverDeniedCallback = object : ResponsePermissionCallback {
            override fun onResult(permissionResult: List<String>) {
                callback(permissionResult)
            }
        }
        return this@PermissionCore
    }

    fun onForeverDenied(callback: ResponsePermissionCallback): PermissionCore {
        this.foreverDeniedCallback = callback
        return this@PermissionCore
    }

    fun ask() {
        if (permissions.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            permissionAlreadyAccepted(activityReference, permissions)
        ) {
            onAcceptedPermission(permissions)
        } else {
            addPermissionForRequest(permissions)
        }
    }

    private fun permissionAlreadyAccepted(
        context: Context,
        permissions: List<String>,
    ): Boolean {
        for (permission in permissions) {
            val permissionState = ContextCompat.checkSelfPermission(context, permission)
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    private fun onAcceptedPermission(permissions: List<String>) {
        onReceivedPermissionResult(permissions, null, null)
    }

    private val resultMultiplePermissions = activityReference.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val acceptedPermissions = ArrayList<String>()
        val askAgainPermissions = ArrayList<String>()
        val refusedPermissions = ArrayList<String>()

        permissions.entries.forEach {
            val permissionName = it.key
            val accepted = it.value
            if (accepted) {
                acceptedPermissions.add(permissionName)
            } else {
                if (shouldShowRequestPermissionRationale(activityReference, permissionName)) {
                    askAgainPermissions.add(permissionName)
                } else {
                    refusedPermissions.add(permissionName)
                }
            }
        }

        onReceivedPermissionResult(acceptedPermissions, refusedPermissions, askAgainPermissions)
    }

    private fun addPermissionForRequest(permissions: List<String>) {
        resultMultiplePermissions.launch(permissions.toTypedArray())
    }

    private fun onReceivedPermissionResult(
        acceptedPermissions: List<String>?,
        foreverDenied: List<String>?,
        denied: List<String>?,
    ) {
        acceptedPermissions?.let {
            acceptedCallback?.onResult(it)
        }

        foreverDenied?.let {
            foreverDeniedCallback?.onResult(it)
        }

        denied?.let {
            deniedCallback?.onResult(it)
        }
    }
}