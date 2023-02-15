package com.arktika.rfidscanner.ui.Link;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class linkViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public linkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}