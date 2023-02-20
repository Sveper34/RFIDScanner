package com.arktika.rfidscanner.ui.FindReplace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FindReplaceViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FindReplaceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}