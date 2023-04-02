package com.arktika.rfidscanner.ui.Link;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arktika.rfidscanner.MainActivity;
import com.arktika.rfidscanner.R;
import com.arktika.rfidscanner.databinding.FragmentLinkBinding;

import java.util.ArrayList;
import java.util.List;

public class LinkFragment extends Fragment {

    private FragmentLinkBinding binding;
    private BroadcastReceiver brRfid;
    ProgressBar pbLink;
    Button BtClear;
    Button BtLink;
    TextView tvLinkMetka;
    TextView tvLinkMetkaSecond;
    Spinner spinnerLink;
    String barcode;
    int metka_rfid_count=0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        linkViewModel linkViewModel =
                new ViewModelProvider(this).get(linkViewModel.class);

        binding = FragmentLinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pbLink = (ProgressBar)  root.findViewById(R.id.loadingLink);
        tvLinkMetka = (TextView) root.findViewById(R.id.tvRfidMetkaLink);
        tvLinkMetkaSecond = (TextView) root.findViewById(R.id.tvRfidMetkaLinkSecond);
        BtClear= (Button)  root.findViewById(R.id.btClearLink);
        pbLink = (ProgressBar)  root.findViewById(R.id.loadingLink);
        spinnerLink= (Spinner) root.findViewById(R.id.spinnerLink);
        brRfid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                 barcode = intent.getStringExtra("rfid_data");
                 if(metka_rfid_count==0){
                     tvLinkMetka.setText(barcode);
                     metka_rfid_count=1;
                 }
                 else
                 {
                     tvLinkMetkaSecond.setText(barcode);
                     metka_rfid_count=0;
                 }
            }
        };

        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
        //Context context = getContext();
        getContext().registerReceiver(brRfid, intFilt);

        BtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLinkMetka.setText("");
                tvLinkMetkaSecond.setText("");
                //BtClear.setVisibility(View.INVISIBLE);
            }
        });
        BtLink= (Button)  root.findViewById(R.id.btSetLink);
        BtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //бэк
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        try {
            getContext().unregisterReceiver(brRfid);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }

        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
        //Context context = getContext();
        getContext().registerReceiver(brRfid, intFilt);
        super.onResume();
    }

    @Override
    public void onPause() {
        try {
            getContext().unregisterReceiver(brRfid);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        super.onPause();
        binding = null;
    }
}