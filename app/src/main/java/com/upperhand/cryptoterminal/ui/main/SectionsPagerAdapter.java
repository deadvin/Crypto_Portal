package com.upperhand.cryptoterminal.ui.main;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.FragmentPrices;
import com.upperhand.cryptoterminal.FragmentWords;
import com.upperhand.cryptoterminal.FragmentMedia;
import com.upperhand.cryptoterminal.Settings;
import com.upperhand.cryptoterminal.FragmentTwitter;

import org.jetbrains.annotations.NotNull;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private FragmentTwitter FragmentTwitter;
    private FragmentPrices FragmentPrices;
    private FragmentPrices fragment_words;
    private FragmentPrices fragment_media;
    private FragmentPrices settings;

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1,  R.string.tab_text_2,R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;


    public SectionsPagerAdapter(Context context, FragmentManager fm) {

        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }


    @NotNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                return new FragmentTwitter();
            case 1:
                return new FragmentPrices();
            case 2:
                return new FragmentWords();
            case 3:
                return new FragmentMedia();
            case 4:
                return new Settings();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                FragmentTwitter = (FragmentTwitter) createdFragment;
                break;
            case 1:
                FragmentPrices = (FragmentPrices) createdFragment;
                break;
        }
        return createdFragment;
    }


    public void refresh(boolean big){

        FragmentPrices.refresh(big);
    }


    @Override
    public int getCount() {
        return 5;
    }


}