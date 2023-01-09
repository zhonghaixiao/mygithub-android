package com.easyhi.manage.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.easyhi.manage.R
import com.easyhi.manage.databinding.DialogChangeDeviceBinding
import com.easyhi.manage.databinding.DialogConfirmBinding

class ChangeDeviceNameDialog constructor(
    context: Context,
    private val deviceName: String,
    private val confirmListener: OnConfirmListener,
    private val cancelListener: OnCancelListener
) : Dialog(context, R.style.FullSreenDialogTheme) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogChangeDeviceBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(false)

        binding.editDeviceName.setText(deviceName)
        binding.commonHeader.txtDialogHeaderTitle.text = "设备别名"

        binding.commonHeader.imageClose.setOnClickListener {
            dismiss()
        }

        binding.slConfirm.setOnClickListener {
            val newDeviceName = binding.editDeviceName.text.toString()
            confirmListener.onConfirm(newDeviceName)
            dismiss()
        }

        binding.slCancel.setOnClickListener {
            cancelListener.onCancel()
            dismiss()
        }

        binding.editDeviceName.requestFocus()

    }


    interface OnConfirmListener {
        fun onConfirm(deviceName: String)
    }

    interface OnCancelListener {
        fun onCancel()
    }

}