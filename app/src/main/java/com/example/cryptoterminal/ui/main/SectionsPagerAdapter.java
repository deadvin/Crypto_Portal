package com.example.cryptoterminal.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cryptoterminal.R;
import com.example.cryptoterminal.alerts;
import com.example.cryptoterminal.div;
import com.example.cryptoterminal.media;
import com.example.cryptoterminal.settings;
import com.example.cryptoterminal.twitter;
import com.example.cryptoterminal.youtube;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }



    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                 fragment = new twitter();
                break;
            case 1:
                fragment = new div();
                break;
            case 2:
                fragment = new media();
                break;
            case 3:
                fragment = new alerts();
                break;
            case 4:
                fragment = new youtube();
                break;
            default:
                return null;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {

        return 5;
    }
}