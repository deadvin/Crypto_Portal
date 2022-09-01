package com.upperhand.cryptoterminal;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.upperhand.cryptoterminal.interfaces.PostData;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Contacts extends Fragment {



    EditText message;
    EditText email;
    Button send;
    Context context;
    OkHttpClient okHttpClient;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    Retrofit retrofit;
    String api_url;
    PostData post_interface;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        api_url = getString(R.string.url);

        //=========================   RETROFIT

        try {

            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            } };

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        retrofit = new Retrofit.Builder()
                .baseUrl(api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_contact, container, false);


        email = view.findViewById(R.id.et1);
        message = view.findViewById(R.id.et2);
        send = view.findViewById(R.id.btn_send);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (message.getText().toString().equals("") || message.getText().toString().equals(" ")){

                    Toast.makeText(context, "Please fill the message form.",
                            Toast.LENGTH_LONG).show();
                }else {

                    post_interface = retrofit.create(PostData.class);

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

                    Call<RequestBody> call = post_interface.form(requestBody);


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
        context = this.getActivity();
        Log.e("see","settings update");

        super.onResume();
    }

    public void manage(boolean sub, String tag) {

        if(sub) {

            Log.e("see"," sub sub sub");

            FirebaseMessaging.getInstance().subscribeToTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Alert On",
                            Toast.LENGTH_SHORT).show();
                }});
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(context, "Alert Off",
                            Toast.LENGTH_SHORT).show();

                }});
        }
    }

    public void hideKeyboard(View view) {

        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }


}