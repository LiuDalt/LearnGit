package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class TestRecyclerViewActivity extends AppCompatActivity {

    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_recycler_view);

//        testWithStaggerLayoutManger();
        testWithGridLayoutManager();
    }

    private void testWithGridLayoutManager() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(this, 6, LinearLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int mod = position % 6;
                if(mod == 0){
                    return 6;
                } else if(mod == 1 || mod == 2) {
                    return 3;
                } else {
                    return 2;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new TestAdapter2());
    }

    public static int getScreenWidth(Context context) {
        WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = mgr.getDefaultDisplay();
        return display.getWidth();
    }

    class TestAdapter2 extends RecyclerView.Adapter<TestViewHolder2>{

        @NonNull
        @Override
        public TestViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(getBaseContext());
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 180);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            return new TestViewHolder2(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull TestViewHolder2 holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return 21;
        }
    }

    class TestViewHolder2 extends RecyclerView.ViewHolder{

        public TestViewHolder2(View itemView) {
            super(itemView);
        }

        public void bind(int pos){
            ((TextView)itemView).setText("第" + pos + "个item");
            itemView.setBackgroundColor(Color.YELLOW);
            int mode = pos % 6;
            if(mode == 0 ){
                ((TextView) itemView).setWidth(getScreenWidth(getBaseContext()));
            } else if(mode == 1 || mode == 2){
                ((TextView) itemView).setWidth(getScreenWidth(getBaseContext())/2);
                if(mode == 1){
                    itemView.setBackgroundColor(Color.GREEN);
                } else {
                    itemView.setBackgroundColor(Color.RED);
                }
            } else {
                ((TextView) itemView).setWidth(getScreenWidth(getBaseContext())/3);
                if(mode == 3){
                    itemView.setBackgroundColor(Color.BLUE);
                } else if(mode == 4){
                    itemView.setBackgroundColor(Color.LTGRAY);
                } else {
                    itemView.setBackgroundColor(Color.MAGENTA);
                }
            }
        }
    }

    private void testWithStaggerLayoutManger() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new TestAdapter());
    }

    class TestAdapter extends RecyclerView.Adapter<TestViewHolder>{

        @NonNull
        @Override
        public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(getBaseContext());
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(getScreenWidth(getBaseContext()) / 3, 180);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            return new TestViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return 18;
        }
    }

    class TestViewHolder extends RecyclerView.ViewHolder{

        public TestViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(int pos){
            ((TextView)itemView).setText("第" + pos + "个item");
            if(pos % 3 == 0){
                itemView.setBackgroundColor(Color.RED);
            } else if (pos % 3 == 1){
                itemView.setBackgroundColor(Color.GREEN);
            } else if (pos % 3 == 2){
                itemView.setBackgroundColor(Color.BLUE);
            }

            if(pos % 4 == 0){
                StaggeredGridLayoutManager.LayoutParams clp = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
                clp.setFullSpan(true);
                ((TextView) itemView).setWidth(getScreenWidth(getBaseContext()));
                itemView.setLayoutParams(clp);
                itemView.setBackgroundColor(Color.YELLOW);
            }
        }
    }
}
