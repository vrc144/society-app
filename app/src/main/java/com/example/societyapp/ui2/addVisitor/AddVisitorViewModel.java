package com.example.societyapp.ui2.addVisitor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddVisitorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddVisitorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add visitor fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}