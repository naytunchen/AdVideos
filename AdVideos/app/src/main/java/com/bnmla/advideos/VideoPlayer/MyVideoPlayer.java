package com.bnmla.advideos.VideoPlayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bnmla.advideos.Callbacks.PlayerCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nay on 2/29/16.
 */
public class MyVideoPlayer extends VideoView implements VideoPlayer {

    private enum PlaybackState {
        STOPPED, PAUSED, PLAYING
    }

    private MediaController controller;
    private PlaybackState state;
    private final List<PlayerCallback> callbacks = new ArrayList<PlayerCallback>(1);

    public MyVideoPlayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyVideoPlayer(Context context){
        super(context);
        init();
    }

    private void init() {
        this.state = PlaybackState.STOPPED;
        this.controller = new MediaController(getContext());
        this.controller.setAnchorView(this);
        enablePlaybackControls();

        super.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Reset the MediaPlayer
                // This prevents a race condition which occasionally results in the media
                // player crashing when switching between videos.

                disablePlaybackControls();
                mp.reset();
                mp.setDisplay(getHolder());
                enablePlaybackControls();
                state = PlaybackState.STOPPED;

                for (PlayerCallback callback : callbacks) {
                    callback.onCompleted();
                }
            }
        });

        super.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                state = PlaybackState.STOPPED;

                for (PlayerCallback callback : callbacks) {
                    callback.onError();
                }
                // Return true signals to MediaPlayer to prevent calling completion handler
                return true;
            }
        });
    }

    @Override
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void play() {
        start();
    }

    @Override
    public void start() {
        super.start();
        PlaybackState oldPlaybackState = state;
        state = PlaybackState.PLAYING;
        switch(oldPlaybackState) {
            case STOPPED:
                for(PlayerCallback callback : callbacks){
                    callback.onPlay();
                }
                break;
            case PAUSED:
                for(PlayerCallback callback : callbacks) {
                    callback.onResume();
                }
                break;
            default :
        }
    }

    @Override
    public void stopPlayback() {
        super.stopPlayback();
        PlaybackState oldPlaybackState = state;
        state = PlaybackState.STOPPED;
        switch (oldPlaybackState) {
            case PLAYING:
                for (PlayerCallback callback : callbacks) {
                    callback.onCompleted();
                }
                break;
            case PAUSED:
                for (PlayerCallback callback : callbacks) {
                    callback.onCompleted();
                }
                break;
            case STOPPED:
                for (PlayerCallback callback : callbacks) {
                    callback.onCompleted();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void disablePlaybackControls() {
        setMediaController(null);
    }

    @Override
    public void enablePlaybackControls() {
        setMediaController(controller);
    }

    @Override
    public void addPlayerCallback(PlayerCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removePlayerCallback(PlayerCallback callback) {
        callbacks.remove(callback);
    }
}
