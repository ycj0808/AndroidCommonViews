package me.icefire.update;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import me.icefire.update.callback.ICheckAgent;
import me.icefire.update.callback.IDownloadAgent;
import me.icefire.update.callback.IUpdateAgent;
import me.icefire.update.callback.IUpdateChecker;
import me.icefire.update.callback.IUpdateDownloader;
import me.icefire.update.callback.IUpdateParser;
import me.icefire.update.callback.IUpdatePrompter;
import me.icefire.update.callback.OnDownloadListener;
import me.icefire.update.callback.OnFailureListener;
import me.icefire.update.constant.Constant;
import me.icefire.update.error.UpdateError;
import me.icefire.update.listener.DefaultPromptClickListener;
import me.icefire.update.log.ULog;
import me.icefire.update.model.UpdateModel;
import me.icefire.update.utils.CryptUtil;
import me.icefire.update.utils.SpUtil;
import me.icefire.update.utils.Util;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public class UpdateAgent implements ICheckAgent, IUpdateAgent, IDownloadAgent {

    private Context mContext;
    private String mUrl;
    private File mTmpFile;
    private File mApkFile;
    private boolean mIsManual = false;
    private boolean mIsWifiOnly = false;

    private UpdateModel updateModel = null;
    private UpdateError mError = null;

    private IUpdateDownloader mDownloader;
    private IUpdatePrompter mPrompter;
    private IUpdateChecker mChecker;
    private IUpdateParser mParse=new DefaultUpdateParse();

    private OnFailureListener mOnFailureListener;
    private OnDownloadListener mOnDownloadListener;
    private OnDownloadListener mOnNotificationDownloadListener;

    public UpdateAgent(Context context, String url, boolean isManual, boolean isWifiOnly, int notifyId) {
        mContext = context.getApplicationContext();
        mUrl = url;
        mIsManual = isManual;
        mIsWifiOnly = isWifiOnly;
        mDownloader=new DefaultUpdateDownloader(mContext);
        mPrompter=new DefaultUpdatePrompter(context);
        mOnFailureListener=new DefaultFailureListener(context);
        mOnDownloadListener=new DefaultDialogDownloadListener(context);
        if (notifyId>0){
            mOnNotificationDownloadListener=new DefaultNotificationDownloadListener(context,notifyId);
        }else {
            mOnNotificationDownloadListener=new DefaultDialogDownloadListener(context);
        }
    }


    public void setDownloader(IUpdateDownloader mDownloader) {
        this.mDownloader = mDownloader;
    }

    public void setParse(IUpdateParser parse){
       this.mParse=parse;
    }

    public void setPrompter(IUpdatePrompter mPrompter) {
        this.mPrompter = mPrompter;
    }

    public void setChecker(IUpdateChecker mChecker) {
        this.mChecker = mChecker;
    }

    public void setOnFailureListener(OnFailureListener listener) {
        this.mOnFailureListener = listener;
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        this.mOnDownloadListener = listener;
    }

    public void setOnNotificationDownloadListener(OnDownloadListener listener) {
        this.mOnNotificationDownloadListener = listener;
    }

    @Override
    public void setInfo(String info) {
        try {
            ULog.logD("返回数据："+info);
            updateModel=mParse.parse(info);
        } catch (Exception e) {
            ULog.logE(e.getMessage());
            setError(new UpdateError(UpdateError.CHECK_PARSE));
        }
    }

    public void setInfo(UpdateModel model){
        updateModel=model;
    }

    @Override
    public void setError(UpdateError updateError) {
        this.mError=updateError;
    }

    @Override
    public UpdateModel getUpdateInfo() {
        return updateModel;
    }

    @Override
    public void update() {
        mApkFile=new File(mContext.getExternalCacheDir(), CryptUtil.md5(updateModel.getVersionName())+".apk");
        if (Util.verify(mApkFile,CryptUtil.md5(updateModel.getVersionName()))){
            //已经存在
            doInstall();
        }else{
            //下载
            doDownload();
        }
    }

    @Override
    public void ignore() {
        SpUtil.putString(mContext, Constant.KEY_IGNORE,CryptUtil.md5(updateModel.getVersionName()));
    }

    @Override
    public void onStart() {
        if (updateModel.isSilent()){
            mOnNotificationDownloadListener.onStart();
        }else{
            mOnDownloadListener.onStart();
        }
    }

    @Override
    public void onProgress(int progress) {
        if (updateModel.isSilent()){
            mOnNotificationDownloadListener.onProgress(progress);
        }else{
            mOnDownloadListener.onProgress(progress);
        }
    }

    @Override
    public void onFinish() {
        if (updateModel.isSilent()){
            mOnNotificationDownloadListener.onFinish();
        }else{
            mOnDownloadListener.onFinish();
        }

        if (mError!=null){
            mOnFailureListener.onFailure(mError);
        }else{
            mTmpFile.renameTo(mApkFile);
            if (updateModel.isAutoInstall()){
                doInstall();
            }
        }
    }

    /**
     * 检测
     */
    public void check(){
        ULog.logI("check");
        if (mIsWifiOnly){
            if (Util.checkWifi(mContext)){
                doCheck();
            }else{
                doFailure(new UpdateError(UpdateError.CHECK_NO_WIFI));
            }
        }else{
            if (Util.checkNetWork(mContext)){
                doCheck();
            }else{
                doFailure(new UpdateError(UpdateError.CHECK_NO_NETWORK));
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    void doCheck(){
        new AsyncTask<String,Void,Void>(){
            @Override
            protected Void doInBackground(String... strings) {
                if (mChecker==null){
                    mChecker=new UpdateChecker();
                }
                mChecker.check(UpdateAgent.this,mUrl);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
               doCheckFinish();
            }
        }.execute();
    }

    void doCheckFinish(){
        ULog.logI("check finish");
        UpdateError error=mError;
        if (error!=null){
            doFailure(error);
        }else{
            UpdateModel updateModel=getUpdateInfo();
            if (updateModel==null){
                doFailure(new UpdateError(UpdateError.CHECK_UNKNOWN));
            }else if(!updateModel.isHasUpdate()){
                doFailure(new UpdateError(UpdateError.UPDATE_NO_NEWER));
            }else if(isIgnore(CryptUtil.md5(updateModel.getVersionName()))){
                doFailure(new UpdateError(UpdateError.UPDATE_IGNORED));
            }else{
                String md5=CryptUtil.md5(updateModel.getVersionName());
                ULog.logI("update md5"+md5);
                Util.ensureExternalCacheDir(mContext);
                Util.setUpdate(mContext,md5);
                mTmpFile = new File(mContext.getExternalCacheDir(), md5);
                mApkFile = new File(mContext.getExternalCacheDir(), md5 + ".apk");
                if (Util.verify(mApkFile,md5)){
                    doInstall();
                }else if(updateModel.isSilent()){
                    doDownload();
                }else {
                    doPrompt();
                }
            }
        }
    }


    void doPrompt(){
        mPrompter.prompt(this);
    }

    void doDownload(){
        mDownloader.download(this,updateModel.getApkUrl(),mTmpFile);
    }

    void doInstall(){
        Util.install(mContext,mApkFile,updateModel.isForce());
    }

    void doFailure(UpdateError updateError){
        if (mIsManual||updateError.isError()){
            mOnFailureListener.onFailure(updateError);
        }
    }

    private boolean isIgnore(String md5){
        return !TextUtils.isEmpty(md5)&&md5.equals(SpUtil.getString(mContext,Constant.KEY_IGNORE,""));
    }

    /**
     * 默认下载
     */
    private static class DefaultUpdateDownloader implements IUpdateDownloader {
        final Context mContext;
        public DefaultUpdateDownloader(Context context) {
            mContext = context;
        }
        @Override
        public void download(IDownloadAgent agent, String url, File temp) {
            new UpdateDownloader(agent,mContext,url,temp).execute();
        }
    }

    private static class DefaultFailureListener implements OnFailureListener {
        private Context mContext;
        public DefaultFailureListener(Context context){
            this.mContext=context;
        }
        @Override
        public void onFailure(UpdateError updateError) {
            ULog.logE(updateError.toString());
            Toast.makeText(mContext,updateError.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private static class DefaultUpdatePrompter implements IUpdatePrompter {
        private Context mContext;
        public DefaultUpdatePrompter(Context context) {
            mContext = context;
        }
        @Override
        public void prompt(IUpdateAgent agent) {
            if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
                return;
            }
            final UpdateModel updateModel=agent.getUpdateInfo();
            String size= Formatter.formatFileSize(mContext,updateModel.getSize());
            String content=updateModel.getUpdateContent();
            final AlertDialog dialog=new AlertDialog.Builder(mContext).create();
            dialog.setTitle("应用更新");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            float density=mContext.getResources().getDisplayMetrics().density;
            TextView tv=new TextView(mContext);
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setVerticalScrollBarEnabled(true);
            tv.setTextSize(14);
            tv.setMaxHeight((int) (250*density));
            dialog.setView(tv,(int)(25*density),(int)(15*density),(int)(25*density),0);
            DialogInterface.OnClickListener listener=new DefaultPromptClickListener(agent,true);
            if (updateModel.isForce()){
                tv.setText("您需要更新应用才能继续使用\n\n" + content);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", listener);
            }else{
                tv.setText(content);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即更新", listener);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "以后再说", listener);
                if (updateModel.isIgnorable()) {
                    dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "忽略该版", listener);
                }
            }
            dialog.show();
        }
    }


    private static class DefaultDialogDownloadListener implements OnDownloadListener {
        private Context mContext;
        private ProgressDialog mDialog;

        public DefaultDialogDownloadListener(Context context) {
            mContext = context;
        }
        @Override
        public void onStart() {
            if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                ProgressDialog dialog=new ProgressDialog(mContext);
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog.setMessage("下载中...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(false);
                dialog.show();
                mDialog = dialog;
            }
        }
        @Override
        public void onProgress(int progress) {
            if (mDialog!=null){
                mDialog.setProgress(progress);
            }
        }

        @Override
        public void onFinish() {
            if (mDialog!=null){
                mDialog.dismiss();
                mDialog=null;
            }
        }
    }

    private static class DefaultNotificationDownloadListener implements OnDownloadListener{
        private Context mContext;
        private int mNotifyId;
        private NotificationCompat.Builder mBuilder;
        NotificationManager mManager;
        public DefaultNotificationDownloadListener(Context context, int notifyId) {
            mContext = context;
            mNotifyId = notifyId;
            mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel();//android8.0以上需要特殊处理
            }
        }

        @Override
        public void onStart() {
            if (mBuilder==null){
                String title = "下载中 - " + mContext.getString(mContext.getApplicationInfo().labelRes);
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                    mBuilder=new NotificationCompat.Builder(mContext,"default");
                }else{
                    mBuilder=new NotificationCompat.Builder(mContext);
                }
                mBuilder.setOngoing(true)
                        .setAutoCancel(false)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(mContext.getApplicationInfo().icon)
                        .setTicker(title)
                        .setContentTitle(title);
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
                    mBuilder.setPriority(Notification.PRIORITY_MAX);
                }
            }
            onProgress(0);
        }


        @Override
        public void onProgress(int progress) {
            if (mBuilder!=null){
                if (progress>0){
                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) {
                        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
                    }
                    mBuilder.setDefaults(0);
                }
                mBuilder.setProgress(100,progress,false);
                mManager.notify(mNotifyId,mBuilder.build());
            }
        }

        @Override
        public void onFinish() {
            mManager.cancel(mNotifyId);
        }

        @TargetApi(Build.VERSION_CODES.O)
        private void createNotificationChannel() {
            NotificationChannel channel = new NotificationChannel("default", "default_channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.canBypassDnd();//是否绕过请勿打扰模式
            channel.enableLights(true);//闪光灯
            channel.setLockscreenVisibility(android.support.v4.app.NotificationCompat.VISIBILITY_SECRET);//锁屏显示通知
            channel.setLightColor(Color.RED);
            channel.canShowBadge();//桌面launcher的消息角标
            channel.enableVibration(true);
            channel.getAudioAttributes();
            channel.getGroup();//获取通知取到组
            channel.setBypassDnd(true);//设置可绕过，请勿打扰模式
            channel.setVibrationPattern(new long[]{100, 100, 200});
            channel.shouldShowLights();//是否会有灯光
            mManager.createNotificationChannel(channel);
        }
    }

    private static class DefaultUpdateParse implements IUpdateParser{
        @Override
        public UpdateModel parse(String source) throws Exception {
            return UpdateModel.parse(source);
        }
    }
}
