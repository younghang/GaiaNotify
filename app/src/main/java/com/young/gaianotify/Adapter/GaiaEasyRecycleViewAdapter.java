package com.young.gaianotify.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.young.gaianotify.AlwaysMarqueeTextView;
import com.young.gaianotify.GaiaData;
import com.young.gaianotify.R;

import java.util.List;

/**
 * Created by young on 2016/3/24 0024.
 */
public class GaiaEasyRecycleViewAdapter extends EasyRecyclerViewAdapter {
    public GaiaEasyRecycleViewAdapter(Context context, List<GaiaData> data) {
        this.context = context;
        this.dataList = data;
    }

    List<GaiaData> dataList;
    int[] data = new int[]{R.layout.item_recyclerview_multiple};

    //        int[] data = new int[]{R.layout.item_recyclerview_single,R.layout.item_recyclerview_multiple};
    @Override
    public int[] getItemLayouts() {
        return data;
    }



    private static final int MULTIPLE_ITEM_TYPE = 0;
    private static final int SINGLE_ITEM_TYPE = 1;

    public void removeData(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }



    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    Context context;

    @Override
    public void onBindRecycleViewHolder(final EasyRecyclerViewHolder viewHolder, int position) {


        int itemType = this.getRecycleViewItemType(position);
        GaiaData data = this.getItem(position);
        switch (itemType) {
            case MULTIPLE_ITEM_TYPE: {
                AlwaysMarqueeTextView multipleTVTitle = viewHolder.findViewById(R.id.recycler_view_mul_tv_title);
                TextView multipleTVTime = viewHolder.findViewById(R.id.recycler_view_mul_tv_time);
                ImageView multipleIV = viewHolder.findViewById(R.id.recycler_view_mul_iv);
                multipleTVTime.setText(data.getStrTime());
                multipleTVTitle.setText(data.getTitle());
                multipleIV.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                break;
            }
            case SINGLE_ITEM_TYPE: {
                ImageView singleIV = viewHolder.findViewById(R.id.recycler_view_single_iv);
//                    singleIV.setImageURI(data.getImageUrl());
                break;
            }
        }
        // 如果设置了回调，则设置点击事件
//        if (mOnItemClickLitener != null) {
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = viewHolder.getLayoutPosition();
//                    mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
//                }
//            });
//
//            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int pos = viewHolder.getLayoutPosition();
//                    mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
//                    return false;
//                }
//            });
//
//        notifyItemInserted(position);
//        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return MULTIPLE_ITEM_TYPE;
    }
}
