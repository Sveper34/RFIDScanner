package com.arktika.rfidscanner.data;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class loginAsyncTask extends AsyncTask<String, Integer, String[]> {

    private ProgressBar mProgressBar;

    public loginAsyncTask(ProgressBar target) {
        mProgressBar = target;
        mProgressBar.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        mProgressBar.setProgress(values[1]);
    }
    @Override
    protected String[] doInBackground(String... params) {
        String[] Result = new String[2];
        try {
            URL url = new URL ("login.php");//
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            String jsonInputString = "{\"email\":\""+params[0]+"\", \"password\": \""+params[1]+"\"}";
            //String jsonInputString = "{email:"+params[0]+",password: "+params[1]+"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                String json = response.toString();
                JSONObject obj = new JSONObject(json);
                Result[0]= obj.getString("bmu_id");
                Result[1]= obj.getString("bmu_login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }
}