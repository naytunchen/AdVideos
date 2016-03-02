package com.bnmla.advideos.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnmla.advideos.MainActivity;
import com.bnmla.advideos.R;
import com.bnmla.advideos.VideoPlayer.VideoPlayerController;

/**
 * Created by nay on 2/26/16.
 */
public class VideoFragment extends Fragment{
    private final String TAG = VideoFragment.class.getSimpleName();

    protected VideoPlayerController mVideoPlayerController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        return view;
    }

    @Override
    public void onResume() {
        if (MainActivity.getmVideoPlayerController() != null) {
            MainActivity.getmVideoPlayerController().resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (MainActivity.getmVideoPlayerController() != null) {
            MainActivity.getmVideoPlayerController().pause();
        }
        super.onPause();
    }
}
