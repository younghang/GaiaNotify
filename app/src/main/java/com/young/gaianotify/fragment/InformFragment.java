package com.young.gaianotify.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.young.gaianotify.BlankFragment;
import com.young.gaianotify.GaiaData;
import com.young.gaianotify.ListDateFragment;
import com.young.gaianotify.MainActivity;
import com.young.gaianotify.R;
import com.young.gaianotify.SetFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InformFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InformFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformFragment extends  android.support.v4.app.Fragment   {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TabLayout mTablayout;
    ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InformFragment newInstance(String param1, String param2) {
        InformFragment fragment = new InformFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InformFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    MainActivity mainActivity;
    String[] mTitles = new String[]{"设置", "记录"};
    String time;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mTablayout = (TabLayout)   getActivity().findViewById(R.id.id_tabLayout);
        mViewPager = (ViewPager) getActivity().findViewById(R.id.id_viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(mainActivity.getSupportFragmentManager()

        ) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return mainActivity.mFragementlist.get(position);
            }

            @Override
            public int getCount() {
                return mainActivity.mFragementlist.size();
            }


            @Override
            public CharSequence getPageTitle(int position) {

                return mTitles[position];
            }
        });
        mTablayout.setupWithViewPager(mViewPager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inform, container, false);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction();
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mainActivity = (MainActivity) activity;
            time=mListener.onInformFragmentInteration();
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        String onInformFragmentInteration();
    }

}
