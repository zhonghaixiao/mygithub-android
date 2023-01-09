package com.easyhi.manage.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.bumptech.glide.Glide
import com.easyhi.manage.BuildConfig
import com.easyhi.manage.R
import com.easyhi.manage.databinding.FragmentBindingBinding
import com.easyhi.manage.databinding.FragmentBindingCodeBinding
import com.easyhi.manage.ui.viewmodel.*
import com.easyhi.manage.util.SnUtil
import com.easyhi.manage.util.gone
import com.easyhi.manage.util.visible
import com.easyhi.manage.util.withDebug
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

private const val TAG = "BindingCode"

@AndroidEntryPoint
class BindingCodeDialogFragment : DialogFragment() {

    private val viewModel by activityViewModels<BindingViewModel>()
    private val bindingCodeViewModel by viewModels<BindingCodeViewModel>()

    private var confirmed = false

    private val args: BindingCodeDialogFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NORMAL, R.style.FullSreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentBindingCodeBinding>(
            inflater,
            R.layout.fragment_binding_code,
            container,
            false
        ).apply {
            dialogHeader.imageClose.setOnClickListener {
                doClose()
            }
            slButtonCount.setOnClickListener {
                doClose()
            }

        }

        val sn = SnUtil.generateSnIfNeed()
        val easyId = args.easyId

        binding.txtDeviceSn.text = sn
        if (easyId.isNullOrEmpty()) {
            bindingCodeViewModel.generateAuthUrl(sn)
            binding.dialogHeader.txtDialogHeaderTitle.text = "设置"
            viewModel.resetCount()
            bindingCodeViewModel.cancelPoll()
        } else {
            bindingCodeViewModel.generateDeviceBindUrl(easyId, sn)
            binding.dialogHeader.txtDialogHeaderTitle.text = "设备绑定"
        }

        viewModel.countData.observe(viewLifecycleOwner) { count ->
            if (confirmed) {
                binding.txtCount.text = "关闭"
            } else if (count > 0) {
                binding.txtCount.text = "关闭（${count}s）"
            } else if (count == 0) {
                binding.txtCount.text = "关闭"
                popBack()
            }
        }

        bindingCodeViewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            withDebug {
                Log.d(TAG, "uiState = $uiState")
            }
            when (uiState) {
                is BindingStateInit -> {
                    // 展示二维码
                    viewModel.beginCount()
                    uiState.qrBitmap?.let {
                        binding.imageQrcode.setImageBitmap(it)
                        if (easyId != null) {
                            bindingCodeViewModel.pollDeviceBinding(easyId, uiState.token)
                        }
                    }
                }
                is BindingStateError -> {
                    Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_SHORT).show()
                }
                is BindingStateExpired -> {
                    viewModel.resetCount()
                    binding.llSuccessTip.visible()
                    binding.txtQrState.text = "二维码已过期"
                    Glide.with(binding.imageQrState).load(R.drawable.ic_fail)
                        .into(binding.imageQrState)
                }
                is BindingStateWaitConfirm -> {
                    binding.llSuccessTip.gone()
                }
                is BindingStateConfirmed -> {
                    confirmed = true
                    viewModel.resetCount()
                    if (bindingCodeViewModel.isBinding) {
                        binding.txtScanState.text = "本设备成功绑定easyID：${uiState.easyId}"
                    }
                    binding.llSuccessTip.visible()
                    binding.txtQrState.text = "验证通过"
                    Glide.with(binding.imageQrState).load(R.drawable.ic_icon_success)
                        .into(binding.imageQrState)
                    if (!bindingCodeViewModel.isBinding) {
                        lifecycleScope.launchWhenResumed {
                            delay(1000)
                            goToSetting()
                        }
                    }
                }
                else -> {

                }
            }
        }

        return binding.root
    }

    private fun doClose() {
        if (confirmed) {
            if (bindingCodeViewModel.isBinding) {
                findNavController().apply {
                    val action =
                        BindingCodeDialogFragmentDirections.actionBindingCodeFragmentToWebviewFragment()
                    navigate(action, navOptions = navOptions {
                        popUpTo(R.id.webviewFragment) {
                            inclusive = true
                        }
                    })
                }
            } else {
                goToSetting()
            }
        } else {
            popBack()
        }
    }

    private fun goToSetting() {
        findNavController().apply {
            val action =
                BindingCodeDialogFragmentDirections.actionBindingCodeFragmentToSettingFragment()
            navigate(action)
        }
    }

    private fun popBack() {
        findNavController().apply {
            popBackStack()
        }
    }

}