package com.rizky.bengkelin.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

fun File.resizeImageFile(sizeW: Int, sizeH: Int): File {
    BitmapFactory.decodeFile(this.path).let {
        val bitmap = Bitmap.createScaledBitmap(it, sizeW, sizeH, true)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(this))
    }
    return this
}

fun Int.formatToDistance() = if ((this % 1000) != this) {
    "${this / 1000}km"
} else "${this}m"