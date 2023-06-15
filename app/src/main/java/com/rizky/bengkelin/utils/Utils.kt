package com.rizky.bengkelin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.math.RoundingMode
import java.text.NumberFormat
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

fun File.resizeImageFile(sizeW: Int, sizeH: Int): File {
    BitmapFactory.decodeFile(this.path).let {
        val bitmap = Bitmap.createScaledBitmap(it, sizeW, sizeH, true)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(this))
    }
    return this
}

fun Double.formatToDistance(): String {
    return if ((this % 1000) != this) {
        val distance = this / 1000
        val result = distance.toBigDecimal()
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
        "${result}km"
    } else "${this}m"
}

fun Int.formatToCurrency() = NumberFormat.getCurrencyInstance(
    Locale.getDefault()
).format(this).toString()

fun isLatLongValid(
    lat: Double, long: Double
) = lat >= -90 && lat <= 90 && long >= -180 && long <= 180