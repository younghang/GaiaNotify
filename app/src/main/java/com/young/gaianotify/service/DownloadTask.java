package com.young.gaianotify.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.young.gaianotify.FileDatabase.ThreadDBDAO;
import com.young.gaianotify.FileDatabase.ThreadInfoDAOImpl;
import com.young.gaianotify.FileDeal.FileInfo;
import com.young.gaianotify.FileDeal.ThreadInfo;
import com.young.gaianotify.SettingActivity;
import com.young.gaianotify.fragment.AnotherTsToDownload;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class DownloadTask  {
    private Context mContext=null;
    private FileInfo mFileInfo=null;
    private ThreadDBDAO mDao=null;
    private int mProgress=0;
    public static boolean isPause= false;
    public DownloadTask() {
    }

    public DownloadTask(Context mContext, FileInfo mFileInfo) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        mDao = new ThreadInfoDAOImpl(mContext);

    }
    public AnotherTsToDownload anotherTsToDownload;

    public void DownloadVideos() {
        List<ThreadInfo> threadInfos = mDao.getThreads(mFileInfo.getUrl());
        ThreadInfo threadInfo=null;
        if (threadInfos.size()==0)
        {
             threadInfo=new ThreadInfo(0,mFileInfo.getUrl(),0,mFileInfo.getLength(),0);
        }
        else
        {
            threadInfo=threadInfos.get(0);
        }
        DownloadThread downloadThread=new DownloadThread(threadInfo);
         downloadThread.tsToDownload=new AnotherTsToDownload() {
            @Override
            public void Download() {
                if (anotherTsToDownload!=null)
                {
                    anotherTsToDownload.Download();
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadTask回调");
                }

            }
        };
        downloadThread.start();

    }
    
    /**
     * 可以断点续传下载线程
     */
    class DownloadThread extends Thread{

        private ThreadInfo mThreadInfo=null;

        public DownloadThread(ThreadInfo mThreadInfo) {
            this.mThreadInfo = mThreadInfo;
        }
        public void ContinualbeDownload()        {
            //向数据库插入线程信息
            if (!mDao.isExist(mThreadInfo.getUrl(),mThreadInfo.getId()))
            {
                mDao.insertThreadInfo(mThreadInfo);
            }
            HttpURLConnection conn = null;
            InputStream inputStream=null;
            RandomAccessFile raf=null;
            try {
                URL url=new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = mThreadInfo.getStart() + mThreadInfo.getProgress();
                conn.setRequestProperty("Range","bytes="+start+"-"+mThreadInfo.getEnd());
                //设置文件写入位置
                File file = new File(mFileInfo.getFileAbsolutePath());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
//                Log.i(DownloadGaiaService.LOG_TAG_TEST, "开始：raf位置"+raf.getFilePointer()+"  start:"+start+"  fileLength:"+mFileInfo.getLength());
                //开始下载

                Intent intent = new Intent(DownloadGaiaService.ACTION_UPDATE);
                mProgress += mThreadInfo.getProgress();
                if (conn.getResponseCode()== HttpStatus.SC_PARTIAL_CONTENT) {
                    //读取文件
                    inputStream = conn.getInputStream();
                    byte[]buffer=new byte[1024*4];
                    int len=-1;
                    long time = System.currentTimeMillis();
                    int persent=mProgress * 100 / mFileInfo.getLength();
                    while ((len=inputStream.read(buffer))!=-1)
                    {
                        //写入文件
                        raf.write(buffer, 0, len);
                        //更新显示通知
                        mProgress += len;
                        int change=mProgress * 100 / mFileInfo.getLength()-persent;
                        if (change>1||System.currentTimeMillis()-time>1000) {
                            time = System.currentTimeMillis();
                            intent.putExtra("id", mFileInfo.getId());
                            intent.putExtra("progress", mProgress * 100 / mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                            persent=mProgress * 100 / mFileInfo.getLength();
                        }
                        //下载暂停，保存进度
                        if (isPause)
                        {
                            mDao.updateThreadInfo(mThreadInfo.getUrl(),mThreadInfo.getId(),mProgress);
//                            Log.i(DownloadGaiaService.LOG_TAG_TEST, "保存：raf位置" + raf.getFilePointer() );
                            return;
                        }

                    }
                    //删除线程信息并发广播
                    mDao.deleteThreadInfo(mThreadInfo.getUrl(), mThreadInfo.getId());
                    Intent intentmessage = new Intent(DownloadGaiaService.ACTION_FINISH);
                    intentmessage.putExtra("message", "文件" + mFileInfo.getFileName() + "下载完成");
                    intentmessage.putExtra("id", mFileInfo.getId());
                    mContext.sendBroadcast(intentmessage);


                    //这个也是发送通知
                    Intent intentFinish = new Intent(mContext, DownloadGaiaService.class);
                    intentFinish.setAction(DownloadGaiaService.ACTION_FINISH);
                    mContext.startService(intentFinish);

                    if (tsToDownload!=null)
                    {
                        tsToDownload.Download();
                        Log.i(DownloadGaiaService.LOG_TAG_TEST, "Not Direct DownloadThread回调");
                    }
                }

            } catch (Exception e) {
                Intent intentFinish = new Intent(mContext, DownloadGaiaService.class);
                intentFinish.putExtra("message", "连接出错，下载失败");
                intentFinish.setAction(DownloadGaiaService.ACTION_FINISH);
                mContext.startService(intentFinish);
                e.printStackTrace();
            }
            finally {
                try {
                    conn.disconnect();
                    if (raf != null) {
                        raf.close();
                    }
                    if (inputStream!=null)
                        inputStream.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        public void run() {
            SharedPreferences sharedPreferences
                    =mContext.getSharedPreferences(SettingActivity.PREFER_NAME,0);
            boolean needContinue=sharedPreferences.getBoolean(SettingActivity.NEED_CONTINUE_DOWNLOAD, false);
            if (needContinue) {
                ContinualbeDownload();
            }
            else
            {
                DirectDownload();
            }

        }
        public void DirectDownload()
        {
            //向数据库插入线程信息
            if (!mDao.isExist(mThreadInfo.getUrl(),mThreadInfo.getId()))
            {
                mDao.insertThreadInfo(mThreadInfo);
            }
            HttpURLConnection conn = null;
            InputStream inputStream=null;
            RandomAccessFile raf=null;
            try {
                URL url=new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                //设置下载位置
//                int start = mThreadInfo.getStart() + mThreadInfo.getProgress();
//                conn.setRequestProperty("Range","bytes="+start+"-"+mThreadInfo.getEnd());
                //设置文件写入位置
                File file = new File(mFileInfo.getFileAbsolutePath());
                raf = new RandomAccessFile(file, "rwd");
//                raf.seek(start);
//                Log.i(DownloadGaiaService.LOG_TAG_TEST, "开始：raf位置"+raf.getFilePointer()+"  start:"+start+"  fileLength:"+mFileInfo.getLength());
                //开始下载

                Intent intent = new Intent(DownloadGaiaService.ACTION_UPDATE);
                mProgress += mThreadInfo.getProgress();
                if (conn.getResponseCode()== HttpStatus.SC_OK) {
                    //读取文件
                    inputStream = conn.getInputStream();
                    byte[]buffer=new byte[1024*4];
                    int len=-1;
                    long time = System.currentTimeMillis();
                    int persent=mProgress * 100 / mFileInfo.getLength();
                    while ((len=inputStream.read(buffer))!=-1)
                    {
                        //写入文件
                        raf.write(buffer, 0, len);
                        //更新显示通知
                        mProgress += len;
                        int change=mProgress * 100 / mFileInfo.getLength()-persent;
                        if (change>1||System.currentTimeMillis()-time>1000) {
                            time = System.currentTimeMillis();
                            intent.putExtra("id", mFileInfo.getId());
                            intent.putExtra("progress", mProgress * 100 / mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                            persent=mProgress * 100 / mFileInfo.getLength();
                        }
                        //下载暂停，保存进度
                        if (isPause)
                        {
                            mDao.deleteThreadInfo(mThreadInfo.getUrl(),mThreadInfo.getId());
//                            Log.i(DownloadGaiaService.LOG_TAG_TEST, "保存：raf位置" + raf.getFilePointer() );
                            return;
                        }

                    }
                    //删除线程信息并发广播
                    mDao.deleteThreadInfo(mThreadInfo.getUrl(), mThreadInfo.getId());
                    Intent intentmessage = new Intent(DownloadGaiaService.ACTION_FINISH);
                    intentmessage.putExtra("message", "文件" + mFileInfo.getFileName() + "下载完成");
                    intentmessage.putExtra("id", mFileInfo.getId());
                    mContext.sendBroadcast(intentmessage);


                    //这个也是发送通知
                    Intent intentFinish = new Intent(mContext, DownloadGaiaService.class);
                    intentFinish.setAction(DownloadGaiaService.ACTION_FINISH);
                    mContext.startService(intentFinish);

                    if (tsToDownload!=null)
                    {
                        tsToDownload.Download();
                        Log.i(DownloadGaiaService.LOG_TAG_TEST, "Direct DownloadThread回调");
                    }


                }



            } catch (Exception e) {
                Intent intentFinish = new Intent(mContext, DownloadGaiaService.class);
                intentFinish.putExtra("message", "连接出错，下载失败");
                intentFinish.setAction(DownloadGaiaService.ACTION_FINISH);
                mContext.startService(intentFinish);
                e.printStackTrace();
            }
            finally {
                try {
                    conn.disconnect();
                    if (raf != null) {
                        raf.close();
                    }
                    if (inputStream!=null)
                        inputStream.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
     public    AnotherTsToDownload tsToDownload;

    }



}
