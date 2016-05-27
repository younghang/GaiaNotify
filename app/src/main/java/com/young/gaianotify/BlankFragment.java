package com.young.gaianotify;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;

import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.young.gaianotify.Network.BitmapCache;
import com.young.gaianotify.Network.VolleyRequest;
import com.young.gaianotify.service.SaveGaiaPicService;

import java.io.File;
import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView text_info;
    ImageView imageView;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        notificationManager = mainActivity.notificationManager;

        text_info = (TextView) getActivity().findViewById(R.id.txt_info);
        imageView = (ImageView) getActivity().findViewById(R.id.imageSave);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, ImageShow.class);
                File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
                String filePath=  new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(System.currentTimeMillis()));

                filePath=sdCardDir+"/Gaia/"+filePath;
                time=time.replace(':', '-');
                filePath=filePath+"/"+time+".jpg";
                intent.putExtra("image_path",filePath);
                mainActivity.startActivity(intent);

            }
        });
        text_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationManager.cancel(666);

                notificationManager.cancel(2);
                notificationManager.cancelAll();
                Toast.makeText(mainActivity, "通知已经清除", Toast.LENGTH_SHORT).show();

                count++;
                if (count == 5) {
                    text_info.setText(R.string.say_not_alone);
                    count = 0;
                }
            }
        });

        if (CheckSave()) {
            acquireWakeLock();
            saveGaiaPicServiceintent = new Intent(getActivity(), SaveGaiaPicService.class);
            getActivity().startService(saveGaiaPicServiceintent);
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.gaia_image_save");
            getActivity().registerReceiver(receiver, filter);


        }

    }

    ImageGaiaReceiver receiver = new ImageGaiaReceiver();

    Intent saveGaiaPicServiceintent;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseWakeLock();
        getActivity().stopService(saveGaiaPicServiceintent);
        MyApplication.getHttpQueues().cancelAll(VolleyRequest.picgettag);
    }

    MainActivity mainActivity;
    NotificationManager notificationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public boolean CheckSave(   ) {
        if (mListener != null) {
           return mListener.onFragmentInteraction( );
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


    public void ImageSave(String url) {
        Log.i("My", "Get Pic Save and Show");
        ImageLoader loader = new ImageLoader(MyApplication.getHttpQueues(), new BitmapCache());
        imageView.setMinimumHeight(200);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.gaianofity3, R.mipmap.ic_launcher);
        loader.get(url, listener);


    }
    String time;

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
    public class ImageGaiaReceiver extends BroadcastReceiver {

        String image;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Log.i("My", "收到广播");
            image = bundle.getString("imageurl");
              time = bundle.getString("edittime");
//            String time= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));


            Log.i("My", "创建gaia image ");

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ImageSave(image);
                }
            };
            runnable.run();




            if (SaveGaiaPicService.failedCount >= 4) {
                getActivity().stopService(saveGaiaPicServiceintent);
                releaseWakeLock();

//                if (onCheckSwitchButton(2, null))
//                    normalNotification = true;
//                else
//                    normalNotification = false;


            }
        }

    }
        /**
         * This interface must be implemented by activities that contain this
         * fragment to allow an interaction in this fragment to be communicated
         * to the activity and potentially other fragments contained in that
         * activity.
         * <p/>
         * See the Android Training lesson <a href=
         * "http://developer.android.com/training/basics/fragments/communicating.html"
         * >Communicating with Other Fragments</a> for more information.
         */
        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            boolean onFragmentInteraction();
        }

    }
