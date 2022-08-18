package com.upperhand.cryptoterminal.ui.main;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.fragment_prices;
import com.upperhand.cryptoterminal.fragment_words;
import com.upperhand.cryptoterminal.fragment_media;
import com.upperhand.cryptoterminal.settings;
import com.upperhand.cryptoterminal.fragment_twitter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private fragment_twitter fragment_twitter;
    private fragment_prices fragment_prices;
    private fragment_prices fragment_words;
    private fragment_prices fragment_media;
    private fragment_prices settings;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1,  R.string.tab_text_2,R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;


    public SectionsPagerAdapter(Context context, FragmentManager fm) {

        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                return new fragment_twitter();
            case 1:
                return new fragment_prices();
            case 2:
                return new fragment_words();
            case 3:
                return new fragment_media();
            case 4:
                return new settings();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                fragment_twitter = (fragment_twitter) createdFragment;
                break;
            case 1:
                fragment_prices = (fragment_prices) createdFragment;
                break;
        }
        return createdFragment;
    }


    public void refresh(boolean big){

        fragment_prices.refresh(big);
    }



    @Override
    public int getCount() {
        return 5;
    }
}