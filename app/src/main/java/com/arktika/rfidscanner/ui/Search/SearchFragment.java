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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private BroadcastReceiver brRfid;//Прием широковешательных сообщений от сканирование штрихкода
    TextView tvSearchRFidtitle;
    TextView tvRfidMetka;
    ProgressBar pbSearch;

    JSONObject obj = new JSONObject();
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
                //BtClear.setVisibility(View.INVISIBLE);
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
                        //pbSearch.setVisibility(View.VISIBLE);
                        //Background work here
//                            BtClear.setVisibility(View.VISIBLE);
                        String barcode = intent.getStringExtra("rfid_data");
                        try {
                            URL url = new URL ("http://ovis-15:5087/get-bar-by-rfid/6335"+barcode.replace("\n",""));
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            //System.out.println(HttpURLConnection);
                            con.setRequestMethod("GET");
                            con.setRequestProperty("Content-Type", "application/json; utf-8");
                            con.setRequestProperty("Accept", "application/json");
                            //          String jsonInputString = "{\"rfid\":\""+barcode+"\"}";
                            //                                        try(OutputStream os = con.getOutputStream()) {
                            //                                            byte[] input = jsonInputString.getBytes("utf-8");
                            //                                            os.write(input, 0, input.length);
                            //                                       }
                            try(BufferedReader br = new BufferedReader(
                                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                                StringBuilder response = new StringBuilder();
                                String responseLine = null;
                                while ((responseLine = br.readLine()) != null) {
                                    response.append(responseLine.trim());
                                }
                                obj = new JSONObject(response.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvRfidMetka.setText(barcode);
                                //UI Thread work here
                                try {
                                    if (!obj.isNull("UIN"))   tvSearchRFidtitle.setText(obj.getString("UIN"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
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

//    @Override
//    public void onResume() {
//        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
//        //Context context = getContext();
//        getContext().registerReceiver(brRfid, intFilt);
//        super.onResume();
//    }
}