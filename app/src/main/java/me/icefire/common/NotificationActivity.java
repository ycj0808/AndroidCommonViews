package me.icefire.common;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;

import me.icefire.common.notification.NotificationHelper;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/14
 */
public class NotificationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAllNotifications();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification2();
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification3();
            }
        });
    }

    private void cancelAllNotifications(){
        NotificationHelper notificationHelper=new NotificationHelper(this);
        notificationHelper.clearNotification();
    }

    /**
     * 发送一个普通通知
     */
    private void sendNotification(){
        NotificationHelper notificationHelper=new NotificationHelper(this);
        notificationHelper.sendNotification(1,"这是一个标题","这是消息内容",R.mipmap.ic_icon_niu);
    }

    /**
     * 发送一个带有动作的通知
     */
    private void sendNotification2() {
        //处理点击Notification的逻辑
        //创建intent
        Intent resultIntent = new Intent(this, RvActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
        resultIntent.putExtra("what",2);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,2,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        // 定义Notification的各种属性
        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper
                .setContentIntent(resultPendingIntent)
                .sendNotification(2,"这个是标题2", "这个是内容2", R.mipmap.ic_icon_niu);
    }

    private void sendNotification3() {
        long[] vibrate = new long[]{0, 500, 1000, 1500};
        //处理点击Notification的逻辑
        //创建intent
        Intent resultIntent = new Intent(this, RvActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
        resultIntent.putExtra("what",3);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,3,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //发送pendingIntent

        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper
                //让通知左右滑的时候是否可以取消通知
                .setOngoing(true)
                //设置内容点击处理intent
                .setContentIntent(resultPendingIntent)
                //设置状态栏的标题
                .setTicker("来通知消息啦")
                //设置自定义view通知栏布局
                .setContent(getRemoteViews())
                //设置sound
                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)
                //设置优先级
                .setPriority(Notification.PRIORITY_DEFAULT)
                //自定义震动效果
                .setVibrate(vibrate)
                //必须设置的属性，发送通知
                .sendNotification(3,"这个是标题3", "这个是内容3", R.mipmap.ic_icon_niu);
    }

    private RemoteViews getRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_mobile_play);
        // 设置 点击通知栏的上一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_pre, getActivityPendingIntent(11));
        // 设置 点击通知栏的下一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_next, getActivityPendingIntent(12));
        // 设置 点击通知栏的播放暂停按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_start, getActivityPendingIntent(13));
        // 设置 点击通知栏的根容器时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.ll_root, getActivityPendingIntent(14));
        remoteViews.setTextViewText(R.id.tv_title, "标题");     // 设置通知栏上标题
        remoteViews.setTextViewText(R.id.tv_artist, "艺术家");   // 设置通知栏上艺术家
        return remoteViews;
    }

    /** 获取一个Activity类型的PendingIntent对象 */
    private PendingIntent getActivityPendingIntent(int what) {
        Intent intent = new Intent(this, RvActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);           //添加为栈顶Activity
        intent.putExtra("what", what);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, what, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
