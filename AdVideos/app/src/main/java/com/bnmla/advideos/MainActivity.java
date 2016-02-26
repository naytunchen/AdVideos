package com.bnmla.advideos;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bnmla.advideos.Adapters.PagerAdapter;
import com.bnmla.advideos.Entities.Global;
import com.bnmla.advideos.Entities.Setting;
import com.bnmla.advideos.Fragments.VideoFragment;

//public class MainActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener,
//        ResponseCallback, Button.OnClickListener{
public class MainActivity extends AppCompatActivity{

    final String TAG = MainActivity.class.getSimpleName();
    private static Setting setting;
    private ViewPager viewPager;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Configuration"));
        tabLayout.addTab(tabLayout.newTab().setText("Video Player"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        // TODO: do some JWPlayer configuration stuffs.
                        updateVideoFragment(tab.getPosition());
                        break;
                    default:
                        break;
                }

                if(Global.DEBUG_MODE)
                    Log.d(TAG, "tab: " + tab.getText() + " position: " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public static Setting getSetting() {
        return setting;
    }

    public static void setSetting(Setting setting) {
        setting = setting;
    }

    private void updateVideoFragment(int position) {
        if(Global.DEBUG_MODE)
            Log.d(TAG, "updateVideoFragment Method called");

        VideoFragment v_fragment =
                (VideoFragment)adapter.instantiateItem(viewPager, position);
        Log.e(TAG, v_fragment.toString());
        View view = v_fragment.getView();
        ((TextView)view.findViewById(R.id.test_text)).setText("NAY KG LAR LOE");
    }

}
