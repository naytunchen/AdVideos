package com.bnmla.advideos.VideoPlayer;

import android.util.Log;
import android.view.ViewGroup;

import com.bnmla.advideos.Callbacks.PlayerCallback;
import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nay on 2/29/16.
 */
public class VideoPlayerWithAdPlayback {

    final String TAG = VideoPlayerWithAdPlayback.class.getSimpleName();

    private VideoPlayer mplayer;
    private ViewGroup mContainer;
    private boolean mIsAdDisplayed;
    private String mContentVideoUrl;
    private int mSavedVideoPosition;
    private OnContentCompleteListener mOnContentCompleteListener;
    private VideoAdPlayer adPlayer;
    private ContentProgressProvider mProgressProvider;
    private final List<VideoAdPlayer.VideoAdPlayerCallback> mAdCallbacks =
            new ArrayList<VideoAdPlayer.VideoAdPlayerCallback>(1);

    private boolean mIsContentComplete;
    public VideoPlayerWithAdPlayback(VideoPlayer videoPlayer, ViewGroup mContainer) {
        this.mplayer = videoPlayer;
        this.mContainer = mContainer;
    }

    public void init() {
        mIsAdDisplayed = true;
        mSavedVideoPosition = 0;
        mIsContentComplete = false;

        adPlayer = new VideoAdPlayer() {
            @Override
            public void playAd() {
                mIsAdDisplayed = true;
                mplayer.play();
            }

            @Override
            public void loadAd(String s) {
                mIsAdDisplayed = true;
                mplayer.setVideoPath(s);
            }

            @Override
            public void stopAd() {
                mplayer.stopPlayback();
            }

            @Override
            public void pauseAd() {
                mplayer.pause();
            }

            @Override
            public void resumeAd() {
                playAd();
            }

            @Override
            public void addCallback(VideoAdPlayerCallback videoAdPlayerCallback) {
                mAdCallbacks.add(videoAdPlayerCallback);
            }

            @Override
            public void removeCallback(VideoAdPlayerCallback videoAdPlayerCallback) {
                mAdCallbacks.remove(videoAdPlayerCallback);
            }

            @Override
            public VideoProgressUpdate getAdProgress() {
                if(!mIsAdDisplayed || mplayer.getDuration() <= 0) {
                    return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
                }
                return new VideoProgressUpdate(mplayer.getCurrentPosition(), mplayer.getDuration());
            }
        };

        mProgressProvider = new ContentProgressProvider() {
            @Override
            public VideoProgressUpdate getContentProgress() {
                if (mIsAdDisplayed || mplayer.getDuration() <= 0) {
                    return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
                }

                return new VideoProgressUpdate(mplayer.getCurrentPosition(),mplayer.getDuration());
            }
        };

        mplayer.addPlayerCallback(new PlayerCallback() {
            @Override
            public void onPlay() {
                if(mIsAdDisplayed) {
                    for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
                        callback.onPlay();
                    }
                }
            }

            @Override
            public void onPause() {
                if (mIsAdDisplayed) {
                    for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
                        callback.onPause();
                    }
                }
            }

            @Override
            public void onResume() {
                if (mIsAdDisplayed) {
                    for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
                        callback.onResume();
                    }
                }
            }

            @Override
            public void onCompleted() {
                if (mIsAdDisplayed) {
                    for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
                        callback.onEnded();
                    }
                } else {
                    if(mOnContentCompleteListener != null) {
                        mOnContentCompleteListener.onContentComplete();
                    }
                    mIsContentComplete = true;
                }
            }

            @Override
            public void onError() {
                if (mIsAdDisplayed) {
                    for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
                        callback.onError();
                    }
                }
            }
        });
    }

    public void setmOnContentCompleteListener(OnContentCompleteListener listener) {
        mOnContentCompleteListener = listener;
    }

    public void setContentVideoPath(String url) {
        this.mContentVideoUrl = url;
    }

    public void savePosition() {
        mSavedVideoPosition = mplayer.getCurrentPosition();
    }

    public void restorePosition() {
        mplayer.seekTo(mSavedVideoPosition);
    }

    public ViewGroup getAdUiContainer() {
        return mContainer;
    }

    public VideoAdPlayer getVideoAdPlayer() {
        return this.adPlayer;
    }

    public boolean getIsAdDisplayed() {
        return this.mIsAdDisplayed;
    }

    public ContentProgressProvider getContentProgressProvider() {
        return mProgressProvider;
    }

    public void pauseContentForAdPlayback() {
        savePosition();
        mplayer.stopPlayback();
    }

    public void resumeContentAfterAdPlayback() {
        if (mContentVideoUrl == null || mContentVideoUrl.isEmpty()) {
            Log.w(TAG, "No content URL specified.");
            return;
        }

        mIsAdDisplayed = false;
        mplayer.setVideoPath(mContentVideoUrl);
        restorePosition();
        if (!mIsContentComplete) {
            mplayer.play();
        } else {
            mplayer.stopPlayback();
        }
    }

    public VideoPlayer getMplayer() {
        return mplayer;
    }

    public String getmContentVideoUrl() {
        return mContentVideoUrl;
    }
}
