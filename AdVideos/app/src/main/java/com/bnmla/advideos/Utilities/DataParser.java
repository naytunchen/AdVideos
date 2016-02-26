package com.bnmla.advideos.Utilities;

import android.util.Log;

import com.bnmla.advideos.Entities.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nay on 2/25/16.
 */
public class DataParser {
    private static DataParser instance = null;
    private ArrayList<String> screen_sizes = null;
    private ArrayList<String> play_modes = null;
    private ArrayList<String> sound_modes = null;
    private ArrayList<String> settings = null;
    private ArrayList<String> video_modes = null;
    private ArrayList<String> vpaid_version = null;
    private ArrayList<String> ad_sources = null;
    private String TAG = DataParser.class.getSimpleName();

    private DataParser() {
    }

    public static synchronized DataParser getInstance() {
        if(instance == null) {
            instance = new DataParser();
        }
        return instance;
    }

    public void processResult(String response) {
        //Log.e(TAG, response);

        if(screen_sizes == null) screen_sizes = new ArrayList<String>();
        if(play_modes == null) play_modes = new ArrayList<String>();
        if(sound_modes == null) sound_modes = new ArrayList<String>();
        if(settings == null) settings = new ArrayList<String>();
        if(video_modes == null) video_modes = new ArrayList<String>();
        if(vpaid_version == null) vpaid_version = new ArrayList<String>();
        if(ad_sources == null) ad_sources = new ArrayList<String>();

        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("size");
            for(int i = 0; i < arr.length(); i++) {
                getScreenSizes().add(arr.getString(i));
                if(Global.DATAPARSER_DEBUG_MODE)
                    Log.d(TAG, getScreenSizes().toString());
            }

            arr = obj.getJSONArray("play_mode");
            for(int i = 0; i < arr.length(); i++) {
                getPlayMode().add(arr.getString(i));
                if(Global.DATAPARSER_DEBUG_MODE)
                    Log.d(TAG, getPlayMode().toString());
            }

            arr = obj.getJSONArray("sound_mode");
            for(int i = 0; i < arr.length(); i++) {
                getSoundMode().add(arr.getString(i));
                if(Global.DATAPARSER_DEBUG_MODE)
                    Log.d(TAG, getSoundMode().toString());
            }

            arr = obj.getJSONArray("commons");
            for(int i = 0; i < arr.length(); i++) {
                getCommonSettings().add(arr.getString(i));
                if(Global.DATAPARSER_DEBUG_MODE)
                    Log.d(TAG, getCommonSettings().toString());
            }

            arr = obj.getJSONArray("mode");
            for(int i = 0; i < arr.length(); i++) {
                getVideoModes().add(arr.getString(i));
                if(Global.DATAPARSER_DEBUG_MODE)
                    Log.d(TAG, getVideoModes().toString());
            }

            arr = obj.getJSONArray("vpaid_version");
            for(int i = 0; i < arr.length(); i++) {
                getVPAIDVersion().add(arr.getString(i));
                if(Global.DATAPARSER_DEBUG_MODE)
                    Log.d(TAG, getVPAIDVersion().toString());
            }

            arr = obj.getJSONArray("ad_source");
            for(int i = 0; i < arr.length(); i++) {
                getAdSources().add(arr.getString(i));
                if(Global.DATAPARSER_DEBUG_MODE)
                    Log.d(TAG, getAdSources().toString());
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error", e);
        }
    }

    public ArrayList<String> getScreenSizes() {
        return screen_sizes;
    }

    public ArrayList<String> getPlayMode() {
        return play_modes;
    }

    public ArrayList<String> getSoundMode() {
        return sound_modes;
    }

    public ArrayList<String> getCommonSettings() {
        return settings;
    }

    public ArrayList<String> getVideoModes() {
        return video_modes;
    }

    public ArrayList<String> getVPAIDVersion() {
        return vpaid_version;
    }

    public ArrayList<String> getAdSources() {
        return ad_sources;
    }
}
