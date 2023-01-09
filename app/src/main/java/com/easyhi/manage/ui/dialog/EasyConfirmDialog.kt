package com.easyhi.manage.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.easyhi.manage.R
import com.easyhi.manage.databinding.DialogConfirmBinding

class EasyConfirmDialog private constructor(
    context: Context,
    private val title: String = "操作提示",
    private val content: String = "",
    private val confirmListener: (() -> Unit)?,
    private val cancelListener: (() -> Unit)?
) : Dialog(context, R.style.FullSreenDialogTheme) {

    constructor(builder: Builder): this(
        builder.context,
        builder.title,
        builder.content,
        builder.confirmListener,
        builder.cancelListener
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogConfirmBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(false)

        binding.commonHeader.imageClose.setOnClickListener {
            dismiss()
        }

        binding.commonHeader.txtDialogHeaderTitle.text = title
        binding.txtContent.text = content

        binding.slConfirm.setOnClickListener {
            confirmListener?.invoke()
            dismiss()
        }

        binding.slCancel.setOnClickListener {
            cancelListener?.invoke()
            dismiss()
        }

    }

    class Builder constructor(
        val context: Context
    ) {

        var title: String = "操作提示"
        var content: String = ""
        var confirmListener: (() -> Unit)? = null
        var cancelListener: (() -> Unit)? = null

        fun build() = EasyConfirmDialog(this)

    }

    companion object {
        inline fun build(context: Context, block: Builder.()->Unit) = Builder(context).apply(block).build()
    }

}