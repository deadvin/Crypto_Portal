package com.example.cryptoterminal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
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
    Switch price_switch;
    Switch event_switch;
    Switch sym_switch;
    Switch sym_f_switch;
    boolean day;
    boolean night;
    boolean price;
    boolean event;
    boolean sym;
    boolean sym_f;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

        preferences = this.getActivity().getSharedPreferences("day", Context.MODE_PRIVATE);
        day = preferences.getBoolean("day", false);

        preferences = this.getActivity().getSharedPreferences("night", Context.MODE_PRIVATE);
        night = preferences.getBoolean("night", false);

        preferences = this.getActivity().getSharedPreferences("price", Context.MODE_PRIVATE);
        price = preferences.getBoolean("price", false);

        preferences = this.getActivity().getSharedPreferences("event", Context.MODE_PRIVATE);
        event = preferences.getBoolean("event", false);

        preferences = this.getActivity().getSharedPreferences("sym", Context.MODE_PRIVATE);
        sym = preferences.getBoolean("sym", false);

        preferences = this.getActivity().getSharedPreferences("sym_f", Context.MODE_PRIVATE);
        sym_f = preferences.getBoolean("sym_f", false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_alerts, container, false);

        day_switch = view.findViewById(R.id.switch1);
        day_switch.setChecked(day);
        night_switch = view.findViewById(R.id.switch2);
        night_switch.setChecked(night);
        price_switch = view.findViewById(R.id.switch3);
        price_switch.setChecked(price);
        event_switch = view.findViewById(R.id.switch4);
        event_switch.setChecked(event);
        sym_switch = view.findViewById(R.id.switch5);
        sym_switch.setChecked(sym);
        sym_f_switch = view.findViewById(R.id.switch6);
        sym_f_switch.setChecked(sym_f);

        Spinner spinner = view.findViewById(R.id.spinner1);
        String[] items = new String[]{"Small", "Medium", "Big"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);





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

        price_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor = context.getSharedPreferences("price", MODE_PRIVATE).edit();
                    editor.putBoolean("price", true);
                    editor.apply();
                    manage(true,"price");
                }else{
                    editor = context.getSharedPreferences("price", MODE_PRIVATE).edit();
                    editor.putBoolean("price", false);
                    editor.apply();
                    manage(false,"price");
                }
            }
        });

        event_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor = context.getSharedPreferences("event", MODE_PRIVATE).edit();
                    editor.putBoolean("event", true);
                    editor.apply();
                    manage(true,"event");
                }else{
                    editor = context.getSharedPreferences("event", MODE_PRIVATE).edit();
                    editor.putBoolean("event", false);
                    editor.apply();
                    manage(false,"event");
                }
            }
        });

        sym_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor = context.getSharedPreferences("sym", MODE_PRIVATE).edit();
                    editor.putBoolean("sym", true);
                    editor.apply();
                    manage(true,"sym");
                }else{
                    editor = context.getSharedPreferences("sym", MODE_PRIVATE).edit();
                    editor.putBoolean("sym", false);
                    editor.apply();
                    manage(false,"sym");
                }
            }
        });

        sym_f_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    editor = context.getSharedPreferences("sym_f", MODE_PRIVATE).edit();
                    editor.putBoolean("sym_f", true);
                    editor.apply();
                    manage(true,"sym_f");
                }else{
                    editor = context.getSharedPreferences("sym_f", MODE_PRIVATE).edit();
                    editor.putBoolean("sym_f", false);
                    editor.apply();
                    manage(false,"sym_f");
                }
            }
        });


        return view;
    }


    public void manage(boolean sub, String tag) {

        if(sub) {

            Log.e("see"," sub sub sub");

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