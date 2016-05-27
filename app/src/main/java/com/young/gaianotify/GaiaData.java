package com.young.gaianotify;

import android.net.Uri;

import java.net.URL;

/**
 * Created by young on 2016/3/21 0021.
 */
public class GaiaData
{
    public String getImageUrl() {
        return imageUrl;
    }
//        public     GaiaData newGaiaInstance(String title,String strTime,Uri imageUrl) {
//            GaiaData gaiaData = new GaiaData(title,strTime,imageUrl);
//            return gaiaData;
//        }

    public GaiaData(String title,String strTime,String imageUrl)
    {
        this.Title=title;
        this.imageUrl=imageUrl;
        this.strTime=strTime;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    private String strTime;
    private String imageUrl;
    private String Title;
}