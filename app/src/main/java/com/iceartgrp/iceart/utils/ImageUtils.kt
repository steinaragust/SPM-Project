package com.iceartgrp.iceart.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class ImageUtils {
    companion object {
        private const val QUALITY = 100

        fun imageTo64Encoding(image: ImageProxy): String {
            val planeProxy = image.planes[0]
            val buffer: ByteBuffer = planeProxy.buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val mat = Matrix()
            mat.postRotate(90.0f)
            val outputStream = ByteArrayOutputStream()
            Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, mat, true)
                .compress(Bitmap.CompressFormat.JPEG, QUALITY, outputStream)
            val byteArr = outputStream.toByteArray()
            return Base64.encodeToString(byteArr, Base64.DEFAULT)
                .replace("\n", "")
        }

        fun imageFrom64Encoding(image: String): Bitmap {
            val decoded = Base64.decode(image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(decoded, 0, decoded.size)
            return decodedImage
        }
    }
}
