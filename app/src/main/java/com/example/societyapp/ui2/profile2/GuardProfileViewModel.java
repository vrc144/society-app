package com.example.societyapp.ui2.profile2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GuardProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public GuardProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is guard profile fragment guard");
    }

    public LiveData<String> getText() {
        return mText;
    }
}