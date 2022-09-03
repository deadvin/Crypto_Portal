package com.upperhand.cryptoterminal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.Context.MODE_PRIVATE;

public final class Utils {

    private static Toast toast;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences preferences;
    private static Dialog customDialog;

    public static void makeToast(String text, Context context){

        if (toast!= null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }


    public static void firebaseSubscribe(boolean subscribe, String tag, Context context) {

        if(subscribe) {
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

    public static void buildAlertDialogue(int resource,Context context){

        customDialog = new Dialog(context);
        customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(resource);
        customDialog.setCancelable(true);
        Window window = customDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        customDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        Button cancel =  customDialog.findViewById(R.id.button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });
    }

    public static Dialog getAlertDialogue(){

        return customDialog;
    }

    public static void setSharedPref(String tag, boolean bool, Context context){
        editor = context.getSharedPreferences(tag, MODE_PRIVATE).edit();
        editor.putBoolean(tag, bool);
        editor.apply();
    }

    public static void setSharedPref(String tag, String string, Context context){
        editor = context.getSharedPreferences(tag, MODE_PRIVATE).edit();
        editor.putString(tag, string);
        editor.apply();
    }

    public static boolean getSharedPref(String tag, boolean defaultVal, Context context){

        preferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
        return preferences.getBoolean(tag, defaultVal);
    }

    public static String getSharedPref(String tag, String defaultVal, Context context){

        preferences = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
        return preferences.getString(tag, defaultVal);
    }



}