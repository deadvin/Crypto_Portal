package com.upperhand.cryptoterminal;

import android.content.Context;
import android.widget.Toast;

public final class Utils {

    private static Toast toast;

    public static void makeToast(Context context, String text){

        if (toast!= null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}