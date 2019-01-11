package me.icefire.adapter.rv.utils;

import android.content.Context;

/**
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/7
 */
public class Utils {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context,float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
