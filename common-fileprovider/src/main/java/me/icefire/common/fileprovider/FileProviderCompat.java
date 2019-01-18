package me.icefire.common.fileprovider;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.List;

/**
 * 适配Android7系统文件下载与存储
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/18
 */
public class FileProviderCompat {

    /**
     * 获取文件存储路径适配Android7以上系统
     * param context
     * param file
     * return
     */
    public static Uri getUriForFile(Context context,File file){
        Uri fileUri=null;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            fileUri=getUriForFile24(context,file);
        }else{
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * 适配Android7系统获取文件存储路径
     * param context
     * param file
     * return
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Uri getUriForFile24(Context context,File file){
        Uri fileUri = android.support.v4.content.FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".android7.fileprovider",
                file);
        return fileUri;
    }

    /**
     * 设置文件打开类型
     * param context
     * param intent
     * param type
     * param file
     * param writeable
     */
    public static void setIntentDataAndType(Context context, Intent intent,String type,File file,boolean writeable){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeable){
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }else{
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

    /**
     * 设置文件
     * param context
     * param intent
     * param file
     * param writeable
     */
    public static void setIntentData(Context context, Intent intent,File file,boolean writeable){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            intent.setData(getUriForFile(context,file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeable){
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }else{
            intent.setData(Uri.fromFile(file));
        }
    }

    /**
     * 授予读写权限
     * param context
     * param intent
     * param uri
     * param writeable
     */
    public static void grantPermissions(Context context, Intent intent, Uri uri, boolean writeable){
        int flag=Intent.FLAG_GRANT_READ_URI_PERMISSION;
        if (writeable){
            flag|=Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
        }
        intent.addFlags(flag);
        List<ResolveInfo> resInfoList = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, flag);
        }
    }
}
