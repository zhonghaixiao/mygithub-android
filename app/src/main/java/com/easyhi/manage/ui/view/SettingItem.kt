package com.easyhi.manage.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import com.easyhi.manage.R
import com.easyhi.manage.databinding.LayoutButtonCountBinding
import com.easyhi.manage.databinding.LayoutSettingItemBinding
import java.util.*

class SettingItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding: LayoutSettingItemBinding
    private var itemTitle: String = ""
    private var itemContent: String = ""
    private var showOptButton: Boolean = false
    private var optButtonText: String = ""
    private var btnColor: Int = Color.BLACK
    private var onBtnClick: (()->Unit)? = null

    init {
        binding = LayoutSettingItemBinding.inflate(LayoutInflater.from(context))
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(binding.root, params)

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItem)
        itemTitle = typeArray.getString(R.styleable.SettingItem_itemTitle) ?: ""
        itemContent = typeArray.getString(R.styleable.SettingItem_itemContent) ?: ""
        showOptButton = typeArray.getBoolean(R.styleable.SettingItem_showOptButton, false)
        optButtonText = typeArray.getString(R.styleable.SettingItem_optButtonText) ?: ""
        btnColor = typeArray.getColor(R.styleable.SettingItem_btnColor, Color.WHITE)
        typeArray.recycle()
        initView()
    }

    private fun initView() {
        binding.txtTitle.text = itemTitle
        binding.txtContent.text = itemContent
        if (showOptButton) {
            binding.slOperation.visibility = View.VISIBLE
            binding.txtOperation.text = optButtonText
            binding.slOperation.setStrokeColor(btnColor)
            binding.slOperation.setLayoutBackground(
                Color.argb(
                    0x1a,
                    Color.red(btnColor),
                    Color.green(btnColor),
                    Color.blue(btnColor)
                )
            )
            binding.slOperation.setLayoutBackgroundTrue(Color.argb(
                0x4d,
                Color.red(btnColor),
                Color.green(btnColor),
                Color.blue(btnColor)
            ))
            binding.txtOperation.setTextColor(btnColor)
            binding.slOperation.setOnClickListener {
                onBtnClick?.invoke()
            }
        } else {
            binding.slOperation.visibility = View.INVISIBLE
        }
    }

    fun setContent(text: String) {
        binding.txtContent.text = text
    }

    fun getContent(): String{
        return binding.txtContent.text.toString()
    }

    fun setOnBtnClick(clickListener: ()->Unit){
        this.onBtnClick = clickListener
    }

}




