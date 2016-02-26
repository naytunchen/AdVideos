package com.bnmla.advideos;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bnmla.advideos.Adapters.PagerAdapter;
import com.bnmla.advideos.DAOs.DataTask;

//public class MainActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener,
//        ResponseCallback, Button.OnClickListener{
public class MainActivity extends AppCompatActivity{
    private final int FALSE_INDEX = 1;
    private final int TRUE_INDEX = 0;
    private final boolean RESET = true;
    private final boolean DEFAULT = false;
    private final String HTML5 = "HTML5";
    private final String FLASH = "Flash";
    private String[] toggle_list = {"On", "Off"};

    DataTask data_task = new DataTask();
    ArrayAdapter<String> commons_adapter = null;
    ArrayAdapter<String> primary_adapter = null;
    final String TAG = MainActivity.class.getSimpleName();

    Spinner spinner, autostart_spinner, controls_spinner, primary_spinner, mute_spinner,
            repeat_spinner;
    TextView selected_tv;
    EditText width_text, height_text, aspect_ratio_text;
    boolean network_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Page"));
        tabLayout.addTab(tabLayout.newTab().setText("Player"));
        tabLayout.addTab(tabLayout.newTab().setText("Ad"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


//        selected_tv = (TextView)findViewById(R.id.selected_config_tv);
//        spinner = (Spinner)findViewById(R.id.video_spinner);
//
//        primary_spinner = (Spinner)findViewById(R.id.primary_spinner);
//        mute_spinner = (Spinner)findViewById(R.id.mute_spinner);
//        autostart_spinner = (Spinner)findViewById(R.id.autostart_spinner);
//        controls_spinner = (Spinner)findViewById(R.id.controls_spinner);
//        repeat_spinner = (Spinner)findViewById(R.id.repeat_spinner);
//
//        Button submit_btn = (Button)findViewById(R.id.submit_btn);
//        Button reset_btn = (Button)findViewById(R.id.reset_btn);
//
//        width_text = (EditText)findViewById(R.id.width_edittext);
//        height_text = (EditText)findViewById(R.id.height_edittext);
//        aspect_ratio_text = (EditText)findViewById(R.id.asepectratio_edittext);
//
//        // Creating adapter for Commons Setting dropdown list
//        commons_adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, new String[0]);
//        commons_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Create adapter for Primary dropdown list
//        primary_adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, new String[0]);
//        primary_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(commons_adapter);
//        spinner.setOnItemSelectedListener(this);
//
//        // Primary default to HTML5
//        primary_spinner.setAdapter(primary_adapter);
//        primary_spinner.setOnItemSelectedListener(this);
//
//        // Check Internet Access
//        if(network_status = NetworkUtils.get_connectivity_status(this.getApplicationContext())) {
//            // Valid Internet Connection, download data.
//            data_task.delegate = this;
//            // Download data from Cloud in new Thread running in background.
//            // execute method calls loadSpinnerLists() after successful data download.
//            data_task.execute("http://nay.bnmla.com/api/data_menu.json");
//        } else {
//            // No Internet, complains!
//            Toast.makeText(this.getApplicationContext(), "Unable to connect to the Internet",
//                    Toast.LENGTH_LONG).show();
//        }
//
//        defaultSetting(DEFAULT, network_status);
//
//        reset_btn.setOnClickListener(this);
//        submit_btn.setOnClickListener(this);

    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        int view_id = parent.getId();
//
//        if(view_id == R.id.video_spinner) {
//            spinner.setSelection(position);
//            selected_tv.setText((String) spinner.getSelectedItem());
//            if(Global.DEBUG_MODE)
//                Log.d(TAG, "commons: " + parent.getAdapter().getItem(position));
//        }
//
//        if(view_id == R.id.autostart_spinner) {
//            autostart_spinner.setSelection(position);
//            if(Global.DEBUG_MODE)
//                Log.d(TAG, "autostart: " + parent.getAdapter().getItem(position));
//        }
//
//        if(view_id == R.id.controls_spinner) {
//            controls_spinner.setSelection(position);
//            if(Global.DEBUG_MODE)
//                Log.d(TAG, "controls: " + parent.getAdapter().getItem(position));
//        }
//
//        if(view_id == R.id.primary_spinner) {
//            primary_spinner.setSelection(position);
//            if(Global.DEBUG_MODE)
//                Log.d(TAG, "primary: " + parent.getAdapter().getItem(position));
//        }
//
//        if(view_id == R.id.mute_spinner) {
//            mute_spinner.setSelection(position);
//            if(Global.DEBUG_MODE)
//                Log.d(TAG, "mute: " + parent.getAdapter().getItem(position));
//        }
//
//        if(view_id == R.id.repeat_spinner) {
//            repeat_spinner.setSelection(position);
//            if(Global.DEBUG_MODE)
//                Log.d(TAG, "repeat: " + parent.getAdapter().getItem(position));
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//    }
//
//    @Override
//    // Callback method, get data responded from cloud back to UI thread (main thread).
//    public void loadSpinnerLists(String output) {
//        DataParser.getInstance().processResult(output);
//
//        // Overwrite the common settings dropdown list.
//        commons_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
//                DataParser.getInstance().getCommonSettings());
//        commons_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Set default to index 0 for Common Settings dropdown list.
//        spinner.setAdapter(commons_adapter);
//        spinner.setSelection(0);
//
//        primary_adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item,
//                DataParser.getInstance().getVideoModes());
//        primary_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Set default to HTML for Primary dropdown list.
//        primary_spinner.setAdapter(primary_adapter);
//        spinner.setSelection(0);
//    }
//
//    private void defaultSetting(boolean reset, boolean network) {
//        ArrayAdapter<String> true_false_adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, toggle_list);
//        true_false_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Mute default to False
//        mute_spinner.setAdapter(true_false_adapter);
//        mute_spinner.setOnItemSelectedListener(this);
//        mute_spinner.setSelection(FALSE_INDEX);
//
//        // Repeat default to False
//
//        // Auto-start default to True
//        autostart_spinner.setAdapter(true_false_adapter);
//        autostart_spinner.setOnItemSelectedListener(this);
//        autostart_spinner.setSelection(TRUE_INDEX);
//
//        // Controls default to True
//        controls_spinner.setAdapter(true_false_adapter);
//        controls_spinner.setOnItemSelectedListener(this);
//        controls_spinner.setSelection(TRUE_INDEX);
//
//        // Repeat default to False
//        repeat_spinner.setAdapter(true_false_adapter);
//        repeat_spinner.setOnItemSelectedListener(this);
//        repeat_spinner.setSelection(FALSE_INDEX);
//
//        width_text.setText(R.string.default_width); // Width default to 640
//        height_text.setText(R.string.default_height); // Height default to 360
//        aspect_ratio_text.setText(null); // Aspect Ratio default to NULL
//
//        if(reset && network) {
//            spinner.setSelection(0);
//            primary_adapter = (ArrayAdapter)primary_spinner.getAdapter();
//            primary_spinner.setSelection(primary_adapter.getPosition(HTML5));
//        }
//
//        if(Global.DEBUG_MODE)
//            Log.d(TAG, "defaultSetting Method finished.");
//    }
//
//    private void submitSetting() {
//        // Configuration submitted, Magic time!
//
//        int width, height;
//        boolean autostart, mute, controls, repeat;
//        String primary = null, aspect_ratio = null;
//
//        width = Integer.parseInt(width_text.getText().toString());
//        height = Integer.parseInt(height_text.getText().toString());
//        aspect_ratio = aspect_ratio_text.getText().toString();
//
//        autostart = autostart_spinner.getSelectedItemPosition() == TRUE_INDEX ? true : false;
//        mute = mute_spinner.getSelectedItemPosition() == TRUE_INDEX ? true : false;
//        controls = controls_spinner.getSelectedItemPosition() == TRUE_INDEX ? true : false;
//        repeat = repeat_spinner.getSelectedItemPosition() == TRUE_INDEX ?  true : false;
//
//        primary = primary_spinner.getSelectedItem().toString();
//
//
//        if(Global.DEBUG_MODE) {
//            Log.d(TAG, "Submit: autostart: " + autostart);
//            Log.d(TAG, "Submit: mute: " + mute);
//            Log.d(TAG, "Submit: controls: " + controls);
//            Log.d(TAG, "Submit: repeat: " + repeat);
//            Log.d(TAG, "Submit: primary: " + primary);
//            Log.d(TAG, "Submit: aspect_ratio: " + aspect_ratio);
//            Log.d(TAG, "submitSetting Method finished.");
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        int view_id = v.getId();
//
//        switch (view_id) {
//            case R.id.reset_btn:
//                if(Global.DEBUG_MODE)
//                    Log.d(TAG, "Reset Button Clicked.");
//                defaultSetting(RESET, network_status);
//                break;
//            case R.id.submit_btn:
//                if(Global.DEBUG_MODE)
//                    Log.d(TAG, "Submit Button Clicked.");
//                submitSetting();
//                break;
//            default:
//                Log.e(TAG, "OnClick: This case shouldn't occur.");
//                break;
//        }
//    }
}
