package com.easyhi.manage.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.navOptions
import com.easyhi.manage.MyApplication
import com.easyhi.manage.R
import com.easyhi.manage.databinding.ActivityMainBinding
import com.easyhi.manage.ui.fragment.WebViewFragmentDirections
import com.easyhi.manage.ui.viewmodel.SettingViewModel
import com.easyhi.manage.util.*
import com.easyhi.manage.util.printer.SunmiPrintHelper
import com.tencent.smtt.sdk.CookieManager
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.header.llSetting.setOnClickListener {
            findNavController(binding.navHostFragment).apply {
                WebViewFragmentDirections
                navigate(R.id.bindingCodeFragment, null, navOptions = navOptions {
                    popUpTo(R.id.bindingCodeFragment) {
                        inclusive = true
                    }
                })
            }
        }

        viewModel.deviceInfoData.observe(this) {
            binding.header.txtShopAlias.text = "${it.merchantName} ${it.deviceName}"
        }

        SunmiPrintHelper.getInstance().initPrinter()

        clearAuthInfoIfNeed()
    }

    override fun onResume() {
        super.onResume()
        findNavController(binding.navHostFragment).addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id != R.id.webviewFragment && destination.id != R.id.settingFragment) {
                binding.header.txtShopAlias.gone()
                binding.header.llSetting.gone()
            } else {
                viewModel.initDeviceInfo()
                binding.header.txtShopAlias.visible()
                binding.header.llSetting.visible()
            }
        }
    }

}







