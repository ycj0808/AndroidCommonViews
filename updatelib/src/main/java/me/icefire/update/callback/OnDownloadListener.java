package me.icefire.update.callback;

/**
 * 下载监听
 *
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public interface OnDownloadListener {

    void onStart();

    void onProgress(int progress);

    void onFinish();
}
