package com.bnmla.advideos;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bnmla.advideos.Adapters.PagerAdapter;
import com.bnmla.advideos.Entities.Global;
import com.bnmla.advideos.Entities.Setting;
import com.bnmla.advideos.Fragments.VideoFragment;
import com.bnmla.advideos.VideoPlayer.VideoPlayer;
import com.bnmla.advideos.VideoPlayer.VideoPlayerController;

public class MainActivity extends AppCompatActivity{

    final String TAG = MainActivity.class.getSimpleName();
    private static Setting setting;
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private int index = 0;

    private static VideoPlayerController mVideoPlayerController;
    private static VideoPlayer mVideoPlayer;
    private static ViewGroup mAdUIContainer;
    private static LinearLayout videoLayoutContainer;
    private static ImageButton mPlayButton;
    private static Context app_context = null;
    private static TabLayout tab_layout;

    private static String current_content_url = null;
    private static boolean resetPressed = false;
    private static boolean first_launched = true;
    private static boolean video_tab = false;

    OrientationEventListener orientationEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.configuration_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.video_player_title));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        app_context = this;

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        video_tab = false;
                        break;
                    case 1:
                        updateVideoFragment(tab.getPosition(), MainActivity.getCurrentContent());
                        video_tab = true;
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

    private void updateVideoFragment(int position, String content_url) {
        if(Global.DEBUG_MODE)
            Log.d(TAG, "updateVideoFragment Method called");

        VideoFragment v_fragment =
                (VideoFragment)adapter.instantiateItem(viewPager, position);
        View view = v_fragment.getView();

        this.videoLayoutContainer = (LinearLayout) view.findViewById(R.id.videoOverlay);
        this.mVideoPlayer = (VideoPlayer) view.findViewById(R.id.videoPlayer);
        this.mAdUIContainer = ((ViewGroup) view.findViewById(R.id.videoPlayerWithAdPlayback));
        this.mPlayButton = ((ImageButton) view.findViewById(R.id.playButton));

        if(content_url == null && first_launched) {
            setmVideoPlayerController(new VideoPlayerController(this, mVideoPlayer, mAdUIContainer));
            MainActivity.setCurrentContent(getString(R.string.ad_tag_url));
            first_launched = false;
            //getmVideoPlayerController().setContentVideo(getString(R.string.content_url));
        }

        this.mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (MainActivity.isResetPressed()) {
                    Toast.makeText(getApp_context(),"Please press submit to load the latest Ad Video.", Toast.LENGTH_LONG).show() ;
                } else {*/
                MainActivity.getmVideoPlayerController().play();
                MainActivity.mPlayButton.setVisibility(View.GONE);
                //}
            }
        });
    }

    public static VideoPlayerController getmVideoPlayerController() {
        return mVideoPlayerController;
    }

    public static void setmVideoPlayerController(VideoPlayerController mVideoPlayerController) {
        MainActivity.mVideoPlayerController = mVideoPlayerController;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if(video_tab) {
            if (configuration.orientation == configuration.ORIENTATION_LANDSCAPE) {
                tab_layout.setVisibility(View.GONE);
            } else if (configuration.orientation == configuration.ORIENTATION_PORTRAIT) {
                tab_layout.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void setCurrentContent(String content_url) {
        MainActivity.current_content_url = content_url;
    }

    public static String getCurrentContent() {
        return current_content_url;
    }

    public static VideoPlayer getmVideoPlayer() {
        return mVideoPlayer;
    }

    public static void setmVideoPlayer(VideoPlayer mVideoPlayer) {
        MainActivity.mVideoPlayer = mVideoPlayer;
    }

    public static ViewGroup getmAdUIContainer() {
        return mAdUIContainer;
    }

    public static void setmAdUIContainer(ViewGroup mAdUIContainer) {
        MainActivity.mAdUIContainer = mAdUIContainer;
    }

    public static LinearLayout getVideoLayoutContainer() {
        return videoLayoutContainer;
    }

    public static void setVideoLayoutContainer(LinearLayout videoLayoutContainer) {
        MainActivity.videoLayoutContainer = videoLayoutContainer;
    }

    public static ImageButton getmPlayButton() {
        return mPlayButton;
    }

    public static void setmPlayButton(ImageButton mPlayButton) {
        MainActivity.mPlayButton = mPlayButton;
    }

    public static Context getApp_context() {
        return app_context;
    }

    public static void setApp_context(Context app_context) {
        MainActivity.app_context = app_context;
    }

    public static void setResetPressed(boolean flag) {
        MainActivity.resetPressed = flag;
    }

    public static boolean isResetPressed() {
        return resetPressed;
    }

    public static void setFirstLaunched(boolean flag) {
        MainActivity.first_launched = flag;
    }

    public static boolean isFirst_launched() {
        return first_launched;
    }

}
