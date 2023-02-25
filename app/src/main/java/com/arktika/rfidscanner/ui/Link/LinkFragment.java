package com.arktika.rfidscanner.ui.Link;

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
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arktika.rfidscanner.MainActivity;
import com.arktika.rfidscanner.R;
import com.arktika.rfidscanner.databinding.FragmentLinkBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LinkFragment extends Fragment {

    private FragmentLinkBinding binding;
    private BroadcastReceiver brRfid;
    ProgressBar pbLink;
    Button BtClear;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        linkViewModel linkViewModel =
                new ViewModelProvider(this).get(linkViewModel.class);

        binding = FragmentLinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pbLink = (ProgressBar)  root.findViewById(R.id.loadingSearch);
        brRfid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pbLink.setVisibility(View.VISIBLE);
                BtClear= (Button)  root.findViewById(R.id.btClearLink);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        pbLink.setVisibility(View.VISIBLE);
                        //Background work here
                        String barcode = intent.getStringExtra("rfid_data");
                        //Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_SHORT).show();
                        BtClear.setVisibility(View.VISIBLE);
                        if (barcode.equals("ABCDEF0000001938\n")) {
                           // tvSearchRFidtitle.setText("318");
                        }else {
                            //tvSearchRFidtitle.setText("");

                        }
                        //tvRfidMetka.setText(barcode);
                        //   tvNumberDate.setText(barcode);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //UI Thread work here
                                pbLink.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }
        };
        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
        //Context context = getContext();
        getContext().registerReceiver(brRfid, intFilt);

        return root;
    }

    @Override
    public void onDestroyView() {
        getContext().unregisterReceiver(brRfid);
        super.onDestroyView();
        binding = null;
    }
}