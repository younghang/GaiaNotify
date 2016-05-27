package com.young.gaianotify.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.young.gaianotify.RoomDataDefine.GaiaRoom;
import com.young.gaianotify.MyApplication;

public class CheckGaiaOnlineService extends Service {
    public CheckGaiaOnlineService() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

    @Override
    public void onDestroy() {
        Stop();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        timespan = intent.getIntExtra("timespan", 1);
        Start();
        return super.onStartCommand(intent, flags, startId);
    }
    GaiaRoom gaiaRoom = new GaiaRoom();

    public void Start( ) {

        Log.i("My", "start");
        handler.postDelayed(runnable, timespan * 1000*60);//每两秒执行一次runnable.
    }

    public void Stop() {
        handler.removeCallbacks(runnable);

        Log.i("My", "停止调用");
    }

    static  final String TAG_HTTPGET="My";
    Intent intent = new Intent();

    void VolleyGet() {
//        101572_rEpCS    gaia  52320  maomao 74160  xianamiao 67101
//        String url = "http://www.zhanqi.tv/api/static/live.roomid/102152.json";
//        String url = "http://www.zhanqi.tv/api/static/live.roomid/52320.json";
        String url = "http://www.zhanqi.tv/api/static/live.roomid/52320.json";
        MyApplication.getHttpQueues().cancelAll("getStatus");
        final org.json.JSONObject jsonRequest = new org.json.JSONObject();
        Log.i("My", "进入MyService");

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

                    intent.putExtra("title", gaiaRoom.data.title);
                    intent.putExtra("time", gaiaRoom.data.editTime);
                    intent.putExtra("imagrurl", gaiaRoom.data.bpic);

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
                error.printStackTrace();
                intent.setAction("android.intent.action.online");//action与接收器相同
                getApplicationContext().sendBroadcast(intent);


            }
        });
request.setRetryPolicy(new DefaultRetryPolicy(5000,
      3,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("getStatus");
        MyApplication.getHttpQueues().add(request);

    }

    private void SendMessage() {
        Log.i("My", "发广播");
        sendBroadcast(intent);
    }



    private static final int GAIA_NOTIFY = 666;
    NotificationManager notificationManager;
}
