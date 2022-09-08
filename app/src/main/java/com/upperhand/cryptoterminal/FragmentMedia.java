package com.upperhand.cryptoterminal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class FragmentMedia extends Fragment {

    FragmentVideo FragmentVideo;
    FragmentNews FragmentNews;
    FragmentEvents FragmentEvents;
    Button btnYoutube;
    Button btnEvents;
    Button btnNews;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_media, container, false);

        FragmentVideo = new FragmentVideo();
        FragmentEvents = new FragmentEvents();
        FragmentNews = new FragmentNews();

        btnYoutube = view.findViewById(R.id.button1);
        btnEvents = view.findViewById(R.id.button2);
        btnNews = view.findViewById(R.id.button3);

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_container, FragmentEvents);
        transaction.add(R.id.fragment_container, FragmentVideo);
        transaction.add(R.id.fragment_container, FragmentNews);

        transaction.show(FragmentNews);
        transaction.hide(FragmentEvents);
        transaction.hide(FragmentVideo);
        transaction.commit();


        btnYoutube.setOnClickListener( new View.OnClickListener() { 

            @Override
            public void onClick(View v) {

                btnYoutube.setTextColor(Color.parseColor("#0A75FF"));
                btnEvents.setTextColor(Color.parseColor("#FFFFFF"));
                btnNews.setTextColor(Color.parseColor("#FFFFFF"));

                transaction = fragmentManager.beginTransaction();
                transaction.show(FragmentVideo);
                transaction.hide(FragmentEvents);
                transaction.hide(FragmentNews);
                transaction.commit();
                FragmentVideo.onResume();
            }
        });

        btnEvents.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btnYoutube.setTextColor(Color.parseColor("#FFFFFF"));
                btnEvents.setTextColor(Color.parseColor("#0A75FF"));
                btnNews.setTextColor(Color.parseColor("#FFFFFF"));

                transaction = fragmentManager.beginTransaction();
                transaction.hide(FragmentVideo);
                transaction.show(FragmentEvents);
                transaction.hide(FragmentNews);
                transaction.commit();
            }
        });

        btnNews.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btnYoutube.setTextColor(Color.parseColor("#FFFFFF"));
                btnEvents.setTextColor(Color.parseColor("#FFFFFF"));
                btnNews.setTextColor(Color.parseColor("#0A75FF"));

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(FragmentVideo);
                transaction.hide(FragmentEvents);
                transaction.show(FragmentNews);
                transaction.commit();
            }
        });

        btnNews.performClick();

        return view;
    }
}