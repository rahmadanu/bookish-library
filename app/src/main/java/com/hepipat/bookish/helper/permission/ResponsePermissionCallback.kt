package com.hepipat.bookish.helper.permission

interface ResponsePermissionCallback {
    fun onResult(permissionResult: List<String>)
}