package com.young.gaianotify;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by young on 2016/2/17 0017.
 */
public class MyApplication  extends Application{

    public  static RequestQueue queues;
     @Override
     public void onCreate() {
         super.onCreate();
         queues = Volley.newRequestQueue(getApplicationContext());
     }

    public static RequestQueue   getHttpQueues()
    {
        return queues;
    }


}
