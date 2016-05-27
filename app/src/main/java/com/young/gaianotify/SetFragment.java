package com.young.gaianotify;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.VolleyError;
import com.kyleduo.switchbutton.SwitchButton;
import com.young.gaianotify.FileDeal.FileHelper;
import com.young.gaianotify.Network.VolleyMethodManage;
import com.young.gaianotify.Network.VolleyRequest;
import com.young.gaianotify.service.CheckGaiaOnlineService;


import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetFragment newInstance(String param1, String param2) {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1, "1");
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
        time = mParam1;


    }

    String time = "1";


    SwitchButton swCheckOn;

    TextView textView;
    DiscreteSeekBar discreteSeekBar;
    Button testButton;

    Intent intent3 = new Intent();
    MyReceiver receiver = new MyReceiver();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Initial();
    }

    private ViewFlipper viewFilpper;
    PopupWindow popWindow;

    private void Initial() {
        intent3.setAction(Intent.ACTION_VIEW);
//        intent3.setComponent(new ComponentName("com.gameabc.zhanqiAndroid/.Activty.LaunchActivity", "com.uzmap.pkg.EntranceActivity"));

        intent3.setComponent(new ComponentName("com.gameabc.zhanqiAndroid", "com.gameabc.zhanqiAndroid.Activty.MainActivity"));

        swCheckOn = (SwitchButton) getActivity().findViewById(R.id.checkSwitch);

        textView = (TextView) getActivity().findViewById(R.id.txt_status);

        discreteSeekBar = (DiscreteSeekBar) getActivity().findViewById(R.id.edit_time);
        discreteSeekBar.setProgress(Integer.parseInt(time));

        notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        Log.i("My", "注册广播");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.online");
        getActivity().registerReceiver(receiver, filter);
        swCheckOn.setOnCheckedChangeListener(listener);

        testButton = (Button) getActivity().findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ToolTest.class);
//                startActivity(intent);
                mainActivity.ShowADialog();
            }
        });


    }
    PowerManager.WakeLock mWakeLock;
    private void acquireWakeLock()
    {
        if (null == mWakeLock)
        {
            PowerManager pm = (PowerManager)getActivity().getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "My");
            if (null != mWakeLock)
            {
                mWakeLock.acquire();
            }
        }
    }
    //释放设备电源锁
    private void releaseWakeLock()
    {
        if (null != mWakeLock)
        {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
    Intent serviceIntent;
    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {

//                FileTest();
                OnSwitchMethod();

            } else {
                releaseWakeLock();
                getActivity().stopService(serviceIntent);

//                service.Stop();
//                if (service.onUnbind(intent))
//                    unbindService(connection);


                Toast.makeText(getActivity(), "关闭查询", Toast.LENGTH_SHORT).show();
            }


        }
    };

    private void FileTest() {
        String url = "http://dlpic.cdn.zhanqi.tv/live/20160329/52320_RuIHA_2016-03-29-06-25-31_big.jpg";
        VolleyRequest.RequstImageGet(url, VolleyRequest.picgettag, new VolleyMethodManage<Bitmap>() {

            @Override
            public void onSuccess(Bitmap result) {
                FileHelper.SaveFile(result, "gaiatest");
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void OnSwitchMethod() {
        String str = discreteSeekBar.getProgress() + "";
        if (str.isEmpty() || str == null)
            str = "1";

        timespan = Integer.parseInt(str);
        serviceIntent = new Intent(getActivity(), CheckGaiaOnlineService.class);
        serviceIntent.putExtra("timespan", timespan);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("timespan", str);
        editor.commit();


        if (!IsNetWorkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "没联网", Toast.LENGTH_SHORT).show();
            Log.i("My", "没联网");
            swCheckOn.setChecked(false);
            return;
        }


        acquireWakeLock();
        getActivity().startService(serviceIntent);


//                service.Start(timespan);

        Toast.makeText(getActivity(), "开启查询" + "每" + timespan + "分钟查询一次", Toast.LENGTH_SHORT).show();
    }

    boolean normalNotification = false;
    static int timespan = 0;


    public NotificationManager notificationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public boolean onCheckSwitchButton(int sect, GaiaData gaiaData) {
        if (mListener != null) {
            return mListener.onFragmentInteraction(sect, gaiaData);
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mainActivity = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public boolean IsNetWorkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(getActivity().CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    public class MyReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.i("My", "收到广播");
            String message = bundle.getString("message");
            String title = bundle.getString("title");
//            String time = bundle.getString("time");
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));
            String image = bundle.getString("imagrurl");
            try {
                Log.i("My", "创建gaia单元信息");

                gaiaData = new GaiaData(title, time, image);
            } catch (Exception e) {
                Log.i("My", "创建Gaia list item 出错");

                e.printStackTrace();
            }
            if (message.equals("开播了")) {
                releaseWakeLock();
                getActivity().stopService(serviceIntent);

                normalNotification = onCheckSwitchButton(2, null);

                if (normalNotification)
                    showNormal();
                else
                    OtherWay();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        GaiaOnline();
                    }
                };
                runnable.run();


            } else {
            }
            textView.setText(message);
        }
    }

    GaiaData gaiaData;
    MainActivity mainActivity;

    private void GaiaOnline() {
        Log.i("My", "处理开播后的事宜");

        onCheckSwitchButton(0, gaiaData);
        BlankFragment fragment = new BlankFragment();
        FragmentTransaction beginTransaction = mainActivity.fragmentManager.beginTransaction();

        beginTransaction.replace(R.id.fragment_set, fragment);

        beginTransaction.commitAllowingStateLoss();
//        mainActivity.mFragementlist.add(0,new BlankFragment());


    }

    void OtherWay() {
        RemoteViews views = new RemoteViews(getActivity().getPackageName(), R.layout.notify_layout);

        views.setTextViewText(R.id.gaia_title, "开播了Gaia");
        views.setTextViewText(R.id.gaia_time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis())));
//        ImageView imageView= (ImageView) findViewById(R.id.gaia_image);
//        imageView.setImageResource(R.drawable.gaianofity2);
        views.setImageViewResource(R.id.gaia_image, R.drawable.gaianofity2);

        Notification status = new Notification();
        status.contentView = views;
        status.flags |= Notification.FLAG_ONGOING_EVENT;
        status.contentIntent = PendingIntent.getActivity(getActivity(), 0, intent3, 0);

        status.flags |= Notification.FLAG_AUTO_CANCEL;
        status.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 系统默认铃声
        if (onCheckSwitchButton(1, null))
            status.flags |= Notification.FLAG_INSISTENT;
        status.icon = R.drawable.gaianofity6;
        status.tickerText = "大哥开播了";


        notificationManager.notify(666, status);


    }

    private Bitmap icon;


    private void showNormal() {

        Notification notification = new NotificationCompat.Builder(getActivity().getApplicationContext())
                .setLargeIcon(icon).setSmallIcon(R.drawable.gaianofity3)
                .setTicker("大哥开播了").setContentInfo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis())))
                .setContentTitle("大哥开播了").setContentText("GaiaOnline")

//                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        if (onCheckSwitchButton(1, null))
            notification.flags |= Notification.FLAG_INSISTENT;
        notificationManager.notify(2, notification);
    }

    @Override
    public void onDestroy() {
        if (serviceIntent != null)
            getActivity().stopService(serviceIntent);
        releaseWakeLock();
        notificationManager.cancel(2);
        notificationManager.cancel(666);
        getActivity().unregisterReceiver(receiver);

        MyApplication.getHttpQueues().cancelAll("getStatus");


        super.onDestroy();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        boolean onFragmentInteraction(int check, GaiaData gaiaData);
    }


}
//    private void InitialDialog() {
//
//        View view    = LayoutInflater.from(getActivity()).inflate(R.layout.activity_dialog_info, null);
//        popWindow = new PopupWindow(getActivity());
//        popWindow.setContentView(view);
////        popWindow.showAtLocation(MainActivity.this.findViewById(R.id.edit_time), Gravity.CENTER, 0, 0);
//
//
//        popWindow.showAtLocation(getActivity().findViewById(R.id.txt_status), Gravity.CENTER,0,0);
//        Button button = (Button) view.findViewById(R.id.dialogbutton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popWindow.dismiss();
//            }
//        });
//
//    }