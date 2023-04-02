package com.arktika.rfidscanner.ui.FindReplace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.arktika.rfidscanner.MainActivity;
import com.arktika.rfidscanner.R;
import com.arktika.rfidscanner.databinding.FragmentFindReplaceBinding;

import org.w3c.dom.Text;

public class FindReplaceFragment extends Fragment {
    String barcode;
    int metka_rfid_count=0;
    TextView tvRfidMetkaSearchReplace;
    TextView tvRfidMetkaSearchReplaceSecond;
    Button BtClear;
    Button BtLink;
    private FragmentFindReplaceBinding binding;
    private BroadcastReceiver brRfid;//Прием широковешательных сообщений от сканирование штрихкода
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FindReplaceViewModel findReplaceViewModel =
                new ViewModelProvider(this).get(FindReplaceViewModel.class);

        binding = FragmentFindReplaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        tvRfidMetkaSearchReplace = root.findViewById(R.id.tvRfidMetkaSearchReplace);;
        tvRfidMetkaSearchReplaceSecond = root.findViewById(R.id.tvRfidMetkaSearchReplaceSecond);
        BtLink = (Button)  root.findViewById(R.id.btSetLinkFindReplace);
        BtClear= (Button)  root.findViewById(R.id.btClearFindReplace);
        BtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvRfidMetkaSearchReplace.setText("");
                tvRfidMetkaSearchReplaceSecond.setText("");
            }
        });
        brRfid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                barcode = intent.getStringExtra("rfid_data");
                if(metka_rfid_count==0){
                    tvRfidMetkaSearchReplace.setText(barcode);
                    metka_rfid_count=1;
                }
                else
                {
                    tvRfidMetkaSearchReplaceSecond.setText(barcode);
                    metka_rfid_count=0;
                }
            }
        };
        IntentFilter intFilt = new IntentFilter(MainActivity.BROADCAST_ACTION);
        getContext().registerReceiver(brRfid, intFilt);
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