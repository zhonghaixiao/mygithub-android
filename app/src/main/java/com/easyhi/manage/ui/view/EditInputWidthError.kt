package com.easyhi.manage.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.easyhi.manage.R
import com.easyhi.manage.databinding.LayoutInputWithErrorBinding
import com.easyhi.manage.util.gone
import com.easyhi.manage.util.visible

class EditInputWidthError @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding: LayoutInputWithErrorBinding

    init {
        binding = LayoutInputWithErrorBinding.inflate(LayoutInflater.from(context))
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(binding.root, params)
    }

    /**
     * @param errorText "": 清除错误状态， 非空: 边框变为红色，
     */
    fun setErrorState(errorText: String) {
        if (errorText.isNotEmpty()) {
            binding.editEasyid.setBackgroundResource(R.drawable.bg_edittext_error)
            binding.txtError.visible()
            binding.txtError.text = errorText
        } else {
            binding.editEasyid.setBackgroundResource(R.drawable.bg_edittext)
            binding.txtError.gone()
            binding.txtError.text = ""
        }

    }

    fun getEdit(): EditText {
        return binding.editEasyid
    }

}




