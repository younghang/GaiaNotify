package com.young.gaianotify.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.young.gaianotify.FileDeal.FileInfo;
import com.young.gaianotify.R;
import com.young.gaianotify.fragment.DownloadFragment;
import com.young.gaianotify.service.DownloadGaiaService;
import com.young.gaianotify.swipebacklayout.lib.ViewDragHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by young on 2016/5/9 0009.
 */
public class NotificationUtils {
    private Context context = null;
    private Map<Integer, Notification> mNotifications=null;
    private NotificationManager mNotificationManager=null;

    public NotificationUtils(Context context)
    {
        this.context=context;
        mNotifications = new HashMap<Integer,Notification>();
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void SendNotification(FileInfo fileInfo) {
//        if (!mNotifications.containsKey(fileInfo.getId())) {//只发送一个通知就不需要这个了

            Notification notification = new Notification();
            //设置滚动文字
            notification.tickerText=fileInfo.getFileName()+"开始下载";
            //设置显示时间
            notification.when = System.currentTimeMillis();
            //设置图标
            notification.icon = R.mipmap.ic_launcher;
            //设置通知特性
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            //设置点击通知操作
            Intent intent = new Intent(context, DownloadFragment.class);
            PendingIntent pending = PendingIntent.getActivities(context, 0,new Intent[]{intent}, 0);
            notification.contentIntent=pending;
            //创建RemoteView 对象
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.file_notification_layout);
            //设置按钮操作
            boolean start=true;
            Intent intentStart = new Intent(context, DownloadGaiaService.class);
//            intentStart.putExtra("identify",2);
//            intentStart.setFlags(6);
            intentStart.setAction(DownloadGaiaService.ACTION_START);
            Intent intentStop = new Intent(context, DownloadGaiaService.class);
//            intentStop.putExtra("identify",3);
//            intentStop.setFlags(7);
            intentStop.setAction(DownloadGaiaService.ACTION_STOP);
            Intent intentMerger = new Intent(context, DownloadGaiaService.class);
//            intentMerger.putExtra("identify",4);
//            intentMerger.setFlags(8);
            intentMerger.setAction(DownloadGaiaService.ACTION_MERGER);

            PendingIntent pendingStart=PendingIntent.getService(context,1,intentStart,0);
            PendingIntent pendingStop=PendingIntent.getService(context,1,intentStop,0);
            PendingIntent pendingMerger=PendingIntent.getService(context,1,intentMerger,0);

            remoteViews.setOnClickPendingIntent(R.id.file_start,pendingStart);
            remoteViews.setOnClickPendingIntent(R.id.file_stop, pendingStop);
            remoteViews.setOnClickPendingIntent(R.id.file_merge,pendingMerger);

            //设置文字信息
            remoteViews.setTextViewText(R.id.file_name,fileInfo.getFileName());

            notification.contentView=remoteViews;
            //发送通知
//            mNotificationManager.notify(fileInfo.getId(),notification);
            mNotificationManager.notify(DownloadGaiaService.NOTIFICATION_ID,notification);

            //把通知放入集合中
//            mNotifications.put(fileInfo.getId(), notification);
            mNotifications.put(DownloadGaiaService.NOTIFICATION_ID, notification);

//        }
    }


    public void CancleNotification(int id)
    {
        mNotificationManager.cancel(id);
//        mNotifications.remove(id);
    }
    public void UpdateNotification(int id,int progress) {
        Notification notification = mNotifications.get(id);
        if (notification != null) {
            notification.contentView.setProgressBar(R.id.progressBarFile, 100,progress, false);
            mNotificationManager.notify(id, notification);
        }
    }




}
