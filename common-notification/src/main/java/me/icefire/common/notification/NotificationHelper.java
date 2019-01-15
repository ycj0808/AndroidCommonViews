package me.icefire.common.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

/**
 * 通知栏工具栏
 * author yangchj
 * email yangchj@icefire.me
 * date 2019/1/14
 */
public class NotificationHelper extends ContextWrapper {

    public static final String CHANNEL_ID = "default";
    public static final String CHANNEL_NAME = "default_channel";
    private NotificationManager mManager;
    private int[] flags;


    private boolean ongoing = false;
    private RemoteViews remoteViews = null;
    private PendingIntent intent = null;
    private String ticker = "";
    private int priority = Notification.PRIORITY_DEFAULT;
    private boolean onlyAlertOnce = false;
    private long when = 0;
    private Uri sound = null;
    private int defaults = 0;
    private long[] pattern = null;

    NotificationManagerCompat notificationManagerCompat;


    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();//android8.0以上需要特殊处理
        }

        notificationManagerCompat=NotificationManagerCompat.from(base);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
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
        getManager().createNotificationChannel(channel);
    }

    /**
     * 获取创建一个NotificationManager的对象
     *
     * @return NotificationManager对象
     */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    /**
     * 清空所有的通知
     */
    public void clearNotification() {
        getManager().cancelAll();
    }

    /**
     * 使用这个发送通知
     * @param notifyId
     * @param title
     * @param content
     * @param icon
     */
    public void sendNotification(int notifyId, String title, String content, int icon) {
        Notification build;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = getChannelNotification(title, content, icon);
            build = builder.build();
        } else {
            NotificationCompat.Builder builder = getNotificaionCompat(title, content, icon);
            build = builder.build();
        }

        if (flags != null && flags.length > 0) {
            for (int a = 0; a < flags.length; a++) {
                build.flags |= flags[a];
            }
        }
        notificationManagerCompat.notify(notifyId, build);
    }

    /**
     * 获取Notification
     *
     * @param title
     * @param content
     * @param icon
     * @return
     */
    public Notification getNotification(String title, String content, int icon) {
        Notification build;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder = getChannelNotification(title, content, icon);
            build = builder.build();
        } else {
            NotificationCompat.Builder builder = getNotificaionCompat(title, content, icon);
            build = builder.build();
        }
        if (flags != null && flags.length > 0) {
            for (int a = 0; a < flags.length; a++) {
                build.flags |= flags[a];
            }
        }
        return build;
    }

    /**
     * 创建带有channel的Notification
     *
     * @param title
     * @param content
     * @param icon
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification.Builder getChannelNotification(String title, String content, int icon) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        Notification.Builder notificationBuilder = builder
                .setContentTitle(title)//设置标题
                .setContentText(content)//设置消息内容
                .setSmallIcon(icon)//设置通知图标
                .setOngoing(ongoing)//让通知左右滑的时候是否可以取消通知
                .setPriority(priority)//设置优先级
                .setOnlyAlertOnce(onlyAlertOnce)//是否提示一次true
                .setAutoCancel(true);
        if (remoteViews != null) {
            //设置自定义view通知栏
            notificationBuilder.setCustomContentView(remoteViews);
        }

        if (intent != null) {
            notificationBuilder.setContentIntent(intent);
        }

        if (!TextUtils.isEmpty(ticker)) {
            //设置状态栏标题
            notificationBuilder.setTicker(ticker);
        }

        if (when != 0) {
            //设置通知时间，默认为系统发出通知的时间，通常不设置
            notificationBuilder.setWhen(when);
        }

        if (sound != null) {
            //设置sound
            notificationBuilder.setSound(sound);
        }

        if (defaults != 0) {
            notificationBuilder.setDefaults(defaults);
        }
        if (pattern != null) {
            notificationBuilder.setVibrate(pattern);
        }
        return notificationBuilder;
    }

    /**
     * 创建通用的Notification
     *
     * @param title
     * @param content
     * @param icon
     * @return
     */
    private NotificationCompat.Builder getNotificaionCompat(String title, String content, int icon) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
            builder.setPriority(Notification.PRIORITY_DEFAULT);
        }
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(icon);
        builder.setPriority(priority);
        builder.setOnlyAlertOnce(onlyAlertOnce);
        builder.setOngoing(ongoing);
        if (remoteViews != null) {
            builder.setContent(remoteViews);
        }
        if (intent != null) {
            builder.setContentIntent(intent);
        }
        if (ticker != null && ticker.length() > 0) {
            builder.setTicker(ticker);
        }
        if (when != 0) {
            builder.setWhen(when);
        }
        if (sound != null) {
            builder.setSound(sound);
        }
        if (defaults != 0) {
            builder.setDefaults(defaults);
        }
        //点击自动删除
        builder.setAutoCancel(true);
        return builder;
    }

    /**
     * 让通知滑懂得时候是否可以取消通知
     *
     * @param ongoing
     * @return
     */
    public NotificationHelper setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
        return this;
    }

    /**
     * 设置自定义View通知栏布局
     *
     * @param remoteViews
     * @return
     */
    public NotificationHelper setContent(RemoteViews remoteViews) {
        this.remoteViews = remoteViews;
        return this;
    }

    /**
     * 设置内容点击动作
     *
     * @param intent
     * @return
     */
    public NotificationHelper setContentIntent(PendingIntent intent) {
        this.intent = intent;
        return this;
    }

    /**
     * 设置状态栏标题
     *
     * @param ticker
     * @return
     */
    public NotificationHelper setTicker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    /**
     * 设置优先级
     *
     * @param priority
     * @return
     */
    public NotificationHelper setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * 是否提示一次
     *
     * @param onlyAlertOnce
     * @return
     */
    public NotificationHelper setOnlyAlertOnce(boolean onlyAlertOnce) {
        this.onlyAlertOnce = onlyAlertOnce;
        return this;
    }

    /**
     * 设置通知时间
     *
     * @param when
     * @return
     */
    public NotificationHelper setWhen(long when) {
        this.when = when;
        return this;
    }

    /**
     * 设置sound
     *
     * @param sound
     * @return
     */
    public NotificationHelper setSound(Uri sound) {
        this.sound = sound;
        return this;
    }

    /**
     * 设置默认的提示音
     *
     * @param defaults
     * @return
     */
    public NotificationHelper setDefaults(int defaults) {
        this.defaults = defaults;
        return this;
    }



    /**
     * 自定义震动效果
     *
     * @param pattern
     * @return
     */
    public NotificationHelper setVibrate(long[] pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     * 设置flag标签
     *
     * @param flags flags
     * @return
     */
    public NotificationHelper setFlags(int... flags) {
        this.flags = flags;
        return this;
    }
}
