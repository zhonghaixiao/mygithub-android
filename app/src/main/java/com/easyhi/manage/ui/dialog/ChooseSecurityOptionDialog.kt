package com.easyhi.manage.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.easyhi.manage.R
import com.easyhi.manage.databinding.DialogChangeDeviceBinding
import com.easyhi.manage.databinding.DialogChooseSecurityBinding
import com.easyhi.manage.databinding.DialogConfirmBinding

class ChooseSecurityOptionDialog constructor(
    context: Context,
    private var clearAuthWhenExit: Boolean = true,
    private val confirmListener: OnConfirmListener,
    private val cancelListener: OnCancelListener
) : Dialog(context, R.style.FullSreenDialogTheme) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogChooseSecurityBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(false)

        binding.radioGroup.check(if (clearAuthWhenExit) R.id.radio_clear else R.id.radio_unclear)

        binding.commonHeader.txtDialogHeaderTitle.text = "登陆保护"

        binding.commonHeader.imageClose.setOnClickListener {
            dismiss()
        }

        binding.slConfirm.setOnClickListener {
            confirmListener.onConfirm(clearAuthWhenExit)
            dismiss()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            clearAuthWhenExit = (checkedId == R.id.radio_clear)
        }

        binding.slCancel.setOnClickListener {
            cancelListener.onCancel()
            dismiss()
        }

    }


    interface OnConfirmListener {
        fun onConfirm(clearAuthWhenExit: Boolean)
    }

    interface OnCancelListener {
        fun onCancel()
    }

}