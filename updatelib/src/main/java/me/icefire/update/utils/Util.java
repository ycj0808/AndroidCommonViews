package me.icefire.update.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import me.icefire.update.constant.Constant;
import me.icefire.update.log.ULog;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/15
 */
public class Util {

    /**
     * 网络请求流转字符串
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static String readString(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            output.flush();
        } finally {
            close(input);
            close(output);
        }
        return output.toString("UTF-8");
    }

    /**
     * 关闭流链接
     *
     * @param closeable
     */
    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检测网络连接
     *
     * @param context
     * return
     */
    public static boolean checkNetWork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean checkWifi(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info!=null&& info.isConnected()&&info.getType()==ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 检测文件是否存在
     * @param apk
     * @param md5
     * @return
     */
    public static boolean verify(File apk,String md5){
        if (!apk.exists()){
            return false;
        }
        return true;
    }

    /**
     * 应用安装
     * @param context
     * @param file
     * @param force
     */
    public static void install(Context context,File file,boolean force){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }else{
            Uri uri= FileProvider.getUriForFile(context,context.getPackageName()+"updatefileprovider",file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (force){
            System.exit(0);
        }
    }

    /**
     * 安装应用
     * @param context
     * @param force
     */
    public static void install(Context context,boolean force){
        String md5=SpUtil.getString(context, Constant.KEY_IGNORE,"");
        File apk=new File(context.getExternalCacheDir(),md5+".apk");
        if (Util.verify(apk,md5)){
            install(context,apk,force);
        }
    }

    public static void ensureExternalCacheDir(Context context){
        File file=context.getExternalCacheDir();
        if (file==null){
            file=new File(context.getExternalFilesDir("").getParentFile(),"cache");
        }
        if (file!=null){
            file.mkdirs();
        }
    }

    /**
     * @param context
     * @param md5
     */
    public static void setUpdate(Context context,String md5){
        if (TextUtils.isEmpty(md5)){
            return;
        }
        String old=SpUtil.getString(context,Constant.KEY_UPDATE,"");
        if (md5.equals(old)){
            ULog.logI("same md5");
            return;
        }

        File oldFile=new File(context.getExternalCacheDir(),old);
        if (oldFile.exists()){
            oldFile.delete();
        }

        SpUtil.putString(context,Constant.KEY_UPDATE,md5);
        File file=new File(context.getExternalCacheDir(),md5);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清理已下载的apk
     * @param context
     */
    public static void clean(Context context){
        String md5=SpUtil.getString(context,Constant.KEY_UPDATE,"");
        if (!TextUtils.isEmpty(md5)){
            File file=new File(context.getExternalCacheDir(),md5+".apk");
            if (file.exists()){
                file.delete();
            }
            SpUtil.putString(context,Constant.KEY_UPDATE,"");
        }
    }
}
