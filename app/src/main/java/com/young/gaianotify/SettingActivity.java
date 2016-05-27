package com.young.gaianotify;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.young.gaianotify.RecyclerViewPractice.DividerItemDecoration;
import com.young.gaianotify.RecyclerViewPractice.SimpleRecylcerAdapter;
import com.young.gaianotify.swipebacklayout.lib.SwipeBackLayout;
import com.young.gaianotify.swipebacklayout.lib.app.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends SwipeBackActivity {

    Toolbar toolbar;
    public static final String PREFER_NAME="settingPre";
    public static final String NEED_CONTINUE_DOWNLOAD="settingPre";
    public static final String SOURCE_ID="SOURCE_ID";
    private SwipeBackLayout mSwipeBackLayout;
    private RecyclerView recyclerView;
    private List<String> mDatas ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        toolbar.setTitle("GaiaNotify");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_dark_x24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwipeBackLayout = getSwipeBackLayout();
        //设置可以滑动的区域，推荐用屏幕像素的一半来指定
        mSwipeBackLayout.setEdgeSize(100);
        //设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_BOTH);
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ImageShow.class);
                intent.putExtra(SettingActivity.SOURCE_ID, R.drawable.notify);
                startActivity(intent);
            }
        });


//        InitailView();
//        InitailDatas();

//        SimpleRecylcerAdapter simpleRecylcerAdapter = new SimpleRecylcerAdapter(this, mDatas);
//        recyclerView.setAdapter(simpleRecylcerAdapter);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));


    }

    private void InitailDatas() {
        mDatas = new ArrayList<String>();
        for (int i=0;i<20;i++)
        {
            mDatas.add(i+"Anl");
        }
    }

    private void InitailView() {
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_simple);
    }

    void SaveSetting()
    {
        SharedPreferences settings = getSharedPreferences(PREFER_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
//        editor.putBoolean("silentMode", mSilentMode);
        editor.commit();
    }
    void InitialSetting()
    {
        SharedPreferences settings = getSharedPreferences(PREFER_NAME, 0);
        boolean silent = settings.getBoolean("silentMode", false);
    }





}
