package com.hepipat.bookish.helper.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class PermissionCore(activity: FragmentActivity) {

    private val activityReference = activity
    private var permissions: List<String> = ArrayList()
    private var acceptedCallback: ResponsePermissionCallback? = null
    private var deniedCallback: ResponsePermissionCallback? = null
    private var foreverDeniedCallback: ResponsePermissionCallback? = null

    private val listener = object : PermissionFragment.PermissionListener {
        override fun onRequestPermissionsResult(
            acceptedPermissions: List<String>,
            refusedPermissions: List<String>,
            askAgainPermissions: List<String>,
        ) {
            onReceivedPermissionResult(acceptedPermissions, refusedPermissions, askAgainPermissions)
        }
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

    private fun addPermissionForRequest(permissions: List<String>) {
        val oldFragment =
            activityReference.supportFragmentManager.findFragmentByTag(TAG) as PermissionFragment?

        if (oldFragment != null) {
            oldFragment.addPermissionForRequest(listener, permissions)
        } else {
            val newFragment = PermissionFragment.newInstance()
            newFragment.addPermissionForRequest(listener, permissions)
            activityReference.runOnUiThread {
                activityReference.supportFragmentManager.beginTransaction().add(newFragment, TAG)
                    .commitNowAllowingStateLoss()
            }
        }
    }

    companion object {
        private const val TAG = "PermissionCore"
    }
}