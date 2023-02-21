package com.arktika.rfidscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.loader.content.AsyncTaskLoader;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.arktika.rfidscanner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private BroadcastReceiver brRfid;//Прием широковешательных сообщений от сканирование штрихкода
    public final static String BROADCAST_ACTION = "com.ubx.scan.rfid";//Широковешательное сообщение для сканера Urovo dt50

    TextView tvSearchRFidtitle;
    TextView tvRfidMetka;
    Button BtClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_search, R.id.nav_find_replace_rfid, R.id.nav_link_rfid)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        tvSearchRFidtitle = (TextView) findViewById(R.id.tvSearchRFidtitle);
        tvRfidMetka = (TextView) findViewById(R.id.tvRfidMetka);
        BtClear= (Button) findViewById(R.id.btClear);

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
                /* AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // All your networking logic
                        // should be here
                    }
                });
                */
                String barcode = intent.getStringExtra("rfid_data");
                Toast.makeText(MainActivity.this, barcode, Toast.LENGTH_SHORT).show();
                BtClear.setVisibility(View.VISIBLE);
                if (barcode.equals("ABCDEF0000001938\n")) {
                    tvSearchRFidtitle.setText("318");
                }else {
                    tvSearchRFidtitle.setText("");

                }
                tvRfidMetka.setText(barcode);
                //   tvNumberDate.setText(barcode);
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(brRfid, intFilt);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}