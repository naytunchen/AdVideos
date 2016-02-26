package com.bnmla.advideos.DAOs;

import android.os.AsyncTask;
import android.util.Log;

import com.bnmla.advideos.Callbacks.ResponseCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nay on 2/25/16.
 */
public class DataTask extends AsyncTask<String, Void, String> {
    public ResponseCallback delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection con = null;
        BufferedReader reader = null;
        String data_json = null;

        try {
            URL url = new URL(params[0]);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            InputStream input = con.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(input == null){
                data_json = null;
            }

            reader = new BufferedReader(new InputStreamReader(input));

            String line;
            while((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }

            if(buffer.length() == 0) {
                data_json = null;
            } else {
                data_json = buffer.toString();
            }

        } catch(IOException e) {
            Log.e("DataTask", "Error", e);
            data_json = null;
        } finally {
            if(con != null) {
                con.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final Exception e) {
                    Log.e("DataTask", "Error closing stream",e);
                }
            }
        }
        return data_json;
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.loadSpinnerLists(s);
    }
}
