package com.young.gaianotify.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.young.gaianotify.RoomDataDefine.GaiaRoom;
import com.young.gaianotify.MyApplication;
import com.young.gaianotify.OnProgressListener;
import com.young.gaianotify.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class CheckOnlineService extends Service {
    public CheckOnlineService() {
    }

    @Override
    public void onCreate() {

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        super.onCreate();
    }

    //需用内部类继承Binder,并定义方法获取Service对象
    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public CheckOnlineService getServise() {
            return CheckOnlineService.this;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    Handler handler = new Handler();
    int timespan=1;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情

                VolleyGet();

            handler.postDelayed(this, timespan * 1000*60);
        }
    };
static  final String TAG_HTTPGET="My";
    private void GetStatus() throws IOException {
        String url = "http://www.zhanqi.tv/api/static/live.roomid/73590.json?";

        // 新建HttpGet对象
        HttpGet httpGet = new HttpGet(url);
        // 获取HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        // 获取HttpResponse实例
        HttpResponse httpResp = httpClient.execute(httpGet);
        // 判断是够请求成功
        String result="";
        if (httpResp.getStatusLine().getStatusCode() == 200) {
            // 获取返回的数据
            result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
            Log.i("My", "HttpGet方式请求成功，返回数据如下：");
            Log.i(TAG_HTTPGET, result);
        } else {
            Log.i(TAG_HTTPGET, "HttpGet方式请求失败");
        }

        gaiaRoom = JSON.parseObject(result.toString(), GaiaRoom.class);
        if (gaiaRoom.data.status.toString() == "4") {
            Log.i("My", "开播了");
            intent.putExtra("message", "开播了");

        } else
            intent.putExtra("message", "还没开播");


    }

    @Override
    public boolean onUnbind(Intent intent) {

        Log.i("My", "解绑");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        timespan = intent.getIntExtra("timespan", 1);
        Log.i("My", "bind");
        return binder;
    }

    GaiaRoom gaiaRoom = new GaiaRoom();

    public void Start(int timespans) {
        timespan=timespans;
        Log.i("My", "start");
        handler.postDelayed(runnable, timespan * 1000*60);//每两秒执行一次runnable.
    }

    public void Stop() {
        handler.removeCallbacks(runnable);
        Log.i("My", "停止调用");
    }
    /**
     * 更新进度的回调接口
     */
    public OnProgressListener onProgressListener;

    /**
     * 注册回调接口的方法，供外部调用
     * @param onProgressListener
     */
    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }


    Intent intent = new Intent();

    void VolleyGet() {
//        String url = "http://www.zhanqi.tv/api/static/live.roomid/102152.json";
        String url = "http://www.zhanqi.tv/api/static/live.roomid/52320.json";
        final org.json.JSONObject jsonRequest = new org.json.JSONObject();
        JsonRequest request = new JsonObjectRequest(Request.Method.GET, url, jsonRequest, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(org.json.JSONObject response) {

                gaiaRoom = JSON.parseObject(response.toString(), GaiaRoom.class);

String message="";
                Log.i("My", "运行中");
                Log.i("My", gaiaRoom.data.status);
                if (gaiaRoom.data.status.equals("4")) {

                    message = "开播了";
                    //进度发生变化通知调用方

                    Log.i("My", "开播了");
                    Stop();
                    intent.putExtra("message", message);

//                        SendNotification();

//                    OtherWay();

                } else {
                    Log.i("My", "还没开播");
                    message = "还没开播";
                    intent.putExtra("message", message);
//                if(onProgressListener != null){
//                    onProgressListener.onProgress(message);
//                }
                }
                intent.setAction("android.intent.action.online");//action与接收器相同
                SendMessage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String string = "查询失败";
                Intent intent = new Intent("android.intent.action.online");
                intent.putExtra("message", string);
                intent.setAction("android.intent.action.online");//action与接收器相同
                getApplicationContext().sendBroadcast(intent);

                if(onProgressListener != null){
                    onProgressListener.onProgress(string);
                }
            }
        });
        request.setTag("getStatus");
        MyApplication.getHttpQueues().add(request);

    }

    private void SendMessage() {
        Log.i("My", "发广播");
        sendBroadcast(intent);
    }
    Resources res  ;

    private static final int GAIA_NOTIFY = 666;
    NotificationManager notificationManager;

    //这玩意真没用
    private void SendNotification()   {
        Log.i("My", "发送通知");
//        Intent intent1=new Intent();
//        PendingIntent pendingIntent;
//        pendingIntent = PendingIntent.getActivities(MainActivity.this, 0, new Intent[]{intent1}, 0);

        Notification.Builder builder = new Notification.Builder( this);
        res =  this.getResources();
        builder.setSmallIcon(R.drawable.gaianofity3);
//        builder.setLargeIcon(R.drawable.gaianofity2);
//        builder.setLargeIcon(BitmapFactory.decodeStream(getAssets().open("app\\src\\main\\res\\drawable\\gaianofity2.png")));
        builder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.gaianofity2));
        builder.setTicker("开播了");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Gaia");
        builder.setContentText("大哥开播了");
//        builder.setContentIntent(pendingIntent);


        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setVibrate(new long[]{500,200,500});
        Notification notification = builder.getNotification();
        notificationManager.notify(GAIA_NOTIFY, notification);


    }


    void OtherWay()
    {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notify_layout);


        views.setTextViewText(R.id.gaia_title, "开播了Gaia");
        views.setTextViewText(R.id.gaia_time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis())));
//        ImageView imageView= (ImageView) findViewById(R.id.gaia_image);
//        imageView.setImageResource(R.drawable.gaianofity2);
        views.setImageViewResource(R.id.gaia_image, R.drawable.gaianofity2);

        Notification status = new Notification();
        status.contentView = views;
        status.flags |= Notification.FLAG_ONGOING_EVENT;

//        status.flags|=Notification.DEFAULT_ALL;
        status.icon = R.drawable.gaianofity6;
        status.tickerText = "大哥开播了";

        status.defaults=Notification.DEFAULT_ALL;
        notificationManager.notify(789,status);



    }




}
