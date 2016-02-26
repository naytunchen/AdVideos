package com.bnmla.advideos.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnmla.advideos.R;

/**
 * Created by nay on 2/26/16.
 */
public class VideoFragment extends Fragment{
    private final String TAG = VideoFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        Log.e(TAG, "HIIIIIIIIIIII");
        return view;
    }
}
