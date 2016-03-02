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
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bnmla.advideos.Adapters.PagerAdapter;
import com.bnmla.advideos.Entities.Global;
import com.bnmla.advideos.Entities.Setting;
import com.bnmla.advideos.Fragments.VideoFragment;
import com.bnmla.advideos.Utilities.VPAIDResponse;
import com.bnmla.advideos.VideoPlayer.VideoPlayer;
import com.bnmla.advideos.VideoPlayer.VideoPlayerController;

import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity{

    public static Queue<VPAIDResponse> pool = new LinkedList<>();
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
    private static WebView web_view = null;

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
        app_context = getApplicationContext();

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

        /*VPAIDResponse vpaid_response = pool.poll();

        if (vpaid_response != null) {
            // if queue isn't empty, then webview stuffs!
            MainActivity.setWebView(loadInterstitial(vpaid_response, view));
            MainActivity.getWebView().setVisibility(View.VISIBLE);
        }*/

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

//    public static void setWebView(WebView view) {
//        MainActivity.web_view = view;
//    }
//
//    public static WebView getWebView() {
//        return web_view;
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private WebView loadInterstitial(final VPAIDResponse vpaid_response, View view) {
//        WebView wv = ((WebView) view.findViewById(R.id.interstitial));
//
//        // Setup WebView with a javascript interface
//        WebView.setWebContentsDebuggingEnabled(true);
//
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.getSettings().setMediaPlaybackRequiresUserGesture(false);
//
//        // load VPAID JS interface.
//        wv.addJavascriptInterface(new VpaidAdInterface(MainActivity.getApp_context(),
//                vpaid_response), "NativeInterface");;
//
//        // load the AdBroker with the given media source parsed from the VAST response
//        wv.loadDataWithBaseURL(
//                "http://search.spotxchange.com",
//                String.format(getString(R.string.htmlAdBrokerScript), vpaid_response.medialUrl),
//                "text/html",
//                "utf8",
//                null
//        );
//
//        Toast.makeText(MainActivity.getApp_context(), "Constructing VPAID ad...",
//                Toast.LENGTH_SHORT).show();
//        return wv;
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private void showInterstitial() {
//        // Show the ad if it's ready. Otherwise toast and reload the ad.
//        if(web_view != null) {
//            web_view.evaluateJavascript(getString(R.string.jsStartAd), null);
//            web_view.setVisibility(View.VISIBLE);
//        } else {
//            Toast.makeText(MainActivity.getApp_context(), "Ad did not load.",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * Evaluates the Javascript inside the VPAID WebView
//     */
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private void evaluateJavascript(final String javascript) {
//       web_view.evaluateJavascript(javascript, null);
//    }
//
//    private class VpaidAdInterface {
//        Context context;
//        VPAIDResponse response;
//
//        VpaidAdInterface(Context context, VPAIDResponse vpaid_response) {
//            this.context = context;
//            this.response = vpaid_response;
//        }
//
//        @TargetApi(Build.VERSION_CODES.KITKAT)
//        @JavascriptInterface
//        public void onPageLoaded() {
//            // load the environment variables.
//            evaluateJavascript(getString(R.string.jsEnvironment));
//
//            // load the JS ad parameters parsed from the VAST response.
//            evaluateJavascript(String.format(getString(R.string.jsAdParameters),
//                    response.adParameters));
//
//            // load the getVPAIDAd JS.
//            evaluateJavascript(getString(R.string.jsGetVPAIDAd));
//        }
//
//        @TargetApi(Build.VERSION_CODES.KITKAT)
//        @JavascriptInterface
//        public void onGetVpaidAd() {
//            evaluateJavascript(getString(R.string.jsInitAd));
//        }
//
//        @TargetApi(Build.VERSION_CODES.KITKAT)
//        @JavascriptInterface
//        public void onAdLoaded() {
//            showInterstitial();
//            Toast.makeText(MainActivity.getApp_context(), "Ad loaded.", Toast.LENGTH_SHORT).show();
//        }
//
//        @TargetApi(Build.VERSION_CODES.KITKAT)
//        @JavascriptInterface
//        public void onAdStarted() {
//            Toast.makeText(MainActivity.getApp_context(), "Ad started.", Toast.LENGTH_SHORT).show();
//        }
//    }
}
