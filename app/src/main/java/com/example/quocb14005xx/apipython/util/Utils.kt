package com.example.quocb14005xx.apipython.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    /*
    * hàm chuyển ảnh image(Bitmap) sang code base64 string
    * */
    fun encodeToBase64(image: Bitmap, compressFormat: Bitmap.CompressFormat, quality: Int): String {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(compressFormat, quality, byteArrayOS)
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
    }

    fun now(d: Long): String{
        val date= Date(d)
        val spf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return spf.format(date)
    }
}