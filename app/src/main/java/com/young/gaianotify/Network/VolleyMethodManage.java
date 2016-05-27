package com.young.gaianotify.Network;

import android.content.Context;


import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by young on 2016/3/26 0026.
 */
public abstract class VolleyMethodManage<T> {
    Context mcontext;
   private Response.Listener<T>  mlistener;
   private Response.ErrorListener merrorlistener;

    public abstract void onSuccess(T result);

    public abstract void onError(VolleyError error);

    public Response.Listener  loadingListener()
    {
        mlistener=new Response.Listener <T>() {


            @Override
            public void onResponse(T response) {
                onSuccess(response);
            }
        };
        return mlistener;
    }
    public Response.ErrorListener errorListener()
    {
        merrorlistener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onError(error);
            }
        };
        return  merrorlistener;
    }




}
