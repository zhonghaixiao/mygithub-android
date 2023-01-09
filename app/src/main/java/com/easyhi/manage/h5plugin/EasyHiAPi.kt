package com.easyhi.manage.h5plugin

import android.util.Log
import android.webkit.JavascriptInterface
import com.easyhi.manage.util.printer.PrintContentParser
import com.easyhi.manage.util.printer.Printer
import com.easyhi.manage.util.printer.SunmiPrintHelper
import com.easyhi.manage.util.printer.content
import com.google.gson.Gson
import com.tencent.smtt.sdk.WebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TAG = "EasyHiAPi"

class EasyHiAPi(
    private val webView: WebView
) {

    private val gson = Gson()
    private val printer = Printer()

    @JavascriptInterface
    fun callHandler(funcCode: String, params: String) {
        val paramsObj = gson.fromJson(params, Map::class.java)
        Log.d(TAG, "call hello funcCode = $funcCode, params = $params")

        GlobalScope.launch(Dispatchers.IO) {
            val composition = "$content<cut/>"
            val contents = PrintContentParser.getBytesForXMLTemplate(composition)
            Log.d(TAG, "contents = $contents")
            SunmiPrintHelper.getInstance().printContents(contents)
        }

        webView.post {
            webView.evaluateJavascript(
                "javascript:easyHiCallback('${funcCode}', ${gson.toJson(paramsObj)})",
                null
            )
        }
    }


}