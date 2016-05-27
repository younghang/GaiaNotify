package com.young.gaianotify.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.renderscript.Double4;
import android.util.Log;

import com.android.volley.VolleyError;
import com.young.gaianotify.FileDeal.FileInfo;
import com.young.gaianotify.Network.VolleyMethodManage;
import com.young.gaianotify.Network.VolleyRequest;
import com.young.gaianotify.fragment.AnotherTsToDownload;

import org.apache.http.HttpConnection;
import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadGaiaService extends Service {


    public static final String ACTION_START = "ACTION_START_GAIA_VIDEO";
    public static final String ACTION_STOP = "ACTION_STOP_GAIA_VIDEO";
    public static final String LOG_TAG_TEST = "LOG_TAG_TEST";
    public static final String ACTION_INFO = "ACTION_INFO";
    public static final String ACTION_MERGER = "ACTION_MERGER";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_FINISH = "ACTION_FINISH";
    public static final String START_DOWNLOAD = "获取文件信息成功，并开始一个下载";
    public static final String ONLINE_ERROR="获取视频地址失败 or Not online";
    public static final int NOTIFICATION_ID=556;
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Gaia/" + "Video/";
    public static final int MSG_INIT = 0;
    public int id = 0;
    public String filePath = "";
    private DownloadTask mTask = null;
    private static boolean Downloading = false;
    private static boolean Mergering = false;
    Handler downloadHandler = new Handler();
    int timespan=8;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            GetUrlThread getUrlThreads = new GetUrlThread();
            getUrlThreads.start();

            downloadHandler.postDelayed(this, timespan * 1400);
        }
    };

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "Init:" + "get file Initialed");
                    SendMessage(START_DOWNLOAD, fileInfo);
                    mTask = new DownloadTask(DownloadGaiaService.this, fileInfo);

                    mTask.DownloadVideos();
                    Downloading=true;
                    //绑定下载回调另一个下载
                    mTask.anotherTsToDownload=new AnotherTsToDownload() {
                        @Override
                        public void Download() {
                            //下载完成时的回调
                            Downloading=false;
                            StartATsDownload();
                        }
                    };
                    break;
            }

        }
    };
    public DownloadGaiaService() {

        //单文件测试必须设置目录
//        SetFilePath();
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "Download:" + "构造函数调用");

    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "启动service:intent.getFlags=" + intent.getFlags()  + " startID: " + startId+"  identify="+intent.getIntExtra("identify",0));
        //获得Activity传入的参数
        switch (intent.getAction()) {
            case ACTION_START:


                if (Downloading) {
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "Downloading:true");
                    break;
                }
                if (intent.getFlags() == 666) {
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "来自Fragment 启动service:  Start flags" + flags + " startID: " + startId);
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "Start:");
                    //直播下载
                    SetFilePath();
                    vedioUrlCollection= new VedioUrlCollection();
                    vedioUrlCollection.startDownload= new IDownloadVideo() {
                        @Override
                        public void StartDownload() {
                            SendMessage("绑定下载接口");
                            StartATsDownload();
                            id++;
                        }
                    };
                    DownloadTask.isPause=false;
                    getUrlThread = new GetUrlThread();
                    getUrlThread.start();
                    downloadHandler.postDelayed(runnable, timespan * 1400);
                    //单文件测试
//                Test();

                    break;
                }
                Intent start = new Intent();
                start.setAction(DownloadGaiaService.ACTION_START);
                sendBroadcast(start);
                break ;



            case ACTION_STOP:

                Downloading = false;

                id = 0;
                if (intent.getFlags() == 665) {
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "来自 Fragment 启动service Stop:" + flags + " startID: " + startId);
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "Stop:");
                    if (mTask != null) {
                        DownloadTask.isPause = true;
                    }
                    downloadHandler.removeCallbacks(runnable);
                    if (vedioUrlCollection!=null)
                    vedioUrlCollection.Empty();
                    break;
                }

                Intent stop = new Intent();
                stop.setAction(DownloadGaiaService.ACTION_STOP);
                sendBroadcast(stop);
                break;


            case ACTION_FINISH:

                Log.i(DownloadGaiaService.LOG_TAG_TEST, "Finish a ts Download.By:StartService ");
                Downloading = false;

                break;
            case ACTION_MERGER:
                if (Downloading)
                {
                    Intent intentstop = new Intent();
                    intentstop.setAction(DownloadGaiaService.ACTION_STOP);
                    sendBroadcast(intentstop);
                    break;
                }
                if (Mergering)
                    break;

                Intent merger = new Intent();
                merger.putExtra("total_id", id);
                merger.setAction(DownloadGaiaService.ACTION_MERGER);
                sendBroadcast(merger);

                Log.i(DownloadGaiaService.LOG_TAG_TEST, "Merger.By:Notification");
                new MergerThread(filePath).start();

                break;
        }


        return super.onStartCommand(intent, flags, startId);
    }

    //添加文件地址URL线程GetUrlThread
// 给定下载位置，InitialFileInfoThread，设置文件大小和名称绝对路径及URL，最后调用下载线程DownloadTask
    private void SetFilePath() {
        File dir = new File(DOWNLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        filePath = DOWNLOAD_PATH + new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new java.util.Date(System.currentTimeMillis()))
                + "/";
    }

    void Test() {
        FileInfo fileInfo = new FileInfo();
//        SetFilePath();
//        测试视频是可以断点续传的，文件就不行了，估计是有一两位的容错
//        http://dlhls.cdn.zhanqi.tv/zqlive/4758_FQuZM_1024/1462725388017_1462725388017.ts?Dnion_vsnae=4758_FQuZM
        fileInfo.setUrl("http://xiazai.mathtype.cn/MathType6.9b_Trial_YY.exe");
        fileInfo.setFileName("something.pdf");
        fileInfo.setId(id);
        new InitialFileInfoThread(fileInfo, filePath).start();

    }

    private void StartATsDownload() {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "StartATsDownload下载。 开始提取一个下载链接");
        if (!vedioUrlCollection.Hasurl()) {
            return;
        }
        String reurl = vedioUrlCollection.GetOneLink();
        String[] urs = reurl.split("\\$");
        FileInfo fileInfo = new FileInfo(urs[1], urs[0] + ".ts", Integer.parseInt(urs[0]), 0, 0);
//        FileInfo fileInfo = new FileInfo(reurl, id + ".ts", id, 0, 0);
        InitialFileInfoThread infthread = new InitialFileInfoThread(fileInfo, filePath);
        infthread.start();

    }

    @Override
    public void onDestroy() {
        downloadHandler.removeCallbacks(runnable);
        super.onDestroy();
    }

    GetUrlThread getUrlThread;
    VedioUrlCollection vedioUrlCollection ;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void SendMessage(String message) {
        SendMessage(message, null);
    }
    public void SendMessage(String message,boolean downloading) {
        if (downloading)
            return;
        SendMessage(message, null);
    }
    public void SendMessage(String message, FileInfo fileInfo) {

        Intent intent = new Intent();
        if (fileInfo != null)
            intent.putExtra("fileInfo", fileInfo);
        intent.putExtra("message", message);
        intent.setAction(DownloadGaiaService.ACTION_INFO);
        sendBroadcast(intent);
    }

    class MergerThread extends Thread {
        private String MergerFilePath = "";

        MergerThread(String mergerFilePath) {
            MergerFilePath = mergerFilePath;
        }

        public void run() {
            Mergering=true;
            SendMessage("合并文件线程启动");
            File file = new File(MergerFilePath);
            if (file.exists() && file.isDirectory()) {
                if ((file.list().length == 0)) {
                    file.delete();
                    return;
                }
                if (file.list().length == 1)
                {
                    SendMessage("没有什么可以合并的");
                    return;
                }
                FilenameFilter ff = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains(".ts");
                    }
                };
                List<String> files = new ArrayList<String>();
                String[] fileNames= file.list(ff);


                for (String f : fileNames) {
                    files.add(f);
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "FileName:" + f);

                }
//                Collections.sort(files);//此处没有任何用处的排序
                byte[] buffer = new byte[1024 * 4];
                for (String f:files)
                {
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "Sort FileName:"+f);
                    File simple = new File(filePath, f);
                    File mergerfile=new File(filePath,"Gaia合并视频.ts");
                    if (!mergerfile.exists()) {
                        try {
                            mergerfile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    InputStream instream=null;
                    OutputStream outStream=null;
                    try {
                        int len=-1;
                        instream = new FileInputStream(simple);
                        outStream = new FileOutputStream(mergerfile,true);
                        while ((len = instream.read(buffer, 0, buffer.length)) != -1) {
                            outStream.write(buffer, 0, len);
                        }

                    }
                    catch (Exception e)
                    {

                        e.printStackTrace();
                    }
                    finally {

                        try {
                            instream.close();
                            outStream.close();
                            simple.delete();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        Mergering=false;

                    }
                }
                SendMessage("合并完成");
            }else
        SendMessage("当前什么都没有");
        }


    }

    class GetUrlThread extends Thread {
        private static final String RoomID = "52320_RuIHA";//Gaia: "52320_RuIHA"  Test:67101_EuFOJ 102152_R1tO7 123699_qIqlt 4758_FQuZM "45086_3GG9G"
        private static final String LiveUrlPrefix = "http://dlhls.cdn.zhanqi.tv/zqlive/" + RoomID + "_1024/";
        private static final String LiveRoomUrl = "http://dlhls.cdn.zhanqi.tv/zqlive/" + RoomID + "_1024/index.m3u8" + "?Dnion_vsnae=" + RoomID;

        @Override
        public void run() {
            Log.i(DownloadGaiaService.LOG_TAG_TEST, "GetUrlThread:Run");
            VolleyRequest.RequestStringGet(LiveRoomUrl, VolleyRequest.stringgettag, new VolleyMethodManage<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "GetUrlThread:Success一次成功获取列表");
                    vedioUrlCollection.AnalysisUrl(result);
                    timespan=Integer.parseInt(vedioUrlCollection.duration);
                    SendMessage("获取视频地址成功",Downloading);

                }

                @Override
                public void onError(VolleyError error) {
                    SendMessage(DownloadGaiaService.ONLINE_ERROR);
                    Log.i(DownloadGaiaService.LOG_TAG_TEST, "GetUrlThread:Failed");
                }
            });


        }
    }

    class VedioUrlCollection {
        Map<Integer, String> urlCollections = new HashMap<Integer, String>();
        public int i = 0;
        public int downloadpos = 1;
        public String duration;
        private boolean first=true;

        public void AnalysisUrl(String str) {
            Log.i(DownloadGaiaService.LOG_TAG_TEST, "GetUrlThread:AnalyseUrl");
            String[] strlist = str.split("\n");
            for (int i = 0; i < strlist.length; i++) {
                if (strlist[i].contains("TARGETDURATION")) {
                    String[] sls = strlist[i].split(":");
                    duration = sls[1];
                }
                if (strlist[i].contains(".ts")) {

                    String key = strlist[i];
                    AddUrl(GetUrlThread.LiveUrlPrefix + key);

                }
            }

            if ((first||!Downloading)&&startDownload != null) {
                startDownload.StartDownload();
                SendMessage("开始下载");
                first=false;
            }

        }


        public void AddUrl(String value) {

            if (urlCollections.containsValue(value)) {
                return;
            }
            i++;
            urlCollections.put(i, value);

        }

        public void RemoveUrl(int pos) {
            if (urlCollections.containsKey(pos)) {
                urlCollections.remove(pos);
            }
        }

        public boolean Hasurl() {
            return i != 0 && downloadpos <= i;
        }

        public void Empty() {
            urlCollections.clear();
            i = 0;
            downloadpos = 1;

        }

        int safeCount = 0;
        private IDownloadVideo startDownload;

        public String GetOneLink() {
            safeCount++;
            if (safeCount != 1) {
                return null;
            }
            if (i == 0 || downloadpos > i) {
                return null;
            }
            String link = urlCollections.get(downloadpos);
            String re = downloadpos + "$" + link;
//            String re =  link;
            downloadpos++;
            safeCount--;
            return re;
        }


    }

    class InitialFileInfoThread extends Thread {

        private FileInfo fileInfo = null;
        private String filePath = "";
        RandomAccessFile raf = null;

        public InitialFileInfoThread(FileInfo fileInfo, String filePath) {
            this.fileInfo = fileInfo;
            this.filePath = filePath;
        }

        public void run() {
            Log.i(DownloadGaiaService.LOG_TAG_TEST, "InitialFileInfoThread:Run to initial a .ts");
            HttpURLConnection connection = null;//不是HttConnection
            try {
                //连接网络文件
                URL url = new URL(fileInfo.getUrl());
                Log.i(DownloadGaiaService.LOG_TAG_TEST,  fileInfo.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                int length = -1;

                if (connection.getResponseCode() == HttpStatus.SC_OK)
                {
//                获取文件长度
                    length = connection.getContentLength();
                }
                if (length <= -1) {
                    return;
                }
                // 在本地创建文件
                File dir = new File(filePath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
//                File file = new File(dir, fileInfo.getFileName());
//                raf = new RandomAccessFile(file, "rwd");//r读w写d删除
//                raf.setLength(length);
                //设置文件长度
                fileInfo.setLength(length);
                fileInfo.setFileAbsolutePath(filePath + "/" + fileInfo.getFileName());
                mhandler.obtainMessage(MSG_INIT, fileInfo).sendToTarget();
                Log.i(DownloadGaiaService.LOG_TAG_TEST, "完成一个FileInfo 的初始化");


            } catch (Exception e) {
                SendMessage("下载视频失败");
                e.printStackTrace();
            } finally {
                try {
                    connection.disconnect();
                    if (raf != null)
                        raf.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


    }

    public interface IDownloadVideo {
        void StartDownload();
    }

}
