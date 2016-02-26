package com.bnmla.advideos.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bnmla.advideos.Fragments.AdConfigFragment;
import com.bnmla.advideos.Fragments.PageFragment;
import com.bnmla.advideos.Fragments.PlayerConfigFragment;

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
                PageFragment pageFragment = new PageFragment();
                return pageFragment;
            case 1:
                PlayerConfigFragment playerConfigFragment = new PlayerConfigFragment();
                return playerConfigFragment;
            case 2:
                AdConfigFragment adConfigFragment = new AdConfigFragment();
                return adConfigFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return num_tabs;
    }
}
