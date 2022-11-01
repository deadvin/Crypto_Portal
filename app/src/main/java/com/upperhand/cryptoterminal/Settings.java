package com.upperhand.cryptoterminal;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import org.jetbrains.annotations.NotNull;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Settings extends Fragment {

    androidx.appcompat.widget.SwitchCompat switchAll;
    androidx.appcompat.widget.SwitchCompat switchBreaking;
    androidx.appcompat.widget.SwitchCompat switchAlts;
    CheckBox checkboxLinksIn;
    CheckBox checkboxLinksOut;
    Spinner spinnerAlts;
    Spinner spinnerBreaking;
    EditText editTextMessage;
    EditText editTextEmail;
    Button btnSend;
    boolean isBreakingOn;
    boolean isAltson;
    boolean isalertsOn;
    boolean islinksIn;
    int alertBreakingSound;
    int alertAltsSound;
    Context context;
    TextView textViewResume;
    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        islinksIn = Utils.getSharedPref("links", false, context);
        alertBreakingSound = Utils.getSharedPref("breaking_sound", 1, context);
        alertAltsSound = Utils.getSharedPref("breaking_sound", 1, context);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);

        switchBreaking = view.findViewById(R.id.switch3);
        switchAlts = view.findViewById(R.id.switch4);
        switchAll = view.findViewById(R.id.switch00);
        spinnerBreaking = view.findViewById(R.id.spinner3);
        spinnerAlts = view.findViewById(R.id.spinner4);
        checkboxLinksIn = view.findViewById(R.id.checkBox1);
        checkboxLinksOut = view.findViewById(R.id.checkBox2);
        textViewResume = view.findViewById(R.id.textViewResume);
        textViewResume.setText(R.string.resume);
        editTextEmail = view.findViewById(R.id.et1);
        editTextMessage = view.findViewById(R.id.et2);
        btnSend = view.findViewById(R.id.btn_send);

        String[] items = new String[]{"None","Soft","Default"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(R.layout.spin_item);
        spinnerBreaking.setAdapter(adapter);
        spinnerAlts.setAdapter(adapter);
        spinnerBreaking.setSelection(alertBreakingSound);
        spinnerAlts.setSelection(alertAltsSound);

        spinnerAlts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                alertAltsSound = position;
                Utils.setSharedPref("alertAltsSound", alertAltsSound, context);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        spinnerBreaking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                alertBreakingSound = position;
                Utils.setSharedPref("alertBreakingSound", alertBreakingSound, context);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        switchAlts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!switchAlts.isPressed()) {
                    return;
                }

                if(isChecked){
                    isAltson = true;
                    Utils.setSharedPref("alert_alts", isAltson, context);
                    manage(true,"alts");
                }else{
                    isAltson = false;
                    Utils.setSharedPref("alert_alts", isAltson, context);
                    manage(false,"alts");
                }
            }
        });

        switchBreaking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!switchBreaking.isPressed()) {
                    return;
                }

                if(isChecked){
                    isBreakingOn = true;
                    Utils.setSharedPref("alert_breaking", isBreakingOn, context);
                    manage(true,"breaking");
                }else{
                    isBreakingOn = false;
                    Utils.setSharedPref("alert_breaking", isBreakingOn, context);
                    manage(false,"breaking");
                }
            }
        });

        switchAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!switchAll.isPressed()) {
                    return;
                }

                if(isChecked){
                    isalertsOn = true;
                    Utils.setSharedPref("alerts", isalertsOn, context);
                    switchBreaking.setEnabled(true);
                    switchAlts.setEnabled(true);
                }else{
                    isalertsOn = false;
                    Utils.setSharedPref("alerts", isalertsOn, context);
                    switchBreaking.setEnabled(false);
                    switchAlts.setEnabled(false);
                }
            }
        });

        if(islinksIn){
            checkboxLinksIn.setChecked(false);
            checkboxLinksOut.setChecked(true);
        }else{
            checkboxLinksIn.setChecked(true);
            checkboxLinksOut.setChecked(false);
        }

        checkboxLinksIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    checkboxLinksOut.setChecked(false);
                    islinksIn = false;
                    Utils.setSharedPref("links", islinksIn, context);
                }else{
                    if(!checkboxLinksIn.isPressed()){
                        return;
                    }
                    checkboxLinksIn.setChecked(true);
                }
            }});

        checkboxLinksOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    checkboxLinksIn.setChecked(false);
                    islinksIn = true;
                    Utils.setSharedPref("links", islinksIn, context);
                }else{
                    if(!checkboxLinksOut.isPressed()){
                        return;
                    }
                    checkboxLinksOut.setChecked(true);
                }
            }});

        //        =============================   CONTACT FORM

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextMessage.getText().toString().equals("") || editTextMessage.getText().toString().equals(" ")){

                    Utils.makeToast("Please fill the Message form.", context);

                }else {

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("id", String.valueOf(Utils.getSharedPref("id", 0 , context)))
                            .addFormDataPart("mess", editTextMessage.getText().toString())
                            .addFormDataPart("mail", editTextEmail.getText().toString())
                            .build();

                    Call<RequestBody> call = RetrofitSingleton.get().postData().form(requestBody);

                    call.enqueue(new Callback<RequestBody>() {
                        @Override
                        public void onResponse(@NotNull Call<RequestBody> call, @NotNull Response<RequestBody> response) {

                            if (!response.isSuccessful()) {
                                Utils.makeToast("Error Code" + response.code(), context);
                            } else {
                                Utils.makeToast("Thank you for your message!", context);
                                editTextMessage.setText("");
                                editTextEmail.setText("");
                                editTextEmail.clearFocus();
                                editTextMessage.clearFocus();
                            }
                        }
                        @Override
                        public void onFailure(@NotNull Call<RequestBody> call, Throwable t) {
                            Utils.makeToast("There was problem sending your Message.", context);
                        }
                    });
                }
            }
        });

        editTextMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        isBreakingOn = Utils.getSharedPref("alert_breaking", false, context);
        isAltson = Utils.getSharedPref("alert_alts", false, context);
        isalertsOn = Utils.getSharedPref("alerts", false, context);

        switchBreaking.setChecked(isBreakingOn);
        switchAlts.setChecked(isAltson);
        switchAll.setChecked(isalertsOn);

        if(isalertsOn){
            switchBreaking.setEnabled(true);
            switchAlts.setEnabled(true);
        }else{
            switchBreaking.setEnabled(false);
            switchAlts.setEnabled(false);
        }

        super.onResume();
    }

    public void manage(boolean sub, String tag) {

        if(sub) {
            FirebaseMessaging.getInstance().subscribeToTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Utils.makeToast("Alert On", context);
                }});
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Utils.makeToast("Alert Off", context);
                }});
        }
    }

    public void hideKeyboard(View view) {

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }


}