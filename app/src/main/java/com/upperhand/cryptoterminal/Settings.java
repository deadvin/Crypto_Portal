package com.upperhand.cryptoterminal;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.content.Context.MODE_PRIVATE;

public class Settings extends Fragment {


    androidx.appcompat.widget.SwitchCompat switch_all;
    androidx.appcompat.widget.SwitchCompat switch_breaking;
    androidx.appcompat.widget.SwitchCompat switch_alts;
    CheckBox checkbox1;
    CheckBox checkbox2;
    Spinner spinner_alts;
    Spinner spinner_breaking;
    EditText message;
    EditText email;
    Button send;
    boolean breaking;
    boolean alts;
    boolean alerts;
    boolean links;
    int breaking_s;
    int alts_s;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    Context context;
    TextView text;

    FirebaseRemoteConfig mFirebaseRemoteConfig;


    LinearLayout ln1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

        preferences = this.getActivity().getSharedPreferences("links", Context.MODE_PRIVATE);
        links = preferences.getBoolean("links", false);

        preferences = this.getActivity().getSharedPreferences("breaking_s", Context.MODE_PRIVATE);
        breaking_s = preferences.getInt("breaking_s", 1);

        preferences = this.getActivity().getSharedPreferences("alts_s", Context.MODE_PRIVATE);
        alts_s = preferences.getInt("alts_s", 1);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);

        switch_breaking = view.findViewById(R.id.switch3);
        switch_alts = view.findViewById(R.id.switch4);
        switch_all = view.findViewById(R.id.switch00);

        spinner_breaking = view.findViewById(R.id.spinner3);
        spinner_alts = view.findViewById(R.id.spinner4);

        checkbox1 = view.findViewById(R.id.checkBox1);
        checkbox2 = view.findViewById(R.id.checkBox2);
        ln1 = view.findViewById(R.id.ln1);

        text = view.findViewById(R.id.textView23);
        text.setText(R.string.resume);

        email = view.findViewById(R.id.et1);
        message = view.findViewById(R.id.et2);
        send = view.findViewById(R.id.btn_send);


        String[] items = new String[]{"None","Soft","Default"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(R.layout.spin_item);

        spinner_breaking.setAdapter(adapter);
        spinner_alts.setAdapter(adapter);

        spinner_breaking.setSelection(breaking_s);
        spinner_alts.setSelection(alts_s);

        spinner_alts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                alts_s = position;
                editor = context.getSharedPreferences("alts_s", MODE_PRIVATE).edit();
                editor.putInt("alts_s", position);
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        spinner_breaking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                breaking_s = position;
                editor = context.getSharedPreferences("breaking_s", MODE_PRIVATE).edit();
                editor.putInt("breaking_s", position);
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        switch_alts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!switch_alts.isPressed()) {
                    return;
                }

                if(isChecked){
                    alts = true;
                    editor = context.getSharedPreferences("alert_alts", MODE_PRIVATE).edit();
                    editor.putBoolean("alert_alts", true);
                    editor.apply();
                    manage(true,"alts");
                }else{
                    alts = false;
                    editor = context.getSharedPreferences("alert_alts", MODE_PRIVATE).edit();
                    editor.putBoolean("alert_alts", false);
                    editor.apply();
                    manage(false,"alts");
                }
            }
        });

        switch_breaking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!switch_breaking.isPressed()) {
                    return;
                }

                if(isChecked){
                    breaking = true;
                    editor = context.getSharedPreferences("alert_breaking", MODE_PRIVATE).edit();
                    editor.putBoolean("alert_breaking", true);
                    editor.apply();
                    manage(true,"breaking");
                }else{
                    breaking = false;
                    editor = context.getSharedPreferences("alert_breaking", MODE_PRIVATE).edit();
                    editor.putBoolean("alert_breaking", false);
                    editor.apply();
                    manage(false,"breaking");
                }
            }
        });

        switch_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!switch_all.isPressed()) {
                    return;
                }

                if(isChecked){
                    alerts = true;
                    editor = context.getSharedPreferences("alerts", MODE_PRIVATE).edit();
                    editor.putBoolean("alerts", true);
                    editor.apply();
                    switch_breaking.setEnabled(true);
                    switch_alts.setEnabled(true);
                }else{
                    alerts = false;
                    editor = context.getSharedPreferences("alerts", MODE_PRIVATE).edit();
                    editor.putBoolean("alerts", false);
                    editor.apply();
                    switch_breaking.setEnabled(false);
                    switch_alts.setEnabled(false);
                }
            }
        });


        if(links){
            checkbox1.setChecked(false);
            checkbox2.setChecked(true);
        }else{
            checkbox1.setChecked(true);
            checkbox2.setChecked(false);
        }

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    checkbox2.setChecked(false);
                    links = false;
                    editor = context.getSharedPreferences("links", MODE_PRIVATE).edit();
                    editor.putBoolean("links", false);
                    editor.apply();
                }else{
                    if(!checkbox1.isPressed()){
                        return;
                    }
                    checkbox1.setChecked(true);
                }
            }
          }
        );

        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    checkbox1.setChecked(false);
                    links = true;
                    editor = context.getSharedPreferences("links", MODE_PRIVATE).edit();
                    editor.putBoolean("links", true);
                    editor.apply();
                }else{
                    if(!checkbox2.isPressed()){
                        return;
                    }
                    checkbox2.setChecked(true);
                }
            }
        }
        );

//        =============================   CONTACT FORM

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (message.getText().toString().equals("") || message.getText().toString().equals(" ")){

                    Toast.makeText(context, "Please fill the message form.",
                            Toast.LENGTH_LONG).show();
                }else {

                    preferences = context.getSharedPreferences("id", Context.MODE_PRIVATE);
                    int id = preferences.getInt("id", 0);
                    String body = message.getText().toString();
                    String mail = email.getText().toString();

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("id", String.valueOf(id))
                            .addFormDataPart("mess", body)
                            .addFormDataPart("mail", mail)
                            .build();

                    Call<RequestBody> call = RetrofitSingleton.get().postData().form(requestBody);


                    call.enqueue(new Callback<RequestBody>() {
                        @Override
                        public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {

                            if (!response.isSuccessful()) {
                                Toast.makeText(context, "Error Code" + response.code(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Thank you for your message!",
                                        Toast.LENGTH_LONG).show();
                                message.setText("");
                                email.setText("");
                                email.clearFocus();
                                message.clearFocus();
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestBody> call, Throwable t) {
                            Toast.makeText(context, "There was problem sending your message.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }



            }
        });

        message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {

        Log.e("see","settings update");

        preferences = this.getActivity().getSharedPreferences("alert_breaking", Context.MODE_PRIVATE);
        breaking = preferences.getBoolean("alert_breaking", false);

        preferences = this.getActivity().getSharedPreferences("alert_alts", Context.MODE_PRIVATE);
        alts = preferences.getBoolean("alert_alts", false);

        preferences = this.getActivity().getSharedPreferences("alerts", Context.MODE_PRIVATE);
        alerts = preferences.getBoolean("alerts", false);

        switch_breaking.setChecked(breaking);
        switch_alts.setChecked(alts);
        switch_all.setChecked(alerts);

        if(alerts){
            switch_breaking.setEnabled(true);
            switch_alts.setEnabled(true);
        }else{
            switch_breaking.setEnabled(false);
            switch_alts.setEnabled(false);
        }

        super.onResume();
    }

    public void manage(boolean sub, String tag) {

        if(sub) {

            Log.e("see"," sub sub sub");

            FirebaseMessaging.getInstance().subscribeToTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "Alert On",
                            Toast.LENGTH_SHORT).show();
                }});
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(getActivity(), "Alert Off",
                            Toast.LENGTH_SHORT).show();

                }});
        }
    }

    public void hideKeyboard(View view) {

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }


}