package com.arktika.rfidscanner.ui.Search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private BroadcastReceiver brRfid;//Прием широковешательных сообщений от сканирование штрихкода
    TextView tvSearchRFidtitle;
    TextView tvRfidMetka;
    TextView tvRfidMetkaTitle;
    TextView tvSearchtitle;

    ProgressBar pbSearch;

    JSONObject obj = new JSONObject();
    boolean jsonExists = false;
    Button BtClear;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvSearchRFidtitle = (TextView) root.findViewById(R.id.tvSearchRFidtitle);
        tvRfidMetka = (TextView) root.findViewById(R.id.tvRfidMetka);
        BtClear = (Button) root.findViewById(R.id.btClearSearch);
        pbSearch = (ProgressBar) root.findViewById(R.id.loadingSearch);
        tvRfidMetkaTitle = (TextView) root.findViewById(R.id.tvRfidMetkaTitle);
        ;
        tvSearchtitle = (TextView) root.findViewById(R.id.tvSearchtitle);
        BtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSearchRFidtitle.setText("");
                tvRfidMetka.setText("");
                tvRfidMetkaTitle.setVisibility(View.INVISIBLE);
                tvSearchtitle.setVisibility(View.INVISIBLE);
                //BtClear.setVisibility(View.INVISIBLE);
            }
        });

        brRfid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getwifistate()) {
                    pbSearch.setVisibility(View.VISIBLE);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Handler handler = new Handler(Looper.getMainLooper());
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            String barcode = intent.getStringExtra("rfid_data").replace("\n", "");
                            try {
                                URL url = new URL(MainActivity.API_URL+"get-bar-by-rfid/" + barcode);
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                con.setRequestMethod("GET");
                                con.setRequestProperty("Content-Type", "application/json; utf-8");
                                con.setRequestProperty("Accept", "application/json");
                                try (BufferedReader br = new BufferedReader(
                                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                                    StringBuilder response = new StringBuilder();
                                    String responseLine = null;
                                    while ((responseLine = br.readLine()) != null) {
                                        response.append(responseLine.trim());
                                    }
                                    if (!response.toString().equals("")) {
                                        jsonExists = true;
                                        obj = new JSONObject(response.toString());
                                    } else jsonExists = false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvRfidMetkaTitle.setVisibility(View.VISIBLE);

                                    tvRfidMetka.setText(barcode);
                                    if (jsonExists) try {
                                        tvSearchRFidtitle.setText(obj.getString("UIN"));
                                        tvSearchtitle.setVisibility(View.VISIBLE);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    pbSearch.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    });
                } else {
                    Toast.makeText(getContext(), R.string.NetworkError, Toast.LENGTH_SHORT).show();
                    pbSearch.setVisibility(View.INVISIBLE);
                }
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
        try {
            getContext().unregisterReceiver(brRfid);
        } catch (IllegalArgumentException e) {
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

    private boolean getwifistate() {
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (mWifi.isConnected());
    }
}