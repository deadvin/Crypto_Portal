package com.upperhand.cryptoterminal;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.adapters.event_adapter;
import com.upperhand.cryptoterminal.objects.event;
import com.upperhand.cryptoterminal.interfaces.get_data;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class fragment_events extends Fragment {


    ArrayList<event> vidlist;
    ListView mListView;
    event_adapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    get_data api_interface;
    Call<List<event>> call;
    RelativeLayout loading;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Context context;
    ImageButton info;
    ImageButton alert;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String api_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vidlist = new ArrayList<>();
        context = this.getActivity();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        api_url =getString(R.string.url);

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

        api_interface = retrofit.create(get_data.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_events, container, false);

        info = view.findViewById(R.id.button14);
        alert = view.findViewById(R.id.button13);
        loading = view.findViewById(R.id.loadingPanel);
        mListView =  view.findViewById(R.id.listView);

        if(vidlist.isEmpty()){
            mListView.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            call = api_interface.get_events();
            call();
        }else{
            adapter = new event_adapter(context, R.layout.layout_event, vidlist);
            mListView.setAdapter(adapter);
        }



        info.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog customDialog = new Dialog(context);
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.setContentView(R.layout.info);
                customDialog.setCancelable(true);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                customDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                Button cancel =  customDialog.findViewById(R.id.button);

                TextView al1 = customDialog.findViewById(R.id.textView2);
                al1.setText(R.string.info_events);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customDialog.dismiss();
                    }
                });

                customDialog.show();
            }
        });

        return view;
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

    public void call(){

        call.enqueue(new Callback<List<event>>() {
            @Override
            public void onResponse(Call<List<event>> call, Response<List<event>> response) {

                loading.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    load_from_sp();
                    return;
                }
                try {
                    List<event> events = response.body();
                    vidlist.clear();
                    vidlist.addAll(events);
                    Collections.reverse(vidlist);
                    save_sp();

                }catch (Exception e){ }

                adapter = new event_adapter(context, R.layout.layout_event, vidlist);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<event>> call, Throwable t) {
                load_from_sp();
            }
        });
    }

    public void load_from_sp(){

        Gson gson = new Gson();
        String json;
        Type type;

        preferences = context.getSharedPreferences("events", Context.MODE_PRIVATE);
        json = preferences.getString("events", "");
        type = new TypeToken<List<event>>() {
        }.getType();
        vidlist = gson.fromJson(json, type);
        if(vidlist != null && !vidlist.isEmpty()) {

            if (adapter == null) {
                adapter = new event_adapter(context, R.layout.layout_event, vidlist);
                mListView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

    }

    public void save_sp(){

        Gson gson = new Gson();
        String json = gson.toJson(vidlist);

        editor = context.getSharedPreferences("events", MODE_PRIVATE).edit();
        editor.putString("events", json);
        editor.apply();

    }

    @Override
    public void onResume() {

        //   =============   SHARED PREFS

        context = this.getActivity();

        if(vidlist.isEmpty()){
            call = api_interface.get_events();
            call();
        }

        super.onResume();
    }

    @Override
    public void onDestroy() {
        if(call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

}