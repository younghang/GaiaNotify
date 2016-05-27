package com.young.gaianotify.Network;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import com.android.volley.toolbox.StringRequest;
import com.young.gaianotify.MyApplication;

import java.util.Map;

/**
 * Created by young on 2016/3/26 0026.
 */
public  class VolleyRequest {
   public static void RequestJsonGet(Context mcontext, String url, String tag, VolleyMethodManage vmm) {
        MyApplication.getHttpQueues().cancelAll(tag);//取消请求 避免重复
        request=new JsonObjectRequest(Request.Method.GET,url,sendjsonobject,vmm.loadingListener(), vmm.errorListener());
        request.setTag(tag);
        MyApplication.getHttpQueues().add(request);
    }
    public static void RequestStringGet( String url, String tag, VolleyMethodManage  vmm) {
        MyApplication.getHttpQueues().cancelAll(tag);//取消请求 避免重复
        stringRequest=new StringRequest(Request.Method.GET,url,vmm.loadingListener(), vmm.errorListener());
        stringRequest.setTag(tag);
        MyApplication.getHttpQueues().add(stringRequest);
    }


    public static void RequstImageGet(String url, String tag,VolleyMethodManage volleyMethodManage) {
        MyApplication.getHttpQueues().cancelAll(tag);//取消请求 避免重复
        imageRequest=new ImageRequest( url ,volleyMethodManage.loadingListener(),0,0, ImageView.ScaleType.FIT_CENTER,Bitmap.Config.RGB_565, volleyMethodManage.errorListener());
        imageRequest.setTag(tag);
        MyApplication.getHttpQueues().add(imageRequest);

    }

    static void RequestPost(Context mcontext, String url, String tag, final Map<String, String> maps, VolleyMethodManage vmm) {

        request=new JsonObjectRequest(Request.Method.POST,url,sendjsonobject,vmm.loadingListener(),vmm.errorListener())
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return  maps;
            }
        };
        request.setTag(tag);
        MyApplication.getHttpQueues().add(request);
    }

    public static final String jsongettag = "jsonrequest";
    public static final String picgettag = "picrequest";
    public static final String stringgettag = "stringrequest";

    static JsonRequest request;
    static ImageRequest imageRequest;
    static StringRequest stringRequest;
    static org.json.JSONObject sendjsonobject=new org.json.JSONObject ();
    static Context context;





}
