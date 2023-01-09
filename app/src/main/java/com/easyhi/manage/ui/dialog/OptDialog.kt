package com.easyhi.manage.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.easyhi.manage.R
import com.easyhi.manage.databinding.DialogChangeDeviceBinding
import com.easyhi.manage.databinding.DialogConfirmBinding
import com.easyhi.manage.databinding.DialogOperationBinding

class OptDialog private constructor(
    context: Context,
    private val type: OptState,
    private val tip: String = ""
) : Dialog(context, R.style.FullSreenDialogTheme) {

    constructor(builder: Builder): this(
        builder.context,
        builder.type,
        builder.tip
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogOperationBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(false)

        binding.txtState.text = tip
    }

    class Builder constructor(
        val context: Context
    ) {

        var type: OptState = OptState.SUCCESS
            private set

        var tip: String = ""
            private set

        fun setType(type: OptState) = apply {
            this.type = type
        }

        fun setTip(tip: String) = apply {
            this.tip = tip
        }

        fun build() = OptDialog(this)

    }

    enum class OptState{
        SUCCESS, FAIL, LOADING
    }

    companion object {
        inline fun build(context: Context, block: Builder.()->Unit): OptDialog = Builder(context).apply(block).build()
    }

}


