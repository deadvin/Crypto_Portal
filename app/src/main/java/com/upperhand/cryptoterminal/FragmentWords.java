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


public class FragmentWords extends Fragment {

    FragmentAltcoinWords FragmentAltcoinWords;
    FragmentBitcoinWords FragmentBitcoinWords;
    Button btnBitcoin;
    Button btnAltcoins;
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
        View view =  inflater.inflate(R.layout.fragment_keywords, container, false);

        FragmentAltcoinWords = new FragmentAltcoinWords();
        FragmentBitcoinWords = new FragmentBitcoinWords();

        btnBitcoin = view.findViewById(R.id.button1);
        btnAltcoins = view.findViewById(R.id.button2);

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.fragment_altcoins, FragmentAltcoinWords);
        transaction.add(R.id.fragment_altcoins, FragmentBitcoinWords);

        btnBitcoin.setTextColor(Color.parseColor("#FFFFFF"));
        btnAltcoins.setTextColor(Color.parseColor("#0A75FF"));
        transaction.show(FragmentAltcoinWords);
        transaction.hide(FragmentBitcoinWords);
        transaction.commit();


        btnBitcoin.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnBitcoin.setTextColor(Color.parseColor("#0A75FF"));
                btnAltcoins.setTextColor(Color.parseColor("#FFFFFF"));
                transaction = fragmentManager.beginTransaction();
                transaction.show(FragmentBitcoinWords);
                transaction.hide(FragmentAltcoinWords);
                transaction.commit();
            }
        });

        btnAltcoins.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnBitcoin.setTextColor(Color.parseColor("#FFFFFF"));
                btnAltcoins.setTextColor(Color.parseColor("#0A75FF"));
                transaction = fragmentManager.beginTransaction();
                transaction.show(FragmentAltcoinWords);
                transaction.hide(FragmentBitcoinWords);
                transaction.commit();
            }
        });

        return view;
    }


}