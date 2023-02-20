package com.arktika.rfidscanner.ui.FindReplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arktika.rfidscanner.databinding.FragmentFindReplaceBinding;

public class FindReplaceFragment extends Fragment {

    private FragmentFindReplaceBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FindReplaceViewModel findReplaceViewModel =
                new ViewModelProvider(this).get(FindReplaceViewModel.class);

        binding = FragmentFindReplaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        findReplaceViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}