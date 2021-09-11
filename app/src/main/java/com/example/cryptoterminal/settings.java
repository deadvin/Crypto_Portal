package com.example.cryptoterminal;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;

public class settings extends Fragment {

    twitter frg1;
    div frg2;
    twitter frg3;
    FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.setting, container, false);

        frg1 = new twitter();
        frg2 = new div();
        frg3 = new twitter();

//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        Fragment childFragment = new ChildFragment();
         transaction = getChildFragmentManager().beginTransaction();
//        transaction.replace(R.id.child_fragment_container, childFragment).commit();
//        transaction = fragmentManager.beginTransaction();



        transaction.add(R.id.fragment_one, frg1);
        transaction.add(R.id.fragment_two, frg2);
        transaction.add(R.id.fragment_three, frg3);
        transaction.commit();



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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(frg1 != null) {
                frg1.refresh(true);
                frg2.refresh(true);
                frg3.refresh(true);
                Log.e("see", "online");
            }

        }else {
            if(frg1 != null) {
                frg1.refresh(false);
                frg2.refresh(false);
                frg3.refresh(false);
                Log.e("see", "offline");
            }
        }
    }
}