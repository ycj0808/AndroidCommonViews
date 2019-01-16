package me.icefire.update.log;

import android.util.Log;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public class ULog {

    private static final String TAG="icefire.me.update";

    static boolean debug=true;

    public static void logD(String content){
        if (debug){
            Log.d(TAG,content);
        }
    }

    public static void logE(String content){
        if (debug){
            Log.e(TAG,content);
        }
    }

    public static void logI(String content){
        if (debug){
            Log.i(TAG,content);
        }
    }

    public static void setDebug(boolean flag) {
        ULog.debug = flag;
    }
}
