package com.upperhand.cryptoterminal;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.interfaces.get_data;
import com.upperhand.cryptoterminal.objects.video;
import com.upperhand.cryptoterminal.adapters.video_adapter;
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

public class fragment_news extends Fragment {


    ArrayList<video> list_all;
    ArrayList<video> list_top;
    ListView mListView;
    video_adapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    Call<List<video>> call;
    RelativeLayout loading;
    get_data api_interface;
    Button btn_all;
    Button btn_top;
    boolean all;
    boolean alerts;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    Context context;
    ImageButton info;
    ImageButton alert;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String api_url;
    Toast toast;
    String selected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list_all = new ArrayList<>();
        list_top = new ArrayList<>();

        context = this.getActivity();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        api_url = getString(R.string.url);

        preferences = context.getSharedPreferences("news", Context.MODE_PRIVATE);
        alerts = preferences.getBoolean("news", false);

        //===============================   RETROFIT

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
        View view =  inflater.inflate(R.layout.fragment_news, container, false);

        btn_all = view.findViewById(R.id.button1);
        btn_top = view.findViewById(R.id.button2);
        info = view.findViewById(R.id.button4);
        alert = view.findViewById(R.id.button3);

        loading = view.findViewById(R.id.loadingPanel);
        mListView =  view.findViewById(R.id.listView);


        btn_all.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btn_all.setTextColor(Color.parseColor("#0A75FF"));
                btn_top.setTextColor(Color.parseColor("#FFFFFF"));
                all = true;
                selected ="news";
                if(list_all.isEmpty() || is_refresh()){
                    mListView.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    call = api_interface.get_news();
                    call(list_all);
                }else{
                    adapter = new video_adapter(context, R.layout.layout_video, list_all);
                    mListView.setAdapter(adapter);
                }
            }
        });

        btn_top.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btn_all.setTextColor(Color.parseColor("#FFFFFF"));
                btn_top.setTextColor(Color.parseColor("#0A75FF"));
                all = false;
                selected ="news_top";
                if(list_top.isEmpty() || is_refresh()){
                    mListView.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    call = api_interface.get_news_f();
                    call(list_top);
                }else{
                    adapter = new video_adapter(context, R.layout.layout_video, list_top);
                    mListView.setAdapter(adapter);
                }

            }
        });


        alert.setOnClickListener( new View.OnClickListener() {    //     FILTERED

            @Override
            public void onClick(View v) {

                final Dialog customDialog = new Dialog(context);
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.setContentView(R.layout.alert_all);
                customDialog.setCancelable(true);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                customDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                Button cancel =  customDialog.findViewById(R.id.button);


                TextView al1 = customDialog.findViewById(R.id.textView4);
                TextView al2 = customDialog.findViewById(R.id.textView5);
                androidx.appcompat.widget.SwitchCompat switch_1 = customDialog.findViewById(R.id.switch1);
                androidx.appcompat.widget.SwitchCompat switch_2 = customDialog.findViewById(R.id.switch2);

                switch_1.setChecked(alerts);
                al1.setText("Above 2 million followers");

                al2.setVisibility(View.GONE);
                switch_2.setVisibility(View.GONE);


                switch_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked){
                            alerts = true;
                            editor = context.getSharedPreferences("news", MODE_PRIVATE).edit();
                            editor.putBoolean("news", true);
                            editor.apply();
                            manage(true,"news");
                        }else{
                            alerts = false;
                            editor = context.getSharedPreferences("news", MODE_PRIVATE).edit();
                            editor.putBoolean("news", false);
                            editor.apply();
                            manage(false,"news");
                        }
                    }
                });




                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customDialog.dismiss();
                    }
                });

                customDialog.show();
            }
        });

        info.setOnClickListener( new View.OnClickListener() {    //     FILTERED

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
                al1.setText(R.string.info_news);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customDialog.dismiss();
                    }
                });

                customDialog.show();
            }
        });


        btn_top.performClick();

        return view;
    }

    public void call(ArrayList<video> list){

        call.enqueue(new Callback<List<video>>() {
            @Override
            public void onResponse(Call<List<video>> call, Response<List<video>> response) {

                loading.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    if (toast!= null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(context, "Error Code" + response.code(), Toast.LENGTH_LONG);
                    toast.show();
                    load_from_sp();
                    return;
                }

                List<video> posts = response.body();
                if(posts == null || posts.isEmpty()){
                    return;
                }

                list.clear();
                list.addAll(posts);
                Collections.reverse(list);
                save_sp(list);

                adapter = new video_adapter(context, R.layout.layout_video, list);
                mListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<video>> call, Throwable t) {
                load_from_sp();
            }
        });
    }

    public void load_from_sp(){

        Gson gson = new Gson();
        String json = null;
        Type type;
        ArrayList<video> list;

        switch (selected) {
            case "news":
                if(!list_all.isEmpty()){ return;}
                break;
            case "news_top":
                if(!list_top.isEmpty()){ return;}
                break;
        }

        switch (selected) {
            case "news":
                preferences = context.getSharedPreferences("news_all", Context.MODE_PRIVATE);
                json = preferences.getString("news_all", "");
                break;
            case "news_top":
                preferences = context.getSharedPreferences("news_top", Context.MODE_PRIVATE);
                json = preferences.getString("news_top", "");
                break;
        }

        type = new TypeToken<List<video>>(){}.getType();
        list = gson.fromJson(json, type);
        if(list != null && !list.isEmpty()) {

            adapter = new video_adapter(context, R.layout.layout_video, list);
            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

            switch (selected) {
                case "news":
                    list_all = list;
                    break;
                case "news_top":
                    list_top = list;
                    break;
            }
        }

    }

    public void save_sp(ArrayList<video> list){

        Gson gson = new Gson();
        String json = gson.toJson(list);

        switch (selected) {
            case "news":
                editor = context.getSharedPreferences("news_all", MODE_PRIVATE).edit();
                editor.putString("news_all", json);
                editor.apply();
                break;
            case "news_top":
                editor = context.getSharedPreferences("news_top", MODE_PRIVATE).edit();
                editor.putString("news_top", json);
                editor.apply();
                break;
        }

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



    @Override
    public void onDestroy() {
        if(call != null) {
            call.cancel();
        }

        super.onDestroy();
    }

    public boolean is_refresh(){

        preferences = context.getSharedPreferences("refresh_" + selected, Context.MODE_PRIVATE);
        boolean refresh = preferences.getBoolean("refresh_" + selected, false);

        editor = context.getSharedPreferences("refresh_" + selected, MODE_PRIVATE).edit();
        editor.putBoolean("refresh_" + selected, false);
        editor.apply();

        return refresh;
    }

    @Override
    public void onResume() {
        context = this.getActivity();
        super.onResume();
    }
}