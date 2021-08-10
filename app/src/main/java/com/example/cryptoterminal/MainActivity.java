package com.example.cryptoterminal;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;



import com.example.cryptoterminal.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        tabs.setSelectedTabIndicatorColor(Color.parseColor("#3a70b6"));
        tabs.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));
        tabs.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#3a70b6"));

//        tabs.addTab(tabs.newTab().setIcon(R.drawable.alert));
//        tabs.addTab(tabs.newTab().setIcon(R.drawable.settings));


        tabs.getTabAt(3).setIcon(R.drawable.alert);
        tabs.getTabAt(4).setIcon(R.drawable.setting_sel);

        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabs.getChildAt(0)).getChildAt(3));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.5f;
        layout.setLayoutParams(layoutParams);

        layout = ((LinearLayout) ((LinearLayout) tabs.getChildAt(0)).getChildAt(4));
        layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.5f;
        layout.setLayoutParams(layoutParams);




    }
}