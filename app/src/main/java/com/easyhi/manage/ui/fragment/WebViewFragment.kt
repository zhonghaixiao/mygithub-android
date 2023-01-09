package com.easyhi.manage.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.easyhi.manage.BuildConfig
import com.easyhi.manage.MyApplication
import com.easyhi.manage.R
import com.easyhi.manage.databinding.FragmentWebviewBinding
import com.easyhi.manage.h5plugin.EasyHiAPi
import com.easyhi.manage.ui.viewmodel.BindingViewModel
import com.easyhi.manage.util.KEY_CURRENT_URL
import com.easyhi.manage.util.KEY_EASY_ID
import com.easyhi.manage.util.SnUtil
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings.LOAD_DEFAULT
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "WebViewFragment"

@AndroidEntryPoint
class WebViewFragment : Fragment() {

    private val viewModel by activityViewModels<BindingViewModel>()
    private lateinit var webView: WebView
    private var binding: FragmentWebviewBinding? = null
    private var webViewLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sn = SnUtil.generateSnIfNeed()
        val easyId = MyApplication.sp.getString(KEY_EASY_ID, "")
        if (easyId == "") {
            navigateToBindingFragment()
        } else {
            viewModel.checkBindEasyId(easyId!!, sn) { resp ->
                if (resp.code != 0) {
                    navigateToBindingFragment()
                } else {
                    if (BuildConfig.DEBUG) {
                        Toast.makeText(
                            requireActivity(),
                            "easyId = $easyId, sn = $sn",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        requireActivity().finish()
                    }
                }

            })

        webView = WebView(requireActivity())

        initWebViewSetting()

        initWebViewClient()

        initWebChromeClient()

        initJavaScriptInterface()

//        webView.loadUrl("file:///android_asset/demo/first.html")

    }

    private fun initJavaScriptInterface() {
        webView.addJavascriptInterface(EasyHiAPi(webView), "easyHiObj")
    }

    private fun initWebViewClient() {
        webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(webview: WebView, url: String) {
                super.onPageFinished(webview, url)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onPageFinished = url = $url")
                }
                MyApplication.sp.edit().putString(KEY_CURRENT_URL, url).apply()
            }

        }
    }

    private fun initWebChromeClient() {
        webView.webChromeClient = object : WebChromeClient() {

        }
    }

    private fun initWebViewSetting() {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.cacheMode = LOAD_DEFAULT
        settings.setUserAgent("easyHi")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentWebviewBinding>(
            inflater,
            R.layout.fragment_webview,
            container,
            false
        )
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        binding!!.container.addView(webView, layoutParams)
        val targetUrl = getUrl()
        if (!webViewLoaded && targetUrl.isNotEmpty()) {
            Log.d(TAG, "binding.webview.loadUrl(getUrl() = ${targetUrl})")
            webViewLoaded = true
            webView.loadUrl(targetUrl)

        }


        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.container?.removeAllViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

    private fun getUrl(): String {

        val easyId = MyApplication.sp.getString(KEY_EASY_ID, "")
        if (easyId.isNullOrEmpty()) {
            return ""
        }
        val currentUrl = MyApplication.sp.getString(
            KEY_CURRENT_URL,
            "${BuildConfig.webview_prefix}${easyId}/admin#/login"
        )

//        val easyId = "2020060002"
//
//        val currentUrl = "https://mapp.easy-hi.com/m/2020060002/admin#/login"
//        val currentUrl = "https://test.easy-hi.com/m/2020060001/admin#/login"
        return currentUrl!!
    }


    private fun navigateToBindingFragment() {
        findNavController().apply {

            val action = WebViewFragmentDirections.actionWebviewFragmentToBindingFragment()
            navigate(action)
        }
    }

}






