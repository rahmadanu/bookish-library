package com.hepipat.bookish.helper.permission

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import timber.log.Timber
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * WARNING! DO NOT USE THIS FRAGMENT DIRECTLY!
 */
class PermissionFragment : Fragment() {

    private val permissionQueue = ConcurrentLinkedQueue<PermissionHolder>()
    private var permissionsList: List<String> = ArrayList()
    private var listener: PermissionListener? = null
    private var waitingForReceive = false

    override fun onResume() {
        super.onResume()
        runQueuePermission()
    }

    private fun runQueuePermission() {
        if (waitingForReceive) return
        val poll = permissionQueue.poll()
        if (poll != null) {
            waitingForReceive = true
            this.listener = poll.listener
            permissionsList = ArrayList(poll.permissions)
            proceedPermissions()
        } else {
            if (!waitingForReceive) removeFragment()
        }
    }

    private fun proceedPermissions() {
        val perms = permissionsList.toTypedArray()
        requestPermissions(perms, REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (requestCode == REQUEST_CODE && permissions.isNotEmpty() && grantResults.isNotEmpty()) {

            val acceptedPermissions = ArrayList<String>()
            val askAgainPermissions = ArrayList<String>()
            val refusedPermissions = ArrayList<String>()

            for (i in permissions.indices) {
                val permissionName = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    acceptedPermissions.add(permissionName)
                } else {
                    if (shouldShowRequestPermissionRationale(permissionName)) {
                        askAgainPermissions.add(permissionName)
                    } else {
                        refusedPermissions.add(permissionName)
                    }
                }
            }

            listener?.onRequestPermissionsResult(
                acceptedPermissions,
                refusedPermissions,
                askAgainPermissions
            )
        }
        waitingForReceive = false
    }

    private fun removeFragment() {
        try {
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this@PermissionFragment).commitAllowingStateLoss()
        } catch (e: Exception) {
            Timber.tag(TAG).w("Error while removing fragment")
        }
    }

    internal fun addPermissionForRequest(listener: PermissionListener, permission: List<String>) {
        permissionQueue.add(PermissionHolder(permission, listener))
    }

    internal interface PermissionListener {
        fun onRequestPermissionsResult(
            acceptedPermissions: List<String>,
            refusedPermissions: List<String>,
            askAgainPermissions: List<String>,
        )
    }

    private data class PermissionHolder(
        val permissions: List<String>,
        val listener: PermissionListener,
    )

    companion object {
        private const val REQUEST_CODE = 23000

        fun newInstance(): PermissionFragment {
            return PermissionFragment()
        }

        private const val TAG = "PermissionFragment"
    }
}
