package com.arktika.rfidscanner.ui.Link;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arktika.rfidscanner.MainActivity;
import com.arktika.rfidscanner.databinding.FragmentLinkBinding;

public class LinkFragment extends Fragment {

    private FragmentLinkBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        linkViewModel linkViewModel =
                new ViewModelProvider(this).get(linkViewModel.class);

        binding = FragmentLinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //                pbSearch.setVisibility(View.VISIBLE);
        //
        //                ExecutorService executor = Executors.newSingleThreadExecutor();
        //                Handler handler = new Handler(Looper.getMainLooper());
        //                executor.execute(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        //Background work here
        //                        for (int i =0;i<10000;i++)
        //                            handler.post(new Runnable() {
        //                                @Override
        //                                public void run() {
        //                                    //UI Thread work here
        //                                    pbSearch.setVisibility(View.INVISIBLE);
        //                                }
        //                            });
        //                    }
        //                });
        //                BtClear.setOnClickListener(new View.OnClickListener() {
        //                    @Override
        //                    public void onClick(View view) {
        //                        tvSearchRFidtitle.setText("");
        //                        tvRfidMetka.setText("");
        //                        BtClear.setVisibility(View.INVISIBLE);
        //                    }
        //                });
        //                String barcode = intent.getStringExtra("rfid_data");
        //                //Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_SHORT).show();
        //                BtClear.setVisibility(View.VISIBLE);
        //                if (barcode.equals("ABCDEF0000001938\n")) {
        //                    tvSearchRFidtitle.setText("318");
        //                }else {
        //                    tvSearchRFidtitle.setText("");
        //
        //                }
        //                tvRfidMetka.setText(barcode);
        //   tvNumberDate.setText(barcode);
        //Прием широковешательных сообщений от сканирование штрихкода
        BroadcastReceiver brRfid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                pbSearch.setVisibility(View.VISIBLE);
//
//                ExecutorService executor = Executors.newSingleThreadExecutor();
//                Handler handler = new Handler(Looper.getMainLooper());
//                executor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Background work here
//                        for (int i =0;i<10000;i++)
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //UI Thread work here
//                                    pbSearch.setVisibility(View.INVISIBLE);
//                                }
//                            });
//                    }
//                });
//                BtClear.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        tvSearchRFidtitle.setText("");
//                        tvRfidMetka.setText("");
//                        BtClear.setVisibility(View.INVISIBLE);
//                    }
//                });
//                String barcode = intent.getStringExtra("rfid_data");
//                //Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_SHORT).show();
//                BtClear.setVisibility(View.VISIBLE);
//                if (barcode.equals("ABCDEF0000001938\n")) {
//                    tvSearchRFidtitle.setText("318");
//                }else {
//                    tvSearchRFidtitle.setText("");
//
//                }
//                tvRfidMetka.setText(barcode);
                //   tvNumberDate.setText(barcode);
            }
        };
        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
        //Context context = getContext();
        getContext().registerReceiver(brRfid, intFilt);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}