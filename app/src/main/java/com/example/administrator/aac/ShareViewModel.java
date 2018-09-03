package com.example.administrator.aac;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {
    private MutableLiveData<Data> mData = new MutableLiveData<>();

    //采用非Lambda表达式的定义方式
//    private LiveData<String> mMapData = Transformations.map(mData, new Function<Data, String>() {
//        @Override
//        public String apply(Data value) {
//            return value.getNum() + value.getUnit1() + "---" + value.getUnit2();
//        }
//    });

    //采用Lambda表达式做Transformations.map
    private LiveData<String> mMapData = Transformations.map(mData, value -> {
        return value.getNum() + value.getUnit1() + "---" + value.getUnit2();
    });

    //采用Lambda表达式做Transformations.switchMap
    private LiveData<String> mSitchMapData = Transformations.switchMap(mData, value -> {
        MutableLiveData<String> dataLiveData = new MutableLiveData<>();
        dataLiveData.setValue(value.getNum() + value.getUnit1() + "/" + value.getUnit2());
        return dataLiveData;
    });

    public LiveData<String> getSitchMapData() {
        return mSitchMapData;
    }

    public LiveData<String> getMapData() {
        return mMapData;
    }

    public ShareViewModel(){
        setData(new Data());
    }

    public void setData(Data data) {
        mData.setValue(data);
    }

    public void setValue(int value){
        mData.getValue().setValue(value);
    }

    public MutableLiveData<Data> getData() {
        return mData;
    }
}
