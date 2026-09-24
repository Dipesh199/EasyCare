package com.easycare.app.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

fun openDialer(context: Context, phoneNumber: String): Boolean = runCatching {
    val intent = Intent(Intent.ACTION_DIAL, "tel:${Uri.encode(phoneNumber)}".toUri())
    context.startActivity(intent)
    true
}.getOrDefault(false)

fun openSms(context: Context, phoneNumber: String, message: String): Boolean = runCatching {
    val intent = Intent(Intent.ACTION_SENDTO, "smsto:${Uri.encode(phoneNumber)}".toUri()).apply {
        putExtra("sms_body", message)
    }
    context.startActivity(intent)
    true
}.getOrDefault(false)
