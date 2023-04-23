package com.arktika.rfidscanner.ui.FindReplace;

import android.app.Dialog;
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
import android.view.LayoutInflater;
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
import com.arktika.rfidscanner.databinding.FragmentFindReplaceBinding;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Dialog;

public class FindReplaceFragment extends Fragment {
    String barcode;
    int metka_rfid_count = 0;
    TextView tvRfidMetkaSearchReplace;
    TextView tvRfidMetkaSearchReplaceSecond;
    Button BtClear;
    ProgressBar pbFindReplace;
    Dialog dialog;
    Integer SendCode;
    EditText spinnerFindReplace;
    Button BtLink;
    JSONArray jsonAr;
    TextView tvRfidMetkaSearchReplaceTitle;
    TextView tvRfidMetkaSearchReplaceSecondTitle;
    Integer SpinnerPosition = -1;
    TextView tvclickfindReplace;
    private FragmentFindReplaceBinding binding;
    private BroadcastReceiver brRfid;//Прием широковешательных сообщений от сканирование штрихкода

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFindReplaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvRfidMetkaSearchReplace = root.findViewById(R.id.tvRfidMetkaSearchReplace);
        pbFindReplace = root.findViewById(R.id.loadingFindReplace);
        tvRfidMetkaSearchReplaceSecond = root.findViewById(R.id.tvRfidMetkaSearchReplaceSecond);
        tvRfidMetkaSearchReplaceTitle = root.findViewById(R.id.tvRfidMetkaSearchReplaceTitle);
        tvRfidMetkaSearchReplaceSecondTitle = root.findViewById(R.id.tvRfidMetkaSearchReplaceSecondTitle);
        tvclickfindReplace = root.findViewById(R.id.tvclickfindReplace);
        BtLink = (Button) root.findViewById(R.id.btSetLinkFindReplace);
        BtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getwifistate()) {
                    if ((!tvRfidMetkaSearchReplace.getText().toString().trim().equals("")) & (!tvRfidMetkaSearchReplaceSecond.getText().toString().trim().equals("")) & (SpinnerPosition != -1)) {
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        Handler handler = new Handler(Looper.getMainLooper());
                        pbFindReplace.setVisibility(View.VISIBLE);
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    SendCode = 0;
                                    URL url = new URL(MainActivity.API_URL + "set-rfid-code?" +
                                            "techBarid=" + jsonAr.getJSONObject(SpinnerPosition).getString("TECH_BAR_ID") +
                                            "&rfidCode1=" + tvRfidMetkaSearchReplace.getText().toString() +
                                            "&rfidcode2=" + tvRfidMetkaSearchReplaceSecond.getText().toString());
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
                                        //UI Thread work here
                                        pbFindReplace.setVisibility(View.INVISIBLE);

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

        BtClear = (Button) root.findViewById(R.id.btClearFindReplace);
        BtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRfidMetkaSearchReplace.setText("");
                tvRfidMetkaSearchReplaceSecond.setText("");
                metka_rfid_count = 0;
                tvRfidMetkaSearchReplaceTitle.setVisibility(view.INVISIBLE);
                tvRfidMetkaSearchReplaceSecondTitle.setVisibility(view.INVISIBLE);
                SpinnerPosition = -1;
                spinnerFindReplace.setText("");

            }
        });
        brRfid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                barcode = intent.getStringExtra("rfid_data").replace("\n", "");
                if (metka_rfid_count == 0) {
                    if ((!tvRfidMetkaSearchReplaceSecond.getText().toString().trim().equals(barcode)) & (!tvRfidMetkaSearchReplace.getText().toString().trim().equals(barcode))) {
                        tvRfidMetkaSearchReplace.setText(barcode);
                        metka_rfid_count = 1;
                        tvRfidMetkaSearchReplaceTitle.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (!tvRfidMetkaSearchReplace.getText().toString().trim().equals(barcode)) {
                        if (!tvRfidMetkaSearchReplaceSecond.getText().toString().trim().equals(barcode)) {
                            tvRfidMetkaSearchReplaceSecond.setText(barcode);
                            metka_rfid_count = 0;
                            tvRfidMetkaSearchReplaceSecondTitle.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        };

        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
        getContext().registerReceiver(brRfid, intFilt);
        spinnerFindReplace = (EditText) root.findViewById(R.id.spinnerFindReplace);
        List<String> spinnerArray = new ArrayList<String>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        if (getwifistate()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    pbFindReplace.setVisibility(View.VISIBLE);
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
                            tvclickfindReplace.setOnClickListener(new View.OnClickListener() {
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

                                    // set adapter
                                    listView.setAdapter(adapter);
                                    editText.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            for (int i = 0; i < spinnerArray.stream().count(); i++)
                                                if (spinnerArray.get(i).indexOf(s.toString()) > 0) {
                                                    listView.setSelection(i);
                                                    break;
                                                }
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
                                            spinnerFindReplace.setText(adapter.getItem(position));
                                            SpinnerPosition = position;
                                            // Dismiss dialog
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                            pbFindReplace.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            });
        } else {
            Toast.makeText(getContext(), R.string.NetworkError, Toast.LENGTH_SHORT).show();
            pbFindReplace.setVisibility(View.INVISIBLE);
        }
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