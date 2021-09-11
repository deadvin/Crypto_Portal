package com.example.cryptoterminal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.Context.MODE_PRIVATE;

public class alerts extends Fragment {

    Switch day_switch;
    Switch night_switch;
    boolean day;
    boolean night;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = this.getActivity().getSharedPreferences("day", Context.MODE_PRIVATE);
        day = preferences.getBoolean("day", false);

        preferences = this.getActivity().getSharedPreferences("night", Context.MODE_PRIVATE);
        night = preferences.getBoolean("night", false);
        context = this.getActivity();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_alerts, container, false);

        day_switch = view.findViewById(R.id.switch1);
        day_switch.setChecked(day);
        night_switch = view.findViewById(R.id.switch2);
        night_switch.setChecked(night);


        day_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor = context.getSharedPreferences("day", MODE_PRIVATE).edit();
                    editor.putBoolean("day", true);
                    editor.apply();
                    manage(true,"day");
                }else{
                    editor = context.getSharedPreferences("day", MODE_PRIVATE).edit();
                    editor.putBoolean("day", false);
                    editor.apply();
                    manage(false,"day");
                }
            }
        });

        night_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor = context.getSharedPreferences("night", MODE_PRIVATE).edit();
                    editor.putBoolean("night", true);
                    editor.apply();
                    manage(true,"night");
                }else{
                    editor = context.getSharedPreferences("night", MODE_PRIVATE).edit();
                    editor.putBoolean("night", false);
                    editor.apply();
                    manage(false,"night");
                }
            }
        });



        return view;
    }


    public void manage(boolean sub, String tag) {

        if(sub) {

            Toast.makeText(getActivity(), "on",
                    Toast.LENGTH_LONG).show();

            FirebaseMessaging.getInstance().subscribeToTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }});
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }});
        }
    }

}