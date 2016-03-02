package com.bnmla.advideos.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nay on 3/2/16.
 */
public class VASTDownloadTask extends AsyncTask<String, Integer, Integer> {
    private static final String TAG = VASTDownloadTask.class.getSimpleName();
    private VASTResponseHandler vast_handler;

    public VASTDownloadTask(VASTResponseHandler handler)  {
        this.vast_handler = handler;
    }

    @Override
    protected Integer doInBackground(String... urls) {
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() >= 400) {
                vast_handler.onFailure(con.getResponseMessage());
                return con.getResponseCode();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            if (builder.length() > 0) {
                vast_handler.onSuccessResponse(builder.toString());
                return con.getResponseCode();
            } else {
                vast_handler.onEmptyResponse();
                return con.getResponseCode();
            }

        } catch (Exception e) {
            Log.e(TAG, "doInBackground", e);
            vast_handler.onFailure(e.toString());
            return -1;
        }
    }

    public interface VASTResponseHandler {
        void onSuccessResponse(String response);
        void onEmptyResponse();
        void onFailure(String response);
    }
}
