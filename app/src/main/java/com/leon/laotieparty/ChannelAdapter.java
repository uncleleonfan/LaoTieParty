package com.leon.laotieparty;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Leon on 2017/4/6.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {

    private Context mContext;
    private List<ChannelItem> mList;

    public ChannelAdapter(Context context, List<ChannelItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChannelViewHolder(new ChannelItemView(mContext));
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        ((ChannelItemView)holder.itemView).bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder {

        public ChannelViewHolder(View itemView) {
            super(itemView);
        }
    }
}
