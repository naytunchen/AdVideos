package com.bnmla.advideos.Callbacks;

/**
 * Created by nay on 2/29/16.
 */
public interface PlayerCallback {
    void onPlay();
    void onPause();
    void onResume();
    void onCompleted();
    void onError();
}

