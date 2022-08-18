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


public class fragment_words extends Fragment {

    keywords_altcoins keywords_altcoins;
    keywords_bitcoin keywords_bitcoin;
    Button btn1;
    Button btn2;
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
        View view =  inflater.inflate(R.layout.fragment_keywords, container, false);

        keywords_altcoins = new keywords_altcoins();
        keywords_bitcoin = new keywords_bitcoin();

        btn1 = view.findViewById(R.id.button1);
        btn2 = view.findViewById(R.id.button2);

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_altcoins, keywords_altcoins);
        transaction.add(R.id.fragment_altcoins, keywords_bitcoin);

        btn1.setTextColor(Color.parseColor("#FFFFFF"));
        btn2.setTextColor(Color.parseColor("#0A75FF"));
        transaction.show(keywords_altcoins);
        transaction.hide(keywords_bitcoin);
        transaction.commit();


        btn1.setOnClickListener( new View.OnClickListener() {  //=============   BITCOIN

            @Override
            public void onClick(View v) {

                btn1.setTextColor(Color.parseColor("#0A75FF"));
                btn2.setTextColor(Color.parseColor("#FFFFFF"));
                transaction = fragmentManager.beginTransaction();
                transaction.show(keywords_bitcoin);
                transaction.hide(keywords_altcoins);
                transaction.commit();
            }
        });

        btn2.setOnClickListener( new View.OnClickListener() {  //=============  ALTCOINS

            @Override
            public void onClick(View v) {

                btn1.setTextColor(Color.parseColor("#FFFFFF"));
                btn2.setTextColor(Color.parseColor("#0A75FF"));
                transaction = fragmentManager.beginTransaction();
                transaction.show(keywords_altcoins);
                transaction.hide(keywords_bitcoin);
                transaction.commit();
            }
        });

        return view;
    }



    @Override
    public void onResume() {

//        preferences = this.getActivity().getSharedPreferences("topic", Context.MODE_PRIVATE);
//        String topic = preferences.getString("topic", "none");
//
//        if(topic.equals("vid")){
//            btn1.performClick();
//            editor = context.getSharedPreferences("topic", MODE_PRIVATE).edit();
//            editor.putString("topic", "none");
//            editor.apply();
//        }else if(topic.equals("events")){
//            btn2.performClick();
//            editor = context.getSharedPreferences("topic", MODE_PRIVATE).edit();
//            editor.putString("topic", "none");
//            editor.apply();
//        }
//
//        if(selected == 3){
//            news.run();
//        }

        super.onResume();
    }

    @Override
    public void onPause() {
//        news.stop();
        super.onPause();
    }

//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//        } else {
//            news.stop();
//        }
//    }

}