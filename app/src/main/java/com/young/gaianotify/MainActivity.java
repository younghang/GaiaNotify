package com.young.gaianotify;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.young.gaianotify.Network.VolleyRequest;
import com.young.gaianotify.fragment.DownloadFragment;
import com.young.gaianotify.fragment.InformFragment;
import com.young.gaianotify.service.DownloadGaiaService;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements
        SetFragment.OnFragmentInteractionListener, BlankFragment.OnFragmentInteractionListener, ListDateFragment.OnFragmentInteractionListener,
        InformFragment.OnFragmentInteractionListener,
        DownloadFragment.OnFragmentInteractionListener

{

    private static final int GAIA_NOTIFY = 666;
    LinearLayout setting;
    RelativeLayout systemStyle;
    RelativeLayout saveImage;
    RelativeLayout alwayseRing;
    SwitchButton swSystemNotifyStyle;
    SwitchButton swAllRing;
    SwitchButton swSaveImage;
    Button btnInform;
    Button btnDownload;
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    String time;
    Toolbar toolbar;
    public NotificationManager notificationManager;

    GifImageView gifImageView;
    int[] gifresources = new int[]{R.drawable.nvwang_gaia, R.drawable.miku_gaia, R.drawable.nvwang2_gaia};
    int giftimes = 0;
    int gifcount=0;
    static final int gifLimitTimes=1; //实际次数为此+1
    private long gifTime = 0;


//    int[] gifresources = new int[]{R.drawable.miku_gaia, R.drawable.nvwang_gaia, R.drawable.kuangsan_gaia, R.drawable.nvwang2_gaia};
//    List<Fragment> mFragementlist = new ArrayList<Fragment>();
//    String[] mTitles = new String[]{"设置", "记录"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "MainActivity:onCreateView");
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("GaiaNotify");
        setSupportActionBar(toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        time = preferences.getString("timespan", "1");

        Initial();
        InitialSetting();
        gifImageView.setImageResource(gifresources[gifcount]);
        if (first) {
            ShowADialog();
        }


    }

    public   void ShowADialog() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.dialog);
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_info, null);
        dialog.setTitle("提示");
        Button button = (Button) view.findViewById(R.id.dialogbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    protected void onStop() {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "MainActivity:onStop");
        SaveSetting();
        super.onStop();

    }





    public List<android.support.v4.app.Fragment> mFragementlist = new ArrayList<android.support.v4.app.Fragment>();
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void Initial() {
//        mFragementlist.add(SetFragment.newInstance(time,"nihao"));
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);

            }
        });
        mFragementlist.add(SetFragment.newInstance(time, "hello"));
        mFragementlist.add(new ListDateFragment());

        setting = (LinearLayout) findViewById(R.id.setting_layout);
        saveImage = (RelativeLayout) findViewById(R.id.save_pic_layout);
        alwayseRing = (RelativeLayout) findViewById(R.id.all_ring_layout);
        systemStyle = (RelativeLayout) findViewById(R.id.system_style_layout);
//        mTablayout = (TabLayout) findViewById(R.id.id_tabLayout);
        swAllRing = (SwitchButton) findViewById(R.id.ringalways);
        swSaveImage = (SwitchButton) findViewById(R.id.savepic);
        swSaveImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    Toast.makeText(MainActivity.this, "保存位置/Sdcard/Gaia下", Toast.LENGTH_SHORT).show();
            }
        });
        swSystemNotifyStyle = (SwitchButton) findViewById(R.id.changeNotifyStyle);
        gifImageView = (GifImageView) findViewById(R.id.gif);

        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if ((System.currentTimeMillis() - exitTime) > 2000) {

                        exitTime = System.currentTimeMillis();
                        giftimes=0;

                        return;
                    } else {
                        giftimes++;
                        if (giftimes >=gifLimitTimes)
                        {
                            gifcount++;
                            if (gifcount>gifresources.length-1)
                                gifcount=0;
                            gifImageView.setImageResource(gifresources[gifcount]);
                            giftimes=0;
                            exitTime=0;
                        }
                    }

            }
        });
        btnDownload = (Button) findViewById(R.id.DownloadButton);
        btnInform = (Button) findViewById(R.id.InformButton);

//        mViewPager = (ViewPager) findViewById(R.id.id_viewPager);
//        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()
//
//        ) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragementlist.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragementlist.size();
//            }
//
//
//            @Override
//            public CharSequence getPageTitle(int position) {
//
//                return mTitles[position];
//            }
//        });
//        mTablayout.setupWithViewPager(mViewPager);


        setting.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "没有设置", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);

                    }
                }
        );
        alwayseRing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swAllRing.setChecked(!swAllRing.isChecked());
            }
        });
        systemStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swSystemNotifyStyle.setChecked(!swSystemNotifyStyle.isChecked());
            }
        });
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swSaveImage.setChecked(!swSaveImage.isChecked());
            }
        });


        btnInform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnInform.setTextColor(getResources().getColor(R.color.white));
                btnDownload.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                if (fragmentTransaction.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"退出下载",Toast.LENGTH_SHORT).show();
                    fragmentTransaction.remove(downloadFragment);

                }
                fragmentTransaction.commit();


            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                btnDownload.setTextColor(getResources().getColor(R.color.white));
                btnInform.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


                fragmentTransaction.replace(R.id.mainFragment, downloadFragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        GifImageView gif = (GifImageView) findViewById(R.id.gif);
        gif.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImageShow.class);
                intent.putExtra(SettingActivity.SOURCE_ID,gifresources[gifcount] );
                startActivity(intent);
                return false;
            }
        });

//        intent = new Intent(MainActivity.this, CheckOnlineService.class);


//        bindService(intent, connection, Service.BIND_AUTO_CREATE);


    }

    DownloadFragment downloadFragment=DownloadFragment.newInstance(time,"haha");
    public IOnline saveInfo;

    void SaveInfo(GaiaData gaiaData) {
        if (saveInfo != null) {
            Log.i("My", "MainActivity的SaveInfo");

            saveInfo.SaveGaiaStateInfo(gaiaData);
        }
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                SaveSetting();
                finish();
//                System.exit(0);  //不能这样这是非正常退出 会造成onDestoy(Fragment Activity等的)不能被调用
//                MainActivity.this.onDestroy();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    static boolean first = true;
    static boolean allRing = false;
    static boolean savePic = false;
    static boolean normalNotification = false;
    static int timespan = 0;
    void SaveSetting() {
        allRing = swAllRing.isChecked();
        savePic = swSaveImage.isChecked();
        normalNotification = swSystemNotifyStyle.isChecked();
        first = false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("allRing", allRing);
        editor.putBoolean("normalNotification", normalNotification);
        editor.putBoolean("first", first);
        editor.putBoolean("savepic", savePic);
        editor.putInt("gifcount", gifcount);
        editor.commit();

    }

    void InitialSetting() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        allRing = preferences.getBoolean("allRing", false);
        swAllRing.setChecked(allRing);

        gifcount = preferences.getInt("gifcount", 0);
        if (gifcount>gifresources.length-1)
            gifcount=0;

        normalNotification = preferences.getBoolean("normalNotification", false);
        swSystemNotifyStyle.setChecked(normalNotification);

        savePic = preferences.getBoolean("savepic", false);
        swSaveImage.setChecked(savePic);
        first = preferences.getBoolean("first", true);

    }




    @Override
    protected void onDestroy() {
//        MainActivity.this.unregisterReceiver(receiver);
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "MainActivity:onDestroy");
        MyApplication.getHttpQueues().cancelAll("getStatus");
        MyApplication.getHttpQueues().cancelAll(VolleyRequest.jsongettag);
        MyApplication.getHttpQueues().cancelAll(VolleyRequest.picgettag);
        notificationManager.cancel(666);
        notificationManager.cancel(2);
//        SaveSetting();
//        if(service.onUnbind(intent))
//        unbindService(connection);
        super.onDestroy();
//        downloadFragment.onDestroy();  这个不能这样
        SaveSetting();//在系统之后调用
    }


    @Override
    public void onFragmentInteraction(GaiaData uri) {

    }

    @Override
    public boolean onFragmentInteraction(int check, GaiaData gaiaData) {
        boolean result = false;
        if (check == 1) {
            result = swAllRing.isChecked();
        }
        if (check == 2) {
            result = swSystemNotifyStyle.isChecked();
        }

        if (check == 0) {
            Log.i("My", "回调MainActivity");

            SaveInfo(gaiaData);
        }
        return result;
    }

    @Override
    public boolean onFragmentInteraction() {
        return swSaveImage.isChecked();

    }

    @Override
    public String  onInformFragmentInteration( ) {

        return  time;
    }

    @Override
    public void onDownloadFragmentInteraction(Uri uri) {

    }


//    public boolean IsNetWorkAvailable(Context context) {
//        if (context != null) {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            if (networkInfo != null) {
//                return networkInfo.isAvailable();
//            }
//        }
//        return false;
//    }
//    public class MyReceiver extends BroadcastReceiver {
//
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle bundle=intent.getExtras();
//            Log.i("My", "收到广播");
//            String  message = bundle.getString("message");
//            if (message.equals("开播了")) {
//                stopService(servicexIntent);
//
//                if (swSystemNotifyStyle.isChecked())
//                    normalNotification=true;
//                else
//                normalNotification=false;
//
//                if (normalNotification)
//                    showNormal();
//                else
//                    OtherWay();
//
//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        GaiaOnline();
//                    }
//                };
//                runnable.run();
//
//
//            }
//            else
//            {
//            }
//            textView.setText(message);
//        }
//    }

//    private void GaiaOnline() {
//        BlankFragment fragment = new BlankFragment();
//        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
//        beginTransaction.replace(R.id.lay_out, fragment);
//        beginTransaction.commitAllowingStateLoss();

//    }


//    void OtherWay()
//    {
//        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notify_layout);
//
//        views.setTextViewText(R.id.gaia_title, "开播了Gaia");
//        views.setTextViewText(R.id.gaia_time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis())));
////        ImageView imageView= (ImageView) findViewById(R.id.gaia_image);
////        imageView.setImageResource(R.drawable.gaianofity2);
//        views.setImageViewResource(R.id.gaia_image, R.drawable.gaianofity2);
//
//        Notification status = new Notification();
//        status.contentView = views;
//        status.flags |= Notification.FLAG_ONGOING_EVENT;
//        status.contentIntent = PendingIntent.getActivity(MainActivity.this, 0, intent3, 0);
//        if (swAllRing.isChecked())
//        status.flags|=Notification.DEFAULT_ALL;
//        status.icon = R.drawable.gaianofity6;
//        status.tickerText = "大哥开播了";
//
//        status.defaults=Notification.DEFAULT_ALL;
//        notificationManager.notify(666,status);
//
//
//
//    }

//OnProgressListener UpdateTextView=new OnProgressListener() {
//    @Override
//    public void onProgress(String progress) {
//        textView.setText(progress);
//    }
//};
    //    CheckOnlineService service=new CheckOnlineService();
//    CheckOnlineService service;
//
//    ServiceConnection connection =new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder binder) {
//            Log.i("My", "connectioned");
//            service = ((CheckOnlineService.MyBinder) binder).getServise();
//            //注册回调接口来接收下载进度的变化
////            service.setOnProgressListener(new OnProgressListener() {
////                @Override
////                public void onProgress(String progress) {
////                    textView.setText(progress);
////                }
////            });
//
//
//
//
//
//            Log.i("My", "guajieshijian");
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Toast.makeText(MainActivity.this, "查询服务意外关闭", Toast.LENGTH_SHORT).show();
//        }
//    };

//    Resources res  ;
//    private void SendNotification() throws IOException {
//        Log.i("My", "发送通知");
////        Intent intent1=new Intent();
////        PendingIntent pendingIntent;
////        pendingIntent = PendingIntent.getActivities(MainActivity.this, 0, new Intent[]{intent1}, 0);
//
//        Notification.Builder builder = new Notification.Builder(MainActivity.this);
//        res = MainActivity.this.getResources();
//        builder.setSmallIcon(R.drawable.gaianofity3);
////        builder.setLargeIcon(R.drawable.gaianofity2);
//        builder.setLargeIcon(BitmapFactory.decodeStream(getAssets().open("app\\src\\main\\res\\drawable\\gaianofity2.png")));
////        builder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.gaianofity));
//        builder.setTicker("开播了");
//        builder.setWhen(System.currentTimeMillis());
//        builder.setContentTitle("Gaia");
//        builder.setContentText("大哥开播了");
////        builder.setContentIntent(pendingIntent);
//
//
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        builder.setVibrate(new long[]{500,200,500});
//        Notification notification = builder.getNotification();
//        notificationManager.notify(GAIA_NOTIFY, notification);
//
//
//    }


//private Bitmap icon;
//


//    private void showNormal() {
//
//        Notification notification = new NotificationCompat.Builder(getApplicationContext())
//                .setLargeIcon(icon).setSmallIcon(R.drawable.gaianofity3)
//                .setTicker("大哥开播了").setContentInfo(new SimpleDateFormat("yyyy-MM-dd\\nHH:mm:ss").format(new java.util.Date(System.currentTimeMillis())))
//                .setContentTitle("大哥开播了").setContentText("GaiaOnline")
//
//                .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL)
//                .build();
//        if (swAllRing.isChecked())
//            notification.flags|=Notification.DEFAULT_ALL;
//        notificationManager.notify(2, notification);
//    }


}

