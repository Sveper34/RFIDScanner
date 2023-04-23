package com.arktika.rfidscanner.ui.Link;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arktika.rfidscanner.MainActivity;
import com.arktika.rfidscanner.R;
import com.arktika.rfidscanner.databinding.FragmentLinkBinding;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Dialog;

public class LinkFragment extends Fragment {

    private FragmentLinkBinding binding;
    private BroadcastReceiver brRfid;
    ProgressBar pbLink;
    Button BtClear;
    Button BtLink;
    TextView tvLinkMetka;
    TextView tvLinkMetkaSecond;
    //Spinner spinnerLink;
    EditText spinnerLink;
    TextView tvclick;
    Integer PostionSpinner;
    Dialog dialog;
    String barcode;
    JSONArray jsonAr;
    int metka_rfid_count = 0;
    int SendCode = 0;
    TextView tvRfidMetkaLinkTitle;
    TextView tvRfidMetkaLinkSecondTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        linkViewModel linkViewModel =
                new ViewModelProvider(this).get(linkViewModel.class);
        PostionSpinner = -1;
        binding = FragmentLinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pbLink = (ProgressBar) root.findViewById(R.id.loadingLink);
        tvLinkMetka = (TextView) root.findViewById(R.id.tvRfidMetkaLink);
        tvLinkMetkaSecond = (TextView) root.findViewById(R.id.tvRfidMetkaLinkSecond);
        tvRfidMetkaLinkTitle = (TextView) root.findViewById(R.id.tvRfidMetkaLinkTitle);
        tvRfidMetkaLinkSecondTitle = (TextView) root.findViewById(R.id.tvRfidMetkaLinkSecondTitle);
        BtClear = (Button) root.findViewById(R.id.btClearLink);
        //spinnerLink = root.findViewById(R.id.spinnerLink);
        spinnerLink = root.findViewById(R.id.spinnerLink);
        tvclick = root.findViewById(R.id.tvclick);
        ArrayList<String> spinnerArray = new ArrayList<String>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        if (getwifistate()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    pbLink.setVisibility(View.VISIBLE);
                    try {
                        URL url = new URL(MainActivity.API_URL + "get-bar-list-rfid/");
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
                            jsonAr = new JSONArray(response.toString());
                            for (int i = 0; i < jsonAr.length(); i++) {
                                if (!jsonAr.getJSONObject(i).getString("TECH_BAR_NUMBER").equals("null"))
                                    spinnerArray.add(jsonAr.getJSONObject(i).getString("TECH_BAR_NUMBER"));
                                else spinnerArray.add("");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                            //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //spinnerLink.setAdapter(dataAdapter);
                            tvclick.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Initialize dialog
                                    dialog = new Dialog(getContext());

                                    // set custom dialog
                                    dialog.setContentView(R.layout.custom_spinner_dropdown_item_selected);

                                    // set custom height and width
                                    dialog.getWindow().setLayout(650, 800);

                                    // set transparent background
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    // show dialog
                                    dialog.show();

                                    // Initialize and assign variable
                                    EditText editText = dialog.findViewById(R.id.edit_text);
                                    ListView listView = dialog.findViewById(R.id.list_view);

                                    // Initialize array adapter
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, spinnerArray);

                                    adapter.notifyDataSetChanged();
                                    // set adapter
                                    listView.setAdapter(adapter);
                                    editText.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            for (int i=0;i<spinnerArray.stream().count();i++)
                                                if(spinnerArray.get(i).indexOf(s.toString())>0){
                                                    listView.setSelection(i);
                                                    break;
                                                }
                                            //adapter.getFilter().filter(s);
                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {

                                        }
                                    });

                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // when item selected from list
                                            // set selected item on textView
                                            spinnerLink.setText(adapter.getItem(position));
                                            PostionSpinner = position;
                                            // Dismiss dialog
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                            pbLink.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });
        } else {
            Toast.makeText(getContext(), R.string.NetworkError, Toast.LENGTH_SHORT).show();
        }

        brRfid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                barcode = intent.getStringExtra("rfid_data").replace("\n", "");
                if (metka_rfid_count == 0) {
                    if ((!tvLinkMetkaSecond.getText().toString().trim().equals(barcode)) & (!tvLinkMetka.getText().toString().trim().equals(barcode))) {
                        tvLinkMetka.setText(barcode);
                        metka_rfid_count = 1;
                        tvRfidMetkaLinkTitle.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (!tvLinkMetka.getText().toString().trim().equals(barcode)) {
                        if (!tvLinkMetkaSecond.getText().toString().trim().equals(barcode)) {
                            tvLinkMetkaSecond.setText(barcode);
                            metka_rfid_count = 0;
                            tvRfidMetkaLinkSecondTitle.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };

        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
        getContext().registerReceiver(brRfid, intFilt);

        BtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLinkMetka.setText("");
                tvLinkMetkaSecond.setText("");
                metka_rfid_count = 0;
                tvRfidMetkaLinkTitle.setVisibility(View.INVISIBLE);
                tvRfidMetkaLinkSecondTitle.setVisibility(View.INVISIBLE);
                spinnerLink.setText("");
                PostionSpinner = -1;
            }
        });
        BtLink = (Button) root.findViewById(R.id.btSetLink);
        BtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getwifistate()) {
                    if (!tvLinkMetkaSecond.getText().toString().trim().equals("") &
                            (!tvLinkMetka.getText().toString().trim().equals("")) &
                            (PostionSpinner != -1)) {
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Handler handler = new Handler(Looper.getMainLooper());
                        pbLink.setVisibility(View.VISIBLE);
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    SendCode = 0;
                                    URL url = new URL(MainActivity.API_URL + "set-rfid-code?" +
                                            "techBarid=" + jsonAr.getJSONObject(PostionSpinner).getString("TECH_BAR_ID") +
                                            "&rfidCode1=" + tvLinkMetka.getText().toString() +
                                            "&rfidcode2=" + tvLinkMetkaSecond.getText().toString());
                                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                    con.setRequestMethod("POST");
                                    con.setRequestProperty("Content-Type", "application/json; utf-8");
                                    con.setRequestProperty("Accept", "*/*");
                                    try (BufferedReader br = new BufferedReader(
                                            new InputStreamReader(con.getInputStream(), "utf-8"))) {
                                        StringBuilder response = new StringBuilder();
                                        String responseLine = null;
                                        SendCode = con.getResponseCode();
                                        while ((responseLine = br.readLine()) != null) {
                                            response.append(responseLine.trim());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (SendCode == 200 || SendCode == 204) {
                                            Toast.makeText(getContext(), R.string.SuccessfullRequest, Toast.LENGTH_SHORT).show();
                                            BtClear.callOnClick();
                                        } else
                                            Toast.makeText(getContext(), R.string.BadRequest, Toast.LENGTH_SHORT).show();
                                        pbLink.setVisibility(View.INVISIBLE);

                                    }
                                });
                            }
                        });
                    } else
                        Toast.makeText(getContext(), R.string.ErorrNotAllFill, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.NetworkError, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        } catch (NullPointerException e) {
            //пустота
        } catch (IllegalArgumentException e) {
            // e.printStackTrace();
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