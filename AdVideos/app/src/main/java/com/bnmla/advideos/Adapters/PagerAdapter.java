package com.bnmla.advideos.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bnmla.advideos.Fragments.ConfigFragment;
import com.bnmla.advideos.Fragments.VideoFragment;

/**
 * Created by nay on 2/26/16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int num_tabs;

    public PagerAdapter(FragmentManager fm, int num_tabs) {
        super(fm);
        this.num_tabs = num_tabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                ConfigFragment configFragment = new ConfigFragment();
                return configFragment;
            case 1:
                VideoFragment videoFragment = new VideoFragment();
                return videoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num_tabs;
    }
}
