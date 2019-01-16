package me.icefire.update;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.nio.charset.Charset;
import me.icefire.update.callback.IUpdateChecker;
import me.icefire.update.callback.IUpdateDownloader;
import me.icefire.update.callback.IUpdateParser;
import me.icefire.update.callback.IUpdatePrompter;
import me.icefire.update.callback.OnDownloadListener;
import me.icefire.update.callback.OnFailureListener;
import me.icefire.update.log.ULog;
import me.icefire.update.utils.Util;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/16
 */
public class UpdateManager {

    private static boolean mIsWifiOnly = true;

    public static void setWifiOnly(boolean mIsWifiOnly) {
        UpdateManager.mIsWifiOnly = mIsWifiOnly;
    }

    public static void setDebuggable(boolean flag) {
        ULog.setDebug(flag);
    }

    public static void install(Context context) {
        Util.install(context, true);
    }

    public static void check(Context context) {
        create(context).check();
    }

    public static void checkManual(Context context){
        create(context).setManual(true).check();
    }

    /**
     * 构建Builder
     * @param context
     * @return
     */
    public static Builder create(Context context){
        Util.ensureExternalCacheDir(context);
        return new Builder(context).setWifiOnly(mIsWifiOnly);
    }

    public static class Builder {
        private Context mContext;
        private String mUrl;
        private byte[] mPostData;
        private boolean mIsManual;
        private boolean mIsWifiOnly;
        private int mNotifyId = 0;

        private OnDownloadListener mOnNotificationDownloadListener;
        private OnDownloadListener mOnDownloadListener;
        private IUpdatePrompter mPrompter;
        private OnFailureListener mOnFailureListener;

        private IUpdateParser mParse;
        private IUpdateChecker mChecker;
        private IUpdateDownloader mDownloader;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setUrl(String url) {
            mUrl = url;
            return this;
        }

        public Builder setPostData(@NonNull byte[] data) {
            mPostData = data;
            return this;
        }

        public Builder setPostData(@NonNull String data) {
            mPostData = data.getBytes(Charset.forName("UTF-8"));
            return this;
        }

        public Builder setNotifyId(int notifyId) {
            mNotifyId = notifyId;
            return this;
        }

        public Builder setManual(boolean isManual) {
            mIsManual = isManual;
            return this;
        }

        public Builder setWifiOnly(boolean isWifiOnly) {
            mIsWifiOnly = isWifiOnly;
            return this;
        }

        public Builder setParser(@NonNull IUpdateParser parser) {
            mParse = parser;
            return this;
        }

        public Builder setChecker(@NonNull IUpdateChecker checker) {
            mChecker = checker;
            return this;
        }

        public Builder setDownloader(@NonNull IUpdateDownloader downloader) {
            mDownloader = downloader;
            return this;
        }

        public Builder setPrompter(@NonNull IUpdatePrompter prompter) {
            mPrompter = prompter;
            return this;
        }

        public Builder setOnNotificationDownloadListener(OnDownloadListener mOnNotificationDownloadListener) {
            this.mOnNotificationDownloadListener = mOnNotificationDownloadListener;
            return this;
        }

        public Builder setOnDownloadListener(OnDownloadListener mOnDownloadListener) {
            this.mOnDownloadListener = mOnDownloadListener;
            return this;
        }

        public Builder setOnFailureListener(OnFailureListener mOnFailureListener) {
            this.mOnFailureListener = mOnFailureListener;
            return this;
        }

        private static long sLastTime;

        public void check() {
            long now = System.currentTimeMillis();
            if (now - sLastTime < 3000) {
                return;
            }
            sLastTime = now;
            if (TextUtils.isEmpty(mUrl)) {
                throw new IllegalArgumentException("url cann't empty");
            }

            UpdateAgent agent = new UpdateAgent(mContext, mUrl, mIsManual, mIsWifiOnly, mNotifyId);
            if (mOnNotificationDownloadListener != null) {
                agent.setOnNotificationDownloadListener(mOnNotificationDownloadListener);
            }

            if (mOnDownloadListener != null) {
                agent.setOnDownloadListener(mOnDownloadListener);
            }

            if (mOnFailureListener != null) {
                agent.setOnFailureListener(mOnFailureListener);
            }

            if (mChecker != null) {
                agent.setChecker(mChecker);
            } else {
                agent.setChecker(new UpdateChecker(mPostData));
            }

            if (mDownloader != null) {
                agent.setDownloader(mDownloader);
            }

            if (mParse!=null){
                agent.setParse(mParse);
            }

            if (mPrompter != null) {
                agent.setPrompter(mPrompter);
            }
            agent.check();
        }
    }
}
