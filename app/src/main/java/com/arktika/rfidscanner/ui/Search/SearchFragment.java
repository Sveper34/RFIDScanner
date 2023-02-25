package com.arktika.rfidscanner.ui.Search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arktika.rfidscanner.MainActivity;
import com.arktika.rfidscanner.R;
import com.arktika.rfidscanner.databinding.FragmentSearchBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private BroadcastReceiver brRfid;//Прием широковешательных сообщений от сканирование штрихкода
    TextView tvSearchRFidtitle;
    TextView tvRfidMetka;
    ProgressBar pbSearch;
    Button BtClear;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvSearchRFidtitle = (TextView) root.findViewById(R.id.tvSearchRFidtitle);
        tvRfidMetka = (TextView)  root.findViewById(R.id.tvRfidMetka);
        BtClear= (Button)  root.findViewById(R.id.btClearSearch);
        pbSearch = (ProgressBar)  root.findViewById(R.id.loadingSearch);
        BtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSearchRFidtitle.setText("");
                tvRfidMetka.setText("");
                BtClear.setVisibility(View.INVISIBLE);
            }
        });

        brRfid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pbSearch.setVisibility(View.VISIBLE);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        pbSearch.setVisibility(View.VISIBLE);
                        //Background work here
                        String barcode = intent.getStringExtra("rfid_data");
                        //Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_SHORT).show();
                        BtClear.setVisibility(View.VISIBLE);
                        if (barcode.equals("ABCDEF0000001938\n")) {
                            tvSearchRFidtitle.setText("318");
                        }else {
                            tvSearchRFidtitle.setText("");

                        }
                        tvRfidMetka.setText(barcode);
                        //   tvNumberDate.setText(barcode);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //UI Thread work here
                                    pbSearch.setVisibility(View.INVISIBLE);
                                }
                            });
                    }
                });
            }
        };
        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
        //Context context = getContext();
        getContext().registerReceiver(brRfid, intFilt);
        //final TextView textView = binding.textHome;
        //searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        getContext().unregisterReceiver(brRfid);
        super.onDestroyView();
        binding = null;

    }
}