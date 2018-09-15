package com.example.administrator.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewDragActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_drag);

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        VideoAdapter adapter = new VideoAdapter(this, getData());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlag, swipeFlag);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return adapter.onMove(viewHolder, target);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                adapter.onSwiped(viewHolder, direction);
            }
        });
        helper.attachToRecyclerView(mRecyclerView);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add("" + i);
        }
        return list;
    }

    static class VideoAdapter extends RecyclerView.Adapter<VideoHolder>{

        private final Context mContext;
        private final List<String> mData;
        private View.OnClickListener mClickListener;
        public VideoAdapter(Context context, List<String> data){
            mContext = context;
            mData = data;
        }

        @NonNull
        @Override
        public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.local_video_drag_item_ly, parent, false);
            return new VideoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
//            holder.mThumbIv.setImageResource();
            holder.mDeleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public boolean onMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Collections.swap(mData, viewHolder.getAdapterPosition(), target.getAdapterPosition());
            notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            mData.remove(viewHolder.getAdapterPosition());
            notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    }

    public static class VideoHolder extends RecyclerView.ViewHolder{
        public FrameLayout mRootLy;
        public ImageView mThumbIv;
        public ImageView mDeleteIv;
        public VideoHolder(View itemView) {
            super(itemView);
            mRootLy = itemView.findViewById(R.id.video_selected_item_rootly);
            mThumbIv = itemView.findViewById(R.id.video_selected_thumb_iv);
            mDeleteIv = itemView.findViewById(R.id.video_selected_delete_iv);
        }
    }

    public interface OnActionListener{
        void onMove();
        void onDelete();
    }
}
