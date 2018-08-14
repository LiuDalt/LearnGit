package com.example.administrator.aac;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class AACFragment1 extends Fragment {

    private View mRootView;
    private ShareViewModel mModel;
    private TextView mValueTv;
    private Observer<String> mObserver;

    public AACFragment1() {
        // Required empty public constructor
    }
    public static AACFragment1 newInstance(String param1, String param2) {
        AACFragment1 fragment = new AACFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_aacfragment1, container, false);

        init();
        return mRootView;
    }


    public void init() {
        mValueTv = mRootView.findViewById(R.id.value_tve);
        mModel = ViewModelProviders.of((FragmentActivity) getContext()).get(ShareViewModel.class);
        //直接观察viewmodel的livedata，观察数据变化
//        mModel.getData().observe((FragmentActivity) getContext(), new Observer<Data>() {
//            @Override
//            public void onChanged(@Nullable Data data) {
//                String unit = data.getUnit1();
//                mValueTv.setText(data.getNum() + unit);
//            }
//        });

        //观察viewmodel的Transformations.map（） data，可以得到转换的String
        mObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String data) {
                mValueTv.setText(data);
            }
        };
        mModel.getSitchMapData().observe(getActivity(), mObserver);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        //移除observer
        mModel.getSitchMapData().removeObserver(mObserver);
    }

    private static void updateFrom14(SQLiteDatabase db) {
        try {
            String SQL_ADD_COLUMN = "ALTER TABLE " + "aaa" + " ADD COLUMN " + "aaaaaa" + " INTEGER default 0";
            db.execSQL(SQL_ADD_COLUMN);
            String SQL_DROP_CONSTRAINT = "ALTER TABLE " + "aaa" + " DROP CONSTRAINT " + 1;
            db.execSQL(SQL_DROP_CONSTRAINT);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
}