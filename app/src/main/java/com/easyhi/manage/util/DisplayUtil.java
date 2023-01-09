package com.easyhi.manage.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

/**
 * dp,sp 和 px 转换的辅助类
 */
public class DisplayUtil {

    private static final String TAG = "DisplayUtil";

    private DisplayUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     * DisplayMetrics类中属性density
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     * DisplayMetrics类中属性density
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * DisplayMetrics类中属性scaledDensity
     */
    public static float px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return  (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * DisplayMetrics类中属性scaledDensity
     */
    public static float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }

    /**
     * 保持字体大小不随系统设置变化（用在界面加载之前）
     * 要重写Activity的attachBaseContext()
     */
    public static Context attachBaseContext(Context context, float fontScale) {
        Configuration config = context.getResources().getConfiguration();
        Log.i(TAG, "changeActivityFontScaleA " + config.fontScale + ", " + fontScale);
        //错误写法
//        if(config.fontScale != fontScale) {
//            config.fontScale = fontScale;
//            return context.createConfigurationContext(config);
//        } else {
//            return context;
//        }
        //正确写法
        config.fontScale = fontScale;
        return context.createConfigurationContext(config);
    }

    /**
     * 保持字体大小不随系统设置变化（用在界面加载之前）
     * 要重写Activity的getResources()
     */
    public static Resources getResources(Context context, Resources resources, float fontScale) {
        Configuration config = resources.getConfiguration();
        Log.i(TAG, "changeActivityFontScaleR " + config.fontScale + ", " + fontScale);
        if(config.fontScale != fontScale) {
            config.fontScale = fontScale;
            return context.createConfigurationContext(config).getResources();
        } else {
            return resources;
        }
    }

    /**
     * 保存字体大小，后通知界面重建，它会触发attachBaseContext，来改变字号
     */
    public static void recreate(Activity activity) {
//          activity.getWindow().getDecorView().requestLayout();
//          activity.getWindow().getDecorView().invalidate();
        //只有这句才有效，其它两句都无效
        activity.recreate();
    }


}
