package com.example.administrator.myapplication;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MViewHolder>  {
    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.rv_item_ly, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MViewHolder extends RecyclerView.ViewHolder{
        public MViewHolder(View itemView) {
            super(itemView);
        }
    }
}
