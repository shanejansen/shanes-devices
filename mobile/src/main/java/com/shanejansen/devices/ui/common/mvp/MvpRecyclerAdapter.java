package com.shanejansen.devices.ui.common.mvp;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shane Jansen on 5/27/16.
 */
public abstract class MvpRecyclerAdapter <T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> mData;

    public MvpRecyclerAdapter() {
        mData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<T> getData() {
        return mData;
    }

    public void clearAndAddAllData(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addAllData(List<T> data) {
        mData.addAll(data);
        int addSize = data.size();
        int oldSize = mData.size() - addSize;
        notifyItemRangeChanged(oldSize, addSize);
    }

    public void addData(T data) {
        mData.add(data);
        notifyItemInserted(mData.size());
    }

    public void updateData(int index, T data) {
        mData.remove(index);
        mData.add(index, data);
        notifyItemChanged(index);
    }

    public void removeData(int index) {
        mData.remove(index);
        notifyItemRemoved(index);
    }
}
