package com.example.cryptoterminal;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



public class media extends Fragment {

    youtube youtube;
    news news;
    event_fragment events;
    Button btn1;
    Button btn2;
    Button btn3;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.media, container, false);

        youtube = new youtube();
        events = new event_fragment();
        news = new news();

        btn1 = view.findViewById(R.id.button1);
        btn2 = view.findViewById(R.id.button2);
        btn3 = view.findViewById(R.id.button3);

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_one, events);
        transaction.add(R.id.fragment_two, youtube);
        transaction.add(R.id.fragment_three, news);


        transaction.show(youtube);
        transaction.hide(news);
        transaction.hide(events);

        transaction.commit();

        btn1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.show(youtube);
                transaction.hide(events);
                transaction.hide(news);
                transaction.commit();

            }
        });

        btn2.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.hide(youtube);
                transaction.show(events);
                transaction.hide(news);
                transaction.commit();

            }
        });

        btn3.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.hide(youtube);
                transaction.hide(events);
                transaction.show(news);
                transaction.commit();

            }
        });

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
    }


}