package com.example.accessibility.ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accessibility.Constant;
import com.example.accessibility.R;
import com.example.accessibility.sharepre.SharePreferenceConstant;
import com.example.accessibility.sharepre.SharePreferenceUtils;
import com.example.accessibility.sharepre.Type;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EditTextActivity extends Activity {

    private EditText mEditText;
    private View mAddBtn;

    private List<String> mList;
    private RecyclerView mRecyclerView;
    private TextAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittext_activity);

        mList = new ArrayList<>();
        for(String txt : Constant.mTexts) {
            mList.add(txt);
        }
        mEditText = findViewById(R.id.edit_text);
        mAddBtn = findViewById(R.id.add_text);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                if(TextUtils.isEmpty(text)){
                    Toast.makeText(EditTextActivity.this, "文案不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(String txt : mList){
                    if(txt.equals(text)){
                        Toast.makeText(EditTextActivity.this, "文案不能重复", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                mList.add(text);
                mEditText.setText("");
                mAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.save_add_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> set = new LinkedHashSet<>();
                for(String str : mList){
                    set.add(str);
                }
                SharePreferenceUtils.put(SharePreferenceConstant.TEXT_SET, set, Type.STRING_SET);
                Toast.makeText(EditTextActivity.this, "已保存", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mRecyclerView = findViewById(R.id.reclcyerview_text);
         mAdapter = new TextAdapter();
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    class TextAdapter extends RecyclerView.Adapter<TextItemHolder>{

        @NonNull
        @Override
        public TextItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.edit_text_item, viewGroup, false);
            TextItemHolder holder = new TextItemHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull TextItemHolder textItemHolder, final int i) {
            textItemHolder.mDeleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(i);
                    notifyDataSetChanged();
                }
            });
            textItemHolder.mTextView.setText(mList.get(i));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    class TextItemHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public View mDeleteView;
        public TextItemHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_view);
            mDeleteView = itemView.findViewById(R.id.delete_text);
        }
    }
}
