package com.bnmla.advideos.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bnmla.advideos.R;

/**
 * Created by nay on 2/26/16.
 */
public class PageFragment extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_tab_fragment, container, false);
    }
}
