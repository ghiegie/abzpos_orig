package com.abztrakinc.ABZPOS;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
   public MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    public void setText(String s){
        mutableLiveData.setValue(s);
    }
    public MutableLiveData<String> getText(){
        return mutableLiveData;
    }
}
