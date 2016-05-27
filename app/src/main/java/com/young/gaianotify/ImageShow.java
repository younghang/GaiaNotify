package com.young.gaianotify;

import android.graphics.BitmapFactory;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.alexvasilkov.gestures.views.interfaces.GestureView;
import com.young.gaianotify.Picture.ZoomImageView;
import com.young.gaianotify.swipebacklayout.lib.SwipeBackLayout;
import com.young.gaianotify.swipebacklayout.lib.app.SwipeBackActivity;


import static android.util.FloatMath.*;

public class ImageShow extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String strfile = getIntent().getStringExtra("image_path");
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_image_show);




//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        widthScreen = dm.widthPixels;
//        heightScreen = dm.heightPixels;
//
//        matrix = new Matrix();


//        uk.co.senab.photoview.
//        mPhotoView = (PhotoView) findViewById(R.id.image_view);

        mImageView = (GestureImageView) findViewById(R.id.imageShow);

//        mAttacher = new PhotoViewAttacher(mImageView);


        toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_dark_x24);
        //        toolbar.setTitle("PicShow");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("PicShow");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ImageShow.this, "back click", Toast.LENGTH_SHORT).show();
                finish();

            }
        });


        if (strfile != null&&!strfile.isEmpty()  ) {
            mImageView.setImageBitmap(BitmapFactory.decodeFile(strfile));
        } else {
            int ResourceId = getIntent().getIntExtra(SettingActivity.SOURCE_ID, R.drawable.nvwang_gaia);
            mImageView.setImageResource(ResourceId);
        }



//        mImageView.setImageResource(R.drawable.nvwang2_gaia);
        mImageView.getController().getSettings().setZoomEnabled(true).setPanEnabled(true).setRotationEnabled(true).setRestrictRotation(true).setOverzoomFactor(2.5f).setMaxZoom(2);



//        mAttacher.update();


        mSwipeBackLayout = getSwipeBackLayout();
        //设置可以滑动的区域，推荐用屏幕像素的一半来指定
        mSwipeBackLayout.setEdgeSize(80);
        //设定滑动关闭的方向，SwipeBackLayout.EDGE_ALL表示向下、左、右滑动均可。EDGE_LEFT，EDGE_RIGHT，EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_BOTH);
    }


    private SwipeBackLayout mSwipeBackLayout;

    Toolbar toolbar;
    GestureImageView  mImageView;



}
