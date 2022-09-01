package com.upperhand.cryptoterminal;



import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;


public class FragmentMedia extends Fragment {

    FragmentVideo FragmentVideo;
    FragmentNews FragmentNews;
    FragmentEvents FragmentEvents;
    Button btn1;
    Button btn2;
    Button btn3;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Context context;
    int selected;


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

        btn1 = view.findViewById(R.id.button1);
        btn2 = view.findViewById(R.id.button2);
        btn3 = view.findViewById(R.id.button3);

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_container, FragmentEvents);
        transaction.add(R.id.fragment_container, FragmentVideo);
        transaction.add(R.id.fragment_container, FragmentNews);

        transaction.show(FragmentNews);
        transaction.hide(FragmentEvents);
        transaction.hide(FragmentVideo);
        transaction.commit();


        btn1.setOnClickListener( new View.OnClickListener() {  //===============YOUTUBE

            @Override
            public void onClick(View v) {

                btn1.setTextColor(Color.parseColor("#0A75FF"));
                btn2.setTextColor(Color.parseColor("#FFFFFF"));
                btn3.setTextColor(Color.parseColor("#FFFFFF"));

                transaction = fragmentManager.beginTransaction();
                transaction.show(FragmentVideo);
                transaction.hide(FragmentEvents);
                transaction.hide(FragmentNews);
                transaction.commit();
                selected = 1;
                FragmentVideo.onResume();
            }
        });

        btn2.setOnClickListener( new View.OnClickListener() {  //===============EVENTS

            @Override
            public void onClick(View v) {

                btn1.setTextColor(Color.parseColor("#FFFFFF"));
                btn2.setTextColor(Color.parseColor("#0A75FF"));
                btn3.setTextColor(Color.parseColor("#FFFFFF"));

                transaction = fragmentManager.beginTransaction();
                transaction.hide(FragmentVideo);
                transaction.show(FragmentEvents);
                transaction.hide(FragmentNews);
                transaction.commit();
                selected = 2;
            }
        });

        btn3.setOnClickListener( new View.OnClickListener() {  //===============NEWS

            @Override
            public void onClick(View v) {

                btn1.setTextColor(Color.parseColor("#FFFFFF"));
                btn2.setTextColor(Color.parseColor("#FFFFFF"));
                btn3.setTextColor(Color.parseColor("#0A75FF"));

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(FragmentVideo);
                transaction.hide(FragmentEvents);
                transaction.show(FragmentNews);
                transaction.commit();
                selected = 3;

            }
        });

        btn3.performClick();

        return view;
    }



    @Override
    public void onResume() {

        preferences = this.getActivity().getSharedPreferences("topic", Context.MODE_PRIVATE);
        String topic = preferences.getString("topic", "none");

        if(topic.equals("vid")){
            btn1.performClick();
            editor = context.getSharedPreferences("topic", MODE_PRIVATE).edit();
            editor.putString("topic", "none");
            editor.apply();
        }else if(topic.equals("events")){
            btn2.performClick();
            editor = context.getSharedPreferences("topic", MODE_PRIVATE).edit();
            editor.putString("topic", "none");
            editor.apply();
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



}