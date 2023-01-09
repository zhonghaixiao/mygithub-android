package com.easyhi.manage.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.easyhi.manage.BuildConfig
import com.easyhi.manage.MyApplication
import com.easyhi.manage.R
import com.easyhi.manage.databinding.FragmentSettingBinding
import com.easyhi.manage.ui.dialog.ChangeDeviceNameDialog
import com.easyhi.manage.ui.dialog.ChooseSecurityOptionDialog
import com.easyhi.manage.ui.dialog.EasyConfirmDialog
import com.easyhi.manage.ui.viewmodel.BindingCodeViewModel
import com.easyhi.manage.ui.viewmodel.SettingViewModel
import com.easyhi.manage.util.KEY_CLEAR_COOKIE_WHEN_EXIT
import com.easyhi.manage.util.KEY_CURRENT_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private val bindingCodeViewModel by viewModels<BindingCodeViewModel>()
    private val settingViewModel by viewModels<SettingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingBinding.inflate(LayoutInflater.from(context))
        binding.settingEasyid.setOnBtnClick {
            EasyConfirmDialog.build(requireContext()) {
                this.title = "操作提示"
                this.content = "即将进行解绑操作，请谨慎操作，是否继续？"
                this.confirmListener = {
                    bindingCodeViewModel.unbindDevice { resp ->
                        lifecycleScope.launchWhenResumed {
                            if (resp.code == 0) {
                                Toast.makeText(requireContext(), "解绑成功", Toast.LENGTH_SHORT).show()
                                findNavController().apply {
                                    val action =
                                        SettingFragmentDirections.actionSettingFragmentToBindingFragment()
                                    navigate(action, navOptions = navOptions {
                                        popUpTo(R.id.bindingFragment) {
                                            inclusive = true
                                        }
                                    })
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "${resp.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }.show()
        }

        binding.settingDeviceName.setOnBtnClick {
            val preDeviceName = binding.settingDeviceName.getContent()
            ChangeDeviceNameDialog(
                requireContext(),
                preDeviceName,
                object : ChangeDeviceNameDialog.OnConfirmListener {
                    override fun onConfirm(deviceName: String) {
                        settingViewModel.renameDevice(deviceName) { resp ->
                            Toast.makeText(
                                requireContext(),
                                if (resp.code == 0) "修改成功" else "修改失败",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                },
                object : ChangeDeviceNameDialog.OnCancelListener {
                    override fun onCancel() {
                    }

                }).show()
        }

        binding.settingClearAuth.setOnBtnClick {
            val clearAuthWhenExit = MyApplication.sp.getBoolean(KEY_CLEAR_COOKIE_WHEN_EXIT, false)
            ChooseSecurityOptionDialog(
                requireContext(),
                clearAuthWhenExit,
                object : ChooseSecurityOptionDialog.OnConfirmListener {
                    override fun onConfirm(clearAuthWhenExit: Boolean) {
                        MyApplication.sp.edit()
                            .putBoolean(KEY_CLEAR_COOKIE_WHEN_EXIT, clearAuthWhenExit).apply()
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(
                                requireContext(),
                                "onConfirm[$clearAuthWhenExit]",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        updateClearAuthState(binding)
                    }

                },
                object : ChooseSecurityOptionDialog.OnCancelListener {
                    override fun onCancel() {
                        if (BuildConfig.DEBUG) {
                            Toast.makeText(requireContext(), "onCancel", Toast.LENGTH_SHORT).show()
                        }
                    }

                }).show()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        settingViewModel.initDeviceInfo()

        settingViewModel.deviceInfoData.observe(viewLifecycleOwner) { deviceInfo ->
            binding.txtDeviceSn.text = "设备SN: ${deviceInfo.sn}"
            binding.settingEasyid.setContent(deviceInfo.easyId)
            binding.settingMerchantName.setContent(deviceInfo.merchantName)
            binding.settingBindTime.setContent(deviceInfo.bindTime ?: "")
            binding.settingDeviceType.setContent(deviceInfo.deviceType)
            binding.settingDeviceName.setContent(deviceInfo.deviceName)
        }
        updateClearAuthState(binding)

        return binding.root
    }

    private fun updateClearAuthState(binding: FragmentSettingBinding){
        val clearAuthWhenExit = MyApplication.sp.getBoolean(KEY_CLEAR_COOKIE_WHEN_EXIT, false)
        binding.settingClearAuth.setContent(
            if (clearAuthWhenExit)
                "关闭APP时，自动退出登录状态"
            else
                "关闭APP时，不自动退出登录状态"
        )
    }


}






