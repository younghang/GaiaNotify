package com.young.gaianotify;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.android.volley.VolleyError;
import com.young.gaianotify.Network.VolleyMethodManage;
import com.young.gaianotify.Network.VolleyRequest;
import com.young.gaianotify.RecyclerViewPractice.DividerItemDecoration;
import com.young.gaianotify.RecyclerViewPractice.SimpleRecylcerAdapter;
import com.young.gaianotify.swipebacklayout.lib.SwipeBackLayout;
import com.young.gaianotify.swipebacklayout.lib.app.SwipeBackActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingActivity extends SwipeBackActivity {

    Toolbar toolbar;
    public static final String PREFER_NAME="settingPre";
    public static final String NEED_CONTINUE_DOWNLOAD="settingPre";
    public static final String SOURCE_ID="SOURCE_ID";
    private SwipeBackLayout mSwipeBackLayout;
    private RecyclerView recyclerView;
    private List<String> mDatas ;
    private Button btnUpdate;
    MaterialDialog mMaterialDialog;
    static final String UpdateUrl="https://github.com/younghang/GaiaNotify/blob/master/notify_update.txt";
static final int NotifyVersion=10;
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
        btnUpdate = (Button) findViewById(R.id.update_button);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog = new MaterialDialog(SettingActivity.this);
                View view = LayoutInflater.from(SettingActivity.this)
                        .inflate(R.layout.dialog_progress,
                                null);
                mMaterialDialog.setCanceledOnTouchOutside(true);
                mMaterialDialog.setView(view).show();


                    new Runnable(

                    ) {
                        @Override
                        public void run() {
                            VolleyRequest.RequestStringGet(UpdateUrl, VolleyRequest.stringgettag, new VolleyMethodManage<String>() {
                                @Override
                                public void onSuccess(String result) {
                                    mMaterialDialog.dismiss();
                                    mMaterialDialog = new MaterialDialog(SettingActivity.this);
                                    boolean HaveUpdate=false;
                                    Log.v("TAG", result);
                                    String versionReg="NotifyVersion=[0-9]+#";
                                    Pattern pattern = Pattern.compile(versionReg);
                                    Matcher matcher = pattern.matcher(result);

                                    String version="";
                                    while(matcher.find())
                                        version =matcher.group();
                                    version=version.split("=")[1];
                                    version=version.substring(0, version.length() - 1);
                                    int LastedVersion = Integer.parseInt(version);
                                    if (LastedVersion>NotifyVersion)
                                        HaveUpdate=true;

                                    if (HaveUpdate) {
                                        mMaterialDialog.setTitle("提示").setMessage("当前版本:"+NotifyVersion+"\n最新版本："+LastedVersion+"\n有更新点击前往")
                                                .setPositiveButton("前往", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String url = "http://yun.baidu.com/share/link?shareid=3067013852&uk=3514645625";
                                                        Uri uri = Uri.parse(url);
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                        startActivity(intent);
                                                    }
                                                }).setCanceledOnTouchOutside(true)
                                                .show();
                                    }
                                        else {
                                            mMaterialDialog.setTitle("提示").setMessage("当前版本:"+NotifyVersion+"\n最新版本："+LastedVersion+"\n没有更新")
                                                    .setPositiveButton("关闭", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            mMaterialDialog.dismiss();
                                                        }
                                                    }).setCanceledOnTouchOutside(true)
                                                .show();
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    mMaterialDialog.dismiss();
                                    mMaterialDialog = new MaterialDialog(SettingActivity.this);
                                    mMaterialDialog.setTitle("提示").setMessage("检测失败")
                                            .setPositiveButton("关闭", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    mMaterialDialog.dismiss();
                                                }
                                            }).setCanceledOnTouchOutside(true)
                                            .show();
                                }
                            });
                        }
                    }.run();
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
