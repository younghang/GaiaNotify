package com.young.gaianotify.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.young.gaianotify.FileDeal.FileHelper;
import com.young.gaianotify.Network.VolleyMethodManage;
import com.young.gaianotify.Network.VolleyRequest;
import com.young.gaianotify.RoomDataDefine.GaiaRoom;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveGaiaPicService extends Service {
    public SaveGaiaPicService() {

    }

    static int timespan = 6;
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            VolleyGetPicUrl();
            handler.postDelayed(this, timespan * 1000 * 60);
        }
    };

    Map<String, String> urlImageList = new HashMap<String, String>();
    List<String> ImageUrlList = new ArrayList<String>();
    public static int failedCount = 0;
    static String url = "http://www.zhanqi.tv/api/static/live.roomid/52320.json";

    private void VolleyGetPicUrl() {
        VolleyRequest.RequestJsonGet(this, url, VolleyRequest.jsongettag, new VolleyMethodManage<JSONObject>() {

            @Override
            public void onSuccess(JSONObject result) {
                Log.i("My", "Get Pic Url");
                GaiaRoom gaiaRoom = JSON.parseObject(result.toString(), GaiaRoom.class);

                failedCount = 0;
                VolleyGetPic(gaiaRoom.data.bpic, gaiaRoom.data.editTime);

                intentImageSave.putExtra("edittime", gaiaRoom.data.editTime);
                intentImageSave.putExtra("imageurl", gaiaRoom.data.bpic);

                intentImageSave.setAction("android.intent.action.gaia_image_save");//action与接收器相同
                String imageurl = gaiaRoom.data.bpic;
                if (ImageUrlList.contains(imageurl)) {
                    return;
                } else {
                    ImageUrlList.add(gaiaRoom.data.bpic);
                    SendMessage();
                }


//                ImageLoaderGetPic(gaiaRoom.data.bpic);
            }

            @Override
            public void onError(VolleyError error) {
                failedCount++;
            }
        });
    }

    private void SendMessage() {
        Log.i("My", "发存图广播");
        sendBroadcast(intentImageSave);
    }

    void ImageLoaderGetPic(String imageurl) {
        Log.i("My", "Get Pic Download");
        if (iImageSave != null) {
            iImageSave.ImageSave(imageurl);
        }
//        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueues(),new BitmapCache());


//        ImageLoader.ImageListener listener=ImageLoader.getImageListener()

    }

    void VolleyGetPic(String imageurl, final String fileName) {


        VolleyRequest.RequstImageGet(imageurl, VolleyRequest.picgettag, new VolleyMethodManage<Bitmap>() {

            @Override
            public void onSuccess(Bitmap result) {

                FileHelper.SaveFile(result, fileName);

            }

            @Override
            public void onError(VolleyError error) {

            }
        });


    }

    @Override
    public void onDestroy() {
        Stop();
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StartPic();

        return super.onStartCommand(intent, flags, startId);
    }

    GaiaRoom gaiaRoom = new GaiaRoom();

    public void Stop() {
        handler.removeCallbacks(runnable);
        Log.i("My", "停止pic download");
    }

    public void StartPic() {

        Log.i("My", "Pic start" + timespan);

        handler.postDelayed(runnable, timespan * 1000 * 60);//每两秒执行一次runnable.
    }

    public IImageLoad iImageLoad;
    public IImageSave iImageSave;
    Intent intentImageSave = new Intent();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public interface IImageSave {
        void ImageSave(String url);
    }

    public interface IImageLoad {
        void ImageLoadListener(ImageView imageView, int resid, int residor);

    }
}
