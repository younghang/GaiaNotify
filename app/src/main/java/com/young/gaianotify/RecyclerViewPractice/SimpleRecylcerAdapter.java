package com.young.gaianotify.RecyclerViewPractice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.young.gaianotify.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by young on 2016/5/9 0009.
 */
public class SimpleRecylcerAdapter extends RecyclerView.Adapter<MyViewHolder> {
///1.
    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;
///2.
    public SimpleRecylcerAdapter(Context mContext, List<String> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(mContext);

    }
///3.
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_recyclerview_simpleuse, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
///5.
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder
{

///4.
    TextView textView;
    public MyViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.item_tv_simple);
    }
}
