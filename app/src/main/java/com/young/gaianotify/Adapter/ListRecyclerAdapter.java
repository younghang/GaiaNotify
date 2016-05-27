package com.young.gaianotify.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.young.gaianotify.AlwaysMarqueeTextView;
import com.young.gaianotify.GaiaData;
import com.young.gaianotify.R;

import java.security.Policy;
import java.util.List;

/**
 * Created by young on 2016/5/10 0010.
 */
public class ListRecyclerAdapter extends RecyclerView.Adapter <ListViewHolder> {
    private Context mContext;
    private List<GaiaData> mDatas;
    private LayoutInflater mInflater;
    public ListRecyclerAdapter(Context contex,List<GaiaData> list)
    {
        this.mContext=contex;
        this.mDatas=list;
        this.mInflater = LayoutInflater.from(contex);
    }
    public interface OnGaiaItemClickListener
    {
        void OnItemClick(View v, int position);
        void OnItemLongClick(View v, int position);
    }
    private OnGaiaItemClickListener mGaiaItemClick;

    public void SetOnClickListener(OnGaiaItemClickListener gaiaItemClickListener )
    {
        if (gaiaItemClickListener!=null)
            mGaiaItemClick=gaiaItemClickListener;
    }

    public void removeData(int postion)
    {
        mDatas.remove(postion);
        notifyItemRemoved(postion);
    }
    public void addData(GaiaData gaiaData,int postion) {
        mDatas.add(postion, gaiaData);
        notifyItemInserted(postion);
    }
    public void addData(GaiaData gaiaData) {
        mDatas.add(gaiaData);
    }



    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.item_recyclerview_multiple, parent, false);
        ListViewHolder listViewHolder=new ListViewHolder(view);
        return  listViewHolder;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        holder.multipleTVTime.setText(mDatas.get(position).getStrTime());
        holder.multipleTVTitle.setText(mDatas.get(position).getTitle());
        holder.multipleIV.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));

        if (mGaiaItemClick!=null)
        {
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mGaiaItemClick.OnItemClick(v, pos);

                }
            });
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                     mGaiaItemClick.OnItemLongClick(v,pos);
                    return false;//zheyang 就可以多层反映 才对
                }
            });
        }


    }



}
class ListViewHolder extends RecyclerView.ViewHolder
{
   public AlwaysMarqueeTextView multipleTVTitle;
    public TextView multipleTVTime;
    public ImageView multipleIV;
    public LinearLayout linearLayout;
    public ListViewHolder(View itemView) {
        super(itemView);
        multipleTVTitle = (AlwaysMarqueeTextView) itemView.findViewById(R.id.recycler_view_mul_tv_title);
          multipleTVTime = (TextView)itemView.findViewById(R.id.recycler_view_mul_tv_time);
          multipleIV = (ImageView) itemView.findViewById(R.id.recycler_view_mul_iv);
          linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_view_mul_tv_linear);

    }
}
