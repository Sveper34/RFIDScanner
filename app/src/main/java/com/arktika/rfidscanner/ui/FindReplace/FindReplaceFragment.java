package com.arktika.rfidscanner.ui.FindReplace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arktika.rfidscanner.MainActivity;
import com.arktika.rfidscanner.databinding.FragmentFindReplaceBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindReplaceFragment extends Fragment {

    private FragmentFindReplaceBinding binding;
    private BroadcastReceiver brRfid;//Прием широковешательных сообщений от сканирование штрихкода
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FindReplaceViewModel findReplaceViewModel =
                new ViewModelProvider(this).get(FindReplaceViewModel.class);

        binding = FragmentFindReplaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        brRfid = new BroadcastReceiver() {
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