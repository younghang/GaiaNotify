package com.young.gaianotify;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
//
//import com.camnter.easyrecyclerview.holder.EasyRecyclerViewHolder;
//import com.camnter.easyrecyclerview.widget.EasyRecyclerView;
//import com.camnter.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.young.gaianotify.Adapter.EasyRecyclerViewHolder;
import com.young.gaianotify.Adapter.ListRecyclerAdapter;
import com.young.gaianotify.RecyclerViewPractice.DividerItemDecoration;
import com.young.gaianotify.SaveDatabaseSQL.GaiaDataManage;
import com.young.gaianotify.Adapter.GaiaEasyRecycleViewAdapter;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

//import it.gmariotti.recyclerview.itemanimator.SlideInOutLeftItemAnimator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListDateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListDateFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private    List<GaiaData> datas = new ArrayList<GaiaData>();
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListDateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListDateFragment newInstance(String param1, String param2) {
        ListDateFragment fragment = new ListDateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListDateFragment() {
        // Required empty public constructor
    }


    ListRecyclerAdapter listRecyclerAdapter;
//    GaiaEasyRecycleViewAdapter easyDividerItemDecorationAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_date, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onOnline(GaiaData uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    MainActivity mainActivity;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mainActivity= (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        mainActivity.saveInfo=new IOnline() {
            @Override
            public void SaveGaiaStateInfo(GaiaData gaiaData) {
                Log.i("My", "List存入 并添加list项目");
                listRecyclerAdapter.addData(gaiaData);
                gaiaDataManage.open();
                gaiaDataManage.insertData(gaiaData.getTitle(), gaiaData.getStrTime(), gaiaData.getImageUrl());
                gaiaDataManage.close();
//                easyDividerItemDecorationAdapter.setList(collectiondatas.GetDatas());
//                easyDividerItemDecorationAdapter.notifyDataSetChanged();
            }
        };
        gaiaDataManage = new GaiaDataManage(mainActivity.getApplicationContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        new Runnable() {
            @Override
            public void run() {
                gaiaDataManage.open();
                Cursor c= gaiaDataManage.fetchAllData();
                if (c != null && c.moveToLast()) {
                    int titleIndex = c.getColumnIndex("title");
                    int timeIndex = c.getColumnIndex("time");
                    int imageIndex = c.getColumnIndex("image");
                    while (!c.isBeforeFirst()) {
                        String title=c.getString(titleIndex);
                        String time=c.getString(timeIndex);
                        String image=c.getString(imageIndex);
                        GaiaData gaiaData=new GaiaData(title,time,image);
                        datas.add(gaiaData);

                        c.moveToPrevious();
                    }
                }
//                datas.add(gaiaData);(new GaiaData("123", "123", null));
//                collectiondatas.AddData(new GaiaData("123must implement OnFragmentInteractionListener66666666666", "456", null));
//                easyDividerItemDecorationAdapter.setList(collectiondatas.GetDatas());
//                easyDividerItemDecorationAdapter.notifyDataSetChanged();
//                listRecyclerAdapter.setList(collectiondatas.GetDatas());


                gaiaDataManage.close();
            }
        }.run();
        listRecyclerAdapter=new ListRecyclerAdapter(getActivity(),datas);
//        listRecyclerAdapter.notifyDataSetChanged();
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
//        this.easyDividerItemDecorationAdapter = new GaiaEasyRecycleViewAdapter(getContext(),collectiondatas.GetDatas());

//        recyclerView.setAdapter(easyDividerItemDecorationAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // set divider
        recyclerView.addItemDecoration(
                new DividerItemDecoration(
                        getActivity(),
                        DividerItemDecoration.VERTICAL_LIST
//                        R.drawable.bg_recycler_view_divider
                )
        );


        recyclerView.setAdapter(listRecyclerAdapter);
//        easyDividerItemDecorationAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
//            @Override
//            public void onItemClick(View convertView, int position) {
//                Log.i("My", "danji");
//                Toast.makeText(getActivity(), position + " click",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        easyDividerItemDecorationAdapter.setOnItemLongClickListener(new EasyRecyclerViewHolder.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(View convertView, int position) {
//                Toast.makeText(getActivity(), position + " long click",
//                        Toast.LENGTH_SHORT).show();
//                easyDividerItemDecorationAdapter.removeData(position);
//                return true;
//            }
//        });

        listRecyclerAdapter.SetOnClickListener(new ListRecyclerAdapter.OnGaiaItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Log.i("My", "danji");
//                Toast.makeText(getActivity(), position + " click",
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemLongClick(View v, int position) {
//                Toast.makeText(getActivity(), position + " long click",
//                        Toast.LENGTH_SHORT).show();
                listRecyclerAdapter.removeData(position);
            }
        });


    }

    GaiaDataManage gaiaDataManage ;
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(GaiaData gaiaData);
    }







}
