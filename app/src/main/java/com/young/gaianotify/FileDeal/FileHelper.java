package com.young.gaianotify.FileDeal;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by young on 2016/3/26 0026.
 */
public class FileHelper {

    public static void SaveFile(Bitmap bitmap ,String fileName)
    {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();//获取SDCard目录

            String filePath=  new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(System.currentTimeMillis()));

            filePath=sdCardDir+"/Gaia/"+filePath+"/";
            File destDir = new File(filePath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            fileName=fileName.replace(':', '-');
            File saveFile = new File(destDir.getAbsolutePath()+"/"+ fileName+".jpg");
            if(!saveFile.exists()) {

                try {
                    saveFile.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream outStream = null;
                try {
                    Log.i("My", "存图");
                    outStream = new FileOutputStream(saveFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }



        }

    }



}


