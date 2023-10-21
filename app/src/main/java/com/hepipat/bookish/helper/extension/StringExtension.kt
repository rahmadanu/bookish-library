package com.hepipat.bookish.helper.extension

import android.content.Context
import android.util.Patterns
import com.hepipat.bookish.R

fun String.hasValidStudentEmail(context: Context): Boolean {
    val studentEmailFormat = context.getString(R.string.student_email_format)
    return this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches() &&
            this.matches(".*\\b$studentEmailFormat\\b.*".toRegex())
}