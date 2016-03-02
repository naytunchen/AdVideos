package com.bnmla.advideos.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bnmla.advideos.Callbacks.ResponseCallback;
import com.bnmla.advideos.DAOs.DataTask;
import com.bnmla.advideos.Entities.Global;
import com.bnmla.advideos.Entities.Setting;
import com.bnmla.advideos.MainActivity;
import com.bnmla.advideos.R;
import com.bnmla.advideos.Utilities.DataParser;
import com.bnmla.advideos.Utilities.NetworkUtils;
import com.bnmla.advideos.VideoPlayer.VideoPlayerController;

/**
 * Created by nay on 2/26/16.
 */
public class ConfigFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        ResponseCallback, Button.OnClickListener{

    private final int FALSE_INDEX = 1;
    private final int TRUE_INDEX = 0;
    private final boolean RESET = true;
    private final boolean DEFAULT = false;
    private final String HTML5 = "HTML5";
    private final String FLASH = "Flash";
    private String[] toggle_list = {"On", "Off"};

    private final String TAG = ConfigFragment.class.getSimpleName();

    DataTask data_task = new DataTask();
    ArrayAdapter<String> commons_adapter = null;
    ArrayAdapter<String> primary_adapter = null;


    Spinner spinner, autostart_spinner, controls_spinner, primary_spinner, mute_spinner,
            repeat_spinner;
    TextView selected_tv;
    EditText width_text, height_text, aspect_ratio_text, url_text;
    boolean network_status = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.config_fragment, container, false);

        selected_tv = (TextView)view.findViewById(R.id.selected_config_tv);
        spinner = (Spinner)view.findViewById(R.id.video_spinner);

        primary_spinner = (Spinner)view.findViewById(R.id.primary_spinner);
        mute_spinner = (Spinner)view.findViewById(R.id.mute_spinner);
        autostart_spinner = (Spinner)view.findViewById(R.id.autostart_spinner);
        controls_spinner = (Spinner)view.findViewById(R.id.controls_spinner);
        repeat_spinner = (Spinner)view.findViewById(R.id.repeat_spinner);

        Button submit_btn = (Button)view.findViewById(R.id.submit_btn);
        Button reset_btn = (Button)view.findViewById(R.id.reset_btn);

        width_text = (EditText)view.findViewById(R.id.width_edittext);
        height_text = (EditText)view.findViewById(R.id.height_edittext);
        aspect_ratio_text = (EditText)view.findViewById(R.id.asepectratio_edittext);
        url_text = (EditText) view.findViewById(R.id.current_url);

        // Creating adapter for Commons Setting dropdown list
        commons_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[0]);
        commons_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Create adapter for Primary dropdown list
        primary_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[0]);
        primary_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(commons_adapter);
        spinner.setOnItemSelectedListener(this);

        // Primary default to HTML5
        primary_spinner.setAdapter(primary_adapter);
        primary_spinner.setOnItemSelectedListener(this);

        // Check Internet Access
        if(network_status = NetworkUtils.get_connectivity_status(getContext())) {
            // Valid Internet Connection, download data.
            data_task.delegate = this;
            // Download data from Cloud in new Thread running in background.
            // execute method calls loadSpinnerLists() after successful data download.
            data_task.execute("http://nay.bnmla.com/api/data_menu.json");
        } else {
            // No Internet, complains!
            Toast.makeText(getContext(),"Unable to connect to the Internet",
                    Toast.LENGTH_LONG).show();
        }

        defaultSetting(DEFAULT, network_status);

        reset_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int view_id = v.getId();

        switch (view_id) {
            case R.id.reset_btn:
                if(Global.DEBUG_MODE)
                    Log.d(TAG, "Reset Button Clicked.");
                defaultSetting(RESET, network_status);
                break;
            case R.id.submit_btn:
                if(Global.DEBUG_MODE)
                    Log.d(TAG, "Submit Button Clicked.");
                if (MainActivity.getmVideoPlayer() != null) {
                    submitSetting();
                }
                break;
            default:
                Log.e(TAG, "OnClick: This case shouldn't occur.");
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int view_id = parent.getId();

        if(view_id == R.id.video_spinner) {
            spinner.setSelection(position);
            selected_tv.setText((String) spinner.getSelectedItem());
            if(Global.DEBUG_MODE)
                Log.d(TAG, "commons: " + parent.getAdapter().getItem(position));
        }

        if(view_id == R.id.autostart_spinner) {
            autostart_spinner.setSelection(position);
            if(Global.DEBUG_MODE)
                Log.d(TAG, "autostart: " + parent.getAdapter().getItem(position));
        }

        if(view_id == R.id.controls_spinner) {
            controls_spinner.setSelection(position);
            if(Global.DEBUG_MODE)
                Log.d(TAG, "controls: " + parent.getAdapter().getItem(position));
        }

        if(view_id == R.id.primary_spinner) {
            primary_spinner.setSelection(position);
            if(Global.DEBUG_MODE)
                Log.d(TAG, "primary: " + parent.getAdapter().getItem(position));
        }

        if(view_id == R.id.mute_spinner) {
            mute_spinner.setSelection(position);
            if(Global.DEBUG_MODE)
                Log.d(TAG, "mute: " + parent.getAdapter().getItem(position));
        }

        if(view_id == R.id.repeat_spinner) {
            repeat_spinner.setSelection(position);
            if(Global.DEBUG_MODE)
                Log.d(TAG, "repeat: " + parent.getAdapter().getItem(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    // Callback method, get data responded from cloud back to UI thread (main thread).
    public void loadSpinnerLists(String output) {
        DataParser.getInstance().processResult(output);

        // Overwrite the common settings dropdown list.
        commons_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,
                DataParser.getInstance().getCommonSettings());
        commons_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set default to index 0 for Common Settings dropdown list.
        spinner.setAdapter(commons_adapter);
        spinner.setSelection(0);

        primary_adapter = new ArrayAdapter<String> (getContext(), android.R.layout.simple_spinner_item,
                DataParser.getInstance().getVideoModes());
        primary_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set default to HTML for Primary dropdown list.
        primary_spinner.setAdapter(primary_adapter);
        spinner.setSelection(0);
    }

    private void defaultSetting(boolean reset, boolean network) {
        ArrayAdapter<String> true_false_adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, toggle_list);
        true_false_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Mute default to False
        mute_spinner.setAdapter(true_false_adapter);
        mute_spinner.setOnItemSelectedListener(this);
        mute_spinner.setSelection(FALSE_INDEX);

        // Repeat default to False

        // Auto-start default to True
        autostart_spinner.setAdapter(true_false_adapter);
        autostart_spinner.setOnItemSelectedListener(this);
        autostart_spinner.setSelection(TRUE_INDEX);

        // Controls default to True
        controls_spinner.setAdapter(true_false_adapter);
        controls_spinner.setOnItemSelectedListener(this);
        controls_spinner.setSelection(TRUE_INDEX);

        // Repeat default to False
        repeat_spinner.setAdapter(true_false_adapter);
        repeat_spinner.setOnItemSelectedListener(this);
        repeat_spinner.setSelection(FALSE_INDEX);

        width_text.setText(R.string.default_width); // Width default to 640
        height_text.setText(R.string.default_height); // Height default to 360
        aspect_ratio_text.setText(null); // Aspect Ratio default to NULL

        if(reset && network) {
            spinner.setSelection(0);
            primary_adapter = (ArrayAdapter)primary_spinner.getAdapter();
            primary_spinner.setSelection(primary_adapter.getPosition(HTML5));
            resetVideoPlayer();
        }

        if(Global.DEBUG_MODE)
            Log.d(TAG, "defaultSetting Method finished.");
    }

    public void resetVideoPlayer() {
        if(MainActivity.getmVideoPlayer() != null) {
            MainActivity.setResetPressed(true);
            stopVideoContent();
        }
        url_text.setText(R.string.ad_tag_url);
        MainActivity.setCurrentContent(getString(R.string.ad_tag_url));
        MainActivity.setmVideoPlayerController(
                new VideoPlayerController(MainActivity.getApp_context(),
                        MainActivity.getmVideoPlayer(), MainActivity.getmAdUIContainer()));
    }

    public void stopVideoContent() {
        MainActivity.getmVideoPlayerController().stop();
        MainActivity.getmPlayButton().setVisibility(View.VISIBLE);
    }

    private void submitSetting() {
        // Configuration submitted, Magic time!

        int width, height;
        boolean autostart, mute, controls, repeat;
        String primary = null, aspect_ratio = null;

        width = Integer.parseInt(width_text.getText().toString());
        height = Integer.parseInt(height_text.getText().toString());
        aspect_ratio = aspect_ratio_text.getText().toString();

        autostart = autostart_spinner.getSelectedItemPosition() == TRUE_INDEX ? true : false;
        mute = mute_spinner.getSelectedItemPosition() == TRUE_INDEX ? true : false;
        controls = controls_spinner.getSelectedItemPosition() == TRUE_INDEX ? true : false;
        repeat = repeat_spinner.getSelectedItemPosition() == TRUE_INDEX ?  true : false;

        primary = primary_spinner.getSelectedItem().toString();


        if(Global.DEBUG_MODE) {
            Log.d(TAG, "Submit: autostart: " + autostart);
            Log.d(TAG, "Submit: mute: " + mute);
            Log.d(TAG, "Submit: controls: " + controls);
            Log.d(TAG, "Submit: repeat: " + repeat);
            Log.d(TAG, "Submit: primary: " + primary);
            Log.d(TAG, "Submit: aspect_ratio: " + aspect_ratio);
            Log.d(TAG, "submitSetting Method finished.");
        }

        Setting setting = new Setting(width,height,autostart,mute,controls,repeat,primary);
        MainActivity.setSetting(setting);

        String ad_url = url_text.getText().toString();

        if (ad_url == null || ad_url.isEmpty()) {
            // if URL edit text is empty, do nothing
            Toast.makeText(MainActivity.getApp_context(), "Please enter a valid URL",
                    Toast.LENGTH_LONG).show();
        } else {
            // if URL edit is entered, then update Video Content
            stopVideoContent();
            MainActivity.setCurrentContent(ad_url);
            MainActivity.setmVideoPlayerController(new VideoPlayerController(
                    MainActivity.getApp_context(), MainActivity.getmVideoPlayer(),
                    MainActivity.getmAdUIContainer(), MainActivity.getCurrentContent()));
        }

        MainActivity.setResetPressed(false);
    }
}
