package com.caesar84mx.textreader.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.util.*

fun Image.toBitmap(): Bitmap {
    val array = toJpgBytes()
    return BitmapFactory.decodeByteArray(array, 0, array.size, null)
}

fun Image.toJpgBytes(): ByteArray {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    return bytes
}

const val REQUEST_CODE_PERMISSIONS = 10
val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

val ComponentActivity.allPermissionsGranted: Boolean
    get() = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

fun ComponentActivity.requestPermissions() {
    ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
}

val Activity.tempImageFile: File
    get() = File.createTempFile(
        "JPEG_${Date().time}_",
        ".jpg",
        cacheDir
    )