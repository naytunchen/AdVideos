package com.bnmla.advideos.VideoPlayer;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.bnmla.advideos.Entities.Global;
import com.bnmla.advideos.R;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;

/**
 * Created by nay on 2/29/16.
 */
public class VideoPlayerController implements AdErrorEvent.AdErrorListener,
        AdsLoader.AdsLoadedListener, AdEvent.AdEventListener, OnContentCompleteListener{

    final String TAG = VideoPlayerController.class.getSimpleName();

    private AdDisplayContainer mAdDisplayContainer;

    private AdsLoader mAdsLoader;

    private AdsManager mAdsManager;

    private ImaSdkFactory mSdkFactory;

    private VideoPlayerWithAdPlayback mVideoPlayerWithAdPlayback;

    private String mDefaultAdTagUrl;

    public VideoPlayerController(Context context, VideoPlayer videoPlayer, ViewGroup adUiContainer) {
        mVideoPlayerWithAdPlayback = new VideoPlayerWithAdPlayback(videoPlayer, adUiContainer);
        mVideoPlayerWithAdPlayback.init();
        mVideoPlayerWithAdPlayback.setmOnContentCompleteListener(this);
        mDefaultAdTagUrl = context.getString(R.string.ad_tag_url); // Default AD_TAG URL


        // Create an AdsLoader
        mSdkFactory = ImaSdkFactory.getInstance();
        mAdsLoader = mSdkFactory.createAdsLoader(context);
        mAdsLoader.addAdErrorListener(this);
        mAdsLoader.addAdsLoadedListener(this);
    }

    public VideoPlayerController(Context context, VideoPlayer videoPlayer, ViewGroup adUiContainer,
                                 String ad_url) {
        mVideoPlayerWithAdPlayback = new VideoPlayerWithAdPlayback(videoPlayer, adUiContainer);
        mVideoPlayerWithAdPlayback.init();
        mVideoPlayerWithAdPlayback.setmOnContentCompleteListener(this);
        mDefaultAdTagUrl = ad_url;


        // Create an AdsLoader
        mSdkFactory = ImaSdkFactory.getInstance();
        mAdsLoader = mSdkFactory.createAdsLoader(context);
        mAdsLoader.addAdErrorListener(this);
        mAdsLoader.addAdsLoadedListener(this);
    }
    private void requestAds() {
        if(Global.DEBUG_MODE)
            Log.d(TAG, "Requesting Ad Tag Url: " + mDefaultAdTagUrl);
        requestAds(mDefaultAdTagUrl);
    }

    private void requestAds(String url) {
        mAdDisplayContainer = mSdkFactory.createAdDisplayContainer();
        mAdDisplayContainer.setPlayer(mVideoPlayerWithAdPlayback.getVideoAdPlayer());
        mAdDisplayContainer.setAdContainer(mVideoPlayerWithAdPlayback.getAdUiContainer());

        // Create the ads request
        AdsRequest request = mSdkFactory.createAdsRequest();
        request.setAdTagUrl(url);
        request.setAdDisplayContainer(mAdDisplayContainer);
        request.setContentProgressProvider(mVideoPlayerWithAdPlayback.getContentProgressProvider());

        // Request the ad. After the ad is loaded, onAdsManagerLoaded() will be called.
        mAdsLoader.requestAds(request);
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        Log.e(TAG, "Ad Error: " + adErrorEvent.getError().getMessage());
        mVideoPlayerWithAdPlayback.resumeContentAfterAdPlayback();
    }

    @Override
    public void onAdEvent(AdEvent adEvent) {
        Log.i(TAG, "Event: " + adEvent.getType());

        // There are the suggested event types to handle. For full list of all ad event types,
        // see the documentation for AdEvent.AdEventType.
        switch (adEvent.getType()) {
            case LOADED:
                // AdEventType.LOADED will be fired when ads are ready to be played.
                // AdsManager.start() begins ad playback. This method is ignored for VMAP or ad
                // rules playlists, as the SDK will automatically start executing the playlist.
                mAdsManager.start();
                break;
            case CONTENT_PAUSE_REQUESTED:
                // AdEventType.CONTENT_PAUSE_REQUESTED is fired immediately before a video ad is
                // played.
                mVideoPlayerWithAdPlayback.pauseContentForAdPlayback();
                break;
            case CONTENT_RESUME_REQUESTED:
                // AdEventType.CONTENT_RESUME_REQUESTED is fired when the ad is completed and you
                // should start playing your content.
                mVideoPlayerWithAdPlayback.resumeContentAfterAdPlayback();
                break;
            case ALL_ADS_COMPLETED:
                if (mAdsManager != null) {
                    mAdsManager.destroy();
                    mAdsManager = null;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
        // Ads were successfully loaded, so get the AdsManager instance. AdsManager has
        // events for ad playback and errors.
        mAdsManager = adsManagerLoadedEvent.getAdsManager();

        // Attach event and error event listeners.
        mAdsManager.addAdErrorListener(this);
        mAdsManager.addAdEventListener(this);
        mAdsManager.init();
    }

    @Override
    public void onContentComplete() {
        mAdsLoader.contentComplete();
    }

    public void setContentVideo(String videoPath) {
        mVideoPlayerWithAdPlayback.setContentVideoPath(videoPath);
    }

    public void play() {
        requestAds();
    }

    public void resume() {
        mVideoPlayerWithAdPlayback.restorePosition();
        if (mAdsManager != null && mVideoPlayerWithAdPlayback.getIsAdDisplayed()) {
            mAdsManager.resume();
        }
    }

    public void pause() {
        mVideoPlayerWithAdPlayback.savePosition();
        if (mAdsManager != null && mVideoPlayerWithAdPlayback.getIsAdDisplayed()) {
            mAdsManager.pause();
        }
    }

    public void stop() {
        mVideoPlayerWithAdPlayback.getVideoAdPlayer().stopAd();
    }
}
