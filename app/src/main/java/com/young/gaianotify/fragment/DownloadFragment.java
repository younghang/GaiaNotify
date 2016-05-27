package com.young.gaianotify.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.young.gaianotify.FileDeal.FileInfo;
import com.young.gaianotify.MainActivity;
import com.young.gaianotify.MyApplication;
import com.young.gaianotify.Network.VolleyRequest;
import com.young.gaianotify.R;
import com.young.gaianotify.Utils.NotificationUtils;
import com.young.gaianotify.dashspinner.DashSpinner;
import com.young.gaianotify.service.DownloadGaiaService;

public class DownloadFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static DownloadFragment newInstance(String param1, String param2) {
        DownloadFragment fragment = new DownloadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DownloadFragment() {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadFragment:构造函数");
        // Required empty public constructor
    }

    SwitchButton swDownload;
    TextView txDownloadInfo;
    DashSpinner dashSpinner;
    ProgressBar progressBar;
    private NotificationUtils mNotificationUtils=null;
    private DownloadReciever downloadreceiver = new DownloadReciever();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadFragment:onActivityCreate");
        super.onActivityCreated(savedInstanceState);
        swDownload = (SwitchButton) getActivity().findViewById(R.id.downloadSwitch);
        txDownloadInfo = (TextView) getActivity().findViewById(R.id.infoDownload);
        dashSpinner = (DashSpinner) getActivity().findViewById(R.id.progress_spinner);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        mNotificationUtils = new NotificationUtils(mainActivity);
        Log.i("My", "注册广播");
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadGaiaService.ACTION_INFO);
        filter.addAction(DownloadGaiaService.ACTION_STOP);
        filter.addAction(DownloadGaiaService.ACTION_START);
        filter.addAction(DownloadGaiaService.ACTION_UPDATE);
        filter.addAction(DownloadGaiaService.ACTION_FINISH);
        getActivity().registerReceiver(downloadreceiver, filter);
        swDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SwitchOn();
                } else {
                    SwitchOff();
                }


            }
        });
        dashSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DownloadGaiaService.class);
                intent.setAction(DownloadGaiaService.ACTION_MERGER);
                getActivity().startService(intent);

            }
        });
        progressBar.setProgress(0);
        dashSpinner.setProgress(0);



    }

    private void SwitchOff() {
        releaseWakeLock();
        if (serviceIntent == null)
            serviceIntent = new Intent(getActivity(), DownloadGaiaService.class);
        serviceIntent.setFlags(665);
        serviceIntent.setAction(DownloadGaiaService.ACTION_STOP);
        getActivity().startService(serviceIntent);
        txDownloadInfo.setText("关闭");
        Toast.makeText(getActivity(), "关闭", Toast.LENGTH_SHORT).show();
    }

    private void SwitchOn() {
        if (!IsNetWorkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "没联网", Toast.LENGTH_SHORT).show();
            Log.i("My", "没联网");
            swDownload.setChecked(false);
            return;
        }
        acquireWakeLock();
        if (serviceIntent == null)
            serviceIntent = new Intent(getActivity(), DownloadGaiaService.class);
        serviceIntent.setFlags(666);
        serviceIntent.putExtra("identify",56);
        serviceIntent.setAction(DownloadGaiaService.ACTION_START);
        getActivity().startService(serviceIntent);

    }

    Intent serviceIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadFragment:onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadFragment:onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onDownloadFragmentInteraction(uri);
        }
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

    MainActivity mainActivity;

    @Override
    public void onDetach() {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadFragment:onDetach");
        super.onDetach();
        mListener = null;

    }

    @Override
    public void onDestroy() {
        Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadFragment:onDestroy");

        if (serviceIntent != null)
        getActivity().stopService(serviceIntent);
        releaseWakeLock();

        MyApplication.getHttpQueues().cancelAll(VolleyRequest.stringgettag);
        super.onDestroy();
        getActivity().unregisterReceiver(downloadreceiver);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onDownloadFragmentInteraction(Uri uri);
    }

    public boolean IsNetWorkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    PowerManager.WakeLock mWakeLock;

    private void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "My");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    //释放设备电源锁
    private void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

  public   class DownloadReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
          if (DownloadGaiaService.ACTION_INFO.equals(intent.getAction()))
          {
              String message = intent.getStringExtra("message");
              if (message.isEmpty() || message == null)
                  message = "Error";
              if (message.equals(DownloadGaiaService.START_DOWNLOAD)) {
                  FileInfo fileInfo= (FileInfo) intent.getSerializableExtra("fileInfo");
                  mNotificationUtils.SendNotification(fileInfo);
                  txDownloadInfo.setText(message + fileInfo.getFileName());
//                  dashSpinner.resetValues();重复刷新太多
                  return;

              }
              if (message.equals(DownloadGaiaService.ONLINE_ERROR))
                  dashSpinner.showUnknown();
              txDownloadInfo.setText(message);
          }
            else if (DownloadGaiaService.ACTION_UPDATE.equals(intent.getAction())) {
              int progress=intent.getIntExtra("progress",0);
              int id = intent.getIntExtra("id",0);
              progressBar.setProgress(progress);
              dashSpinner.setProgress((float) (1.0 * progress / 100));
//              mNotificationUtils.UpdateNotification(id,progress);
              mNotificationUtils.UpdateNotification(DownloadGaiaService.NOTIFICATION_ID,progress);

          }
            else if (DownloadGaiaService.ACTION_FINISH.equals(intent.getAction())) {
              String message = intent.getStringExtra("message");
              if (message.isEmpty() || message == null)
                  message = "File Error";
              int id = intent.getIntExtra("id",0);
//              mNotificationUtils.CancleNotification(id);
              txDownloadInfo.setText(message);
              dashSpinner.showSuccess();

          }
          else if (DownloadGaiaService.ACTION_START.equals(intent.getAction())) {
              Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadFragment:收到 start广播");
              swDownload.toggleImmediately();
          }
          else if (DownloadGaiaService.ACTION_STOP.equals(intent.getAction())) {
              Log.i(DownloadGaiaService.LOG_TAG_TEST, "DownloadFragment:收到 stop广播");
              swDownload.toggleImmediately();
          }


        }
    }

}
