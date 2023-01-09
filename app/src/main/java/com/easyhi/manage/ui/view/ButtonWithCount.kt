package com.easyhi.manage.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.easyhi.manage.R
import com.easyhi.manage.databinding.LayoutButtonCountBinding
import com.easyhi.manage.databinding.LayoutInputWithErrorBinding
import com.easyhi.manage.util.DisplayUtil
import com.easyhi.manage.util.gone
import com.easyhi.manage.util.visible
import java.util.*
import kotlin.concurrent.timerTask

class ButtonWithCount @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding: LayoutButtonCountBinding
    private var state: ButtonState = ButtonState.INIT
    private var buttonText: String = ""

    init {
        binding = LayoutButtonCountBinding.inflate(LayoutInflater.from(context))
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(binding.root, params)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonWithCount)
        buttonText = typedArray.getString(R.styleable.ButtonWithCount_buttonText) ?: "验证"
        typedArray.recycle()
        initView()
    }

    private fun initView() {
        setBtnState(state)
        binding.txtCount.text = buttonText
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        super.onTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }

    fun setBtnClickable(clickable: Boolean) {
        binding.root.isClickable = clickable
    }

    @SuppressLint("SetTextI18n")
    fun updateCountText(text: String) {
        binding.txtCount.text = text
    }

    fun getBtnState(): ButtonState {
        return state
    }

    fun setBtnState(state: ButtonState) {
        this.state = state
        if (state == ButtonState.COUNTING) {
            binding.root.setLayoutBackground(Color.parseColor("#d8d8d8"))
            binding.root.setGradientColor(
                0,
                Color.parseColor("#d8d8d8"),
                Color.parseColor("#d8d8d8")
            )
            binding.root.setShadowColor(Color.TRANSPARENT)
        } else {
            binding.root.setGradientColor(
                45,
                Color.parseColor("#49bbf3"),
                Color.parseColor("#2386e3")
            )
            binding.root.setShadowColor(Color.parseColor("#262386e3"))
            binding.txtCount.text = buttonText
        }

    }

    enum class ButtonState {
        INIT, COUNTING
    }

}




