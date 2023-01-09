package com.easyhi.manage.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easyhi.manage.BuildConfig
import com.easyhi.manage.MyApplication
import com.tencent.smtt.sdk.CookieManager

fun withDebug(callback: ()->Unit) {
    if (BuildConfig.DEBUG) {
        callback()
    }
}

fun clearAuthInfoIfNeed(){
    val clearAuthWhenExit = MyApplication.sp.getBoolean(KEY_CLEAR_COOKIE_WHEN_EXIT, false)
    if (clearAuthWhenExit) {
        CookieManager.getInstance().removeAllCookies(null)
        MyApplication.sp.edit().remove(KEY_CURRENT_URL)
    }
}

private tailrec fun getCompatActivity(context: Context?): AppCompatActivity? {
    return when (context) {
        is AppCompatActivity -> context
        is androidx.appcompat.view.ContextThemeWrapper -> getCompatActivity(context.baseContext)
        is android.view.ContextThemeWrapper -> getCompatActivity(context.baseContext)
        else -> null
    }
}

val View.activity: AppCompatActivity?
    get() = getCompatActivity(context)

fun View.disableAutoFill() = run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
    }
}

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.visible(visible: Boolean) {
    if (visible && visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    } else if (!visible && visibility == View.VISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.screenshot(): Bitmap? {
    return runCatching {
        val screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(screenshot)
        c.translate(-scrollX.toFloat(), -scrollY.toFloat())
        draw(c)
        screenshot
    }.getOrNull()
}

fun SeekBar.progressAdd(int: Int) {
    progress += int
}

fun RadioGroup.getIndexById(id: Int): Int {
    for (i in 0 until this.childCount) {
        if (id == get(i).id) {
            return i
        }
    }
    return 0
}

fun RadioGroup.getCheckedIndex(): Int {
    for (i in 0 until this.childCount) {
        if (checkedRadioButtonId == get(i).id) {
            return i
        }
    }
    return 0
}

fun RadioGroup.checkByIndex(index: Int) {
    check(get(index).id)
}


fun GridLayoutManager.configSingleViewSpan(range: (position: Int) -> Boolean) {
    val oldSpanSizeLookup = spanSizeLookup
    val oldSpanCount = spanCount

    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (range(position)) oldSpanCount else oldSpanSizeLookup.getSpanSize(position)
        }
    }
}

fun ConcatAdapter.getAdapterByItemPosition(position: Int): RecyclerView.Adapter<out RecyclerView.ViewHolder>? {
    var pos = position
    val adapters = adapters
    for (adapter in adapters) {
        when {
            pos >= adapter.itemCount -> {
                pos -= adapter.itemCount
            }
            pos < 0 -> return null
            else -> return adapter
        }
    }
    return null
}

fun View.fadeOut(delayTime: Long = 200) {
    if (visibility != View.VISIBLE) return
    isEnabled = false
    val animation: Animation = AlphaAnimation(1f, 0f)
    animation.duration = delayTime
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            visibility = View.GONE
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }
    })
    startAnimation(animation)
}

fun View.fadeIn(delayTime: Long = 200) {
    if (visibility == View.VISIBLE) return
    val animation: Animation = AlphaAnimation(0f, 1f)
    animation.duration = delayTime
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            isEnabled = true
        }

        override fun onAnimationRepeat(animation: Animation) {}
    })
    startAnimation(animation)
    visibility = View.VISIBLE
}

//fun Dialog.showDialogFromBottom(context: Activity) {
//    show()
//    window?.setBackgroundDrawableResource(android.R.color.transparent)
//    window?.setWindowAnimations(R.style.BottomInOutDialogStyle) //添加动画
//    // 设置宽度为屏宽、靠近屏幕底部。
//    window?.attributes?.gravity = Gravity.BOTTOM
//    window?.attributes?.width = context.windowManager.defaultDisplay.width
//    window?.clearFlags(
//        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
//                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
//    )
//    window?.setSoftInputMode(
//        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
//    )
//}

fun String.parseColor(defaultColor: Int): Int {
    return try {
        Color.parseColor(this)
    } catch (e: Exception) {
        return defaultColor
    }
}

fun String.parseColor(defaultColor: String): Int {
    return try {
        Color.parseColor(this)
    } catch (e: Exception) {
        Color.parseColor(defaultColor)
    }
}




