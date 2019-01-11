package me.icefire.common;

import android.app.Application;

import me.icefire.common.toast.ToastCompat;

/**
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/9
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastCompat.init(this);
    }
}
