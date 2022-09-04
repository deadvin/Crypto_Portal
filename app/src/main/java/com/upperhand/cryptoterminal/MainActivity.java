package com.upperhand.cryptoterminal;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.viewpager.widget.ViewPager;

import com.upperhand.cryptoterminal.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {


    TabLayout tabs;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Timer timer;
    Handler handler;
    final private int mInterval = 10000;
    Context context;
    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;
    int selected = 0;
    int counter = 0;
    boolean visible;
    String[] refresh_sp_list = new String[] {"refresh_keywords_altcoins", "refresh_keywords_btc", "refresh_news_top", "refresh_news",
            "refresh_vid",  "refresh_breaking_f",  "refresh_breaking",  "refresh_sym_f",  "refresh_sym",
            "refresh_10",  "refresh_500",  "refresh_1",  "refresh_100", "refresh_events"};


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_CryptoTerminal_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        context = this;

        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.setCurrentItem(viewPager.getCurrentItem());
                return true;
            }
        });

        viewPager.beginFakeDrag();


        //============= SET APP ID

//        SharedPreferences.Editor editor = null;
        preferences = MainActivity.this.getSharedPreferences("id", Context.MODE_PRIVATE);
        int id = preferences.getInt("id", 0);

        if(id == 0){
            editor = MainActivity.this.getSharedPreferences("id", MODE_PRIVATE).edit();
            editor.putInt("id",  new Random().nextInt(1000000000) );
            editor.apply();
        }


        //=============   REMOTE CONFIG

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        HashMap<String,Object> defaults = new HashMap<>();
        defaults.put("url",getString(R.string.url));

        mFirebaseRemoteConfig.setDefaultsAsync(defaults);

//        mFirebaseRemoteConfig.fetchAndActivate()
//                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Boolean> task) {
//                        if (task.isSuccessful()) {
//                            boolean updated = task.getResult();
//                            Log.d(TAG, "Config params updated: " + updated);
//
//                        } else {
//                        }
//                    }
//                });

        //========     SERVICES

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String updatedToken = instanceIdResult.getToken();
                Log.e("Updated Token",updatedToken);
            }
        });


        //========     SET TABS TEXT COLOR

        tabs.setSelectedTabIndicatorColor( Color.parseColor("#0A75FF"));
        tabs.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));
        tabs.setTabTextColors(Color.parseColor("#FFFAFA"), Color.parseColor("#0A75FF"));


        //========     SET TABS ICONS AND SIZE

        tabs.getTabAt(4).setIcon(R.drawable.settings);

        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabs.getChildAt(0)).getChildAt(4));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.6f;
        layout.setLayoutParams(layoutParams);


        //========     SET TABS ICONS  COLOR

        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.white);
        tabs.getTabAt(4).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        tabs.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        selected = tab.getPosition();
                        if (tab.getIcon() != null){
                            int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.selected);
                            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        if (tab.getIcon() != null){
                            int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.white);
                            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                        }
                    }
                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                });

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.v_dark));


//        ====================== SHARED PREFERENCES FALSE

        for (int i = 0; i < refresh_sp_list.length - 1; i++) {
            editor = context.getSharedPreferences(refresh_sp_list[i], MODE_PRIVATE).edit();
            editor.putBoolean(refresh_sp_list[i], false);
            editor.apply();
        }


        //endregion

//     ==================================    HANDLER

        handler = new Handler();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mStatusChecker.run();
            }
        }, 5000);

    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            counter++;

            if(selected == 1 && visible){
                sectionsPagerAdapter.refresh(false);
            }

            if(counter > 4){
                counter = 0;
                if(isNetworkAvailable()) {

                    for (int i = 0; i < refresh_sp_list.length - 1; i++) {
                        editor = context.getSharedPreferences(refresh_sp_list[i], MODE_PRIVATE).edit();
                        editor.putBoolean(refresh_sp_list[i], true);
                        editor.apply();
                    }
                }

                if(selected == 1 && visible){
                    sectionsPagerAdapter.refresh(true);
                }
            }

            handler.postDelayed(mStatusChecker, mInterval);
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDestroy() {

        if(timer != null) {
            timer.cancel();
            timer = null;
        }

        if(handler != null) {
            handler.removeCallbacks(mStatusChecker);
            handler = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {

        visible = true;

        preferences = MainActivity.this.getSharedPreferences("topic", Context.MODE_PRIVATE);
        String topic = preferences.getString("topic", "none");

        if (topic.equals("alts") || topic.equals("breaking") ) {
            tabs.getTabAt(0).select();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        visible = false;
        super.onPause();
    }
}