package com.bnmla.advideos.VideoPlayer;

import com.bnmla.advideos.Callbacks.PlayerCallback;

/**
 * Created by nay on 2/29/16.
 */
public interface VideoPlayer {
    void play();
    void pause();
    int getCurrentPosition();
    void seekTo(int videoPosition);
    int getDuration();
    void stopPlayback();
    void disablePlaybackControls();
    void enablePlaybackControls();
    void setVideoPath(String videoUrl);
    void addPlayerCallback(PlayerCallback callback);
    void removePlayerCallback(PlayerCallback callback);
}
