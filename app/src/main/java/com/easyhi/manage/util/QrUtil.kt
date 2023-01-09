package com.easyhi.manage.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

object QrUtil {

    fun zxing(content: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hintMap = hashMapOf<EncodeHintType, String>()
        hintMap[EncodeHintType.CHARACTER_SET] = "utf-8"

        var encode: BitMatrix?
        try {
            encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hintMap)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val colors = IntArray(width * height)
        for (i in 0 until width) {
            for (j in 0 until height) {
                if (encode.get(i, j)) {
                    colors[i * width + j] = Color.BLACK
                } else {
                    colors[i * width + j] = Color.WHITE
                }
            }
        }
        return Bitmap.createBitmap(colors, width, height, Bitmap.Config.RGB_565)

    }


}


