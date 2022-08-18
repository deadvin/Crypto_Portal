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

public class fragment_videos extends Fragment {


    ArrayList<video> symlist;
    ListView mListView;
    video_adapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    get_data api_interface;
    Call<List<video>> call;
    RelativeLayout loading;

    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    boolean vid;
    Context context;
    ImageButton info;
    ImageButton alert;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String api_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        api_url = getString(R.string.url);
        symlist = new ArrayList<>();

        //============================  RETROFIT

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
        View view =  inflater.inflate(R.layout.fragment_video, container, false);

        info = view.findViewById(R.id.button14);
        alert = view.findViewById(R.id.button13);
        loading = view.findViewById(R.id.loadingPanel);
        mListView =  view.findViewById(R.id.listView);
        loading.setVisibility(View.VISIBLE);

        call = api_interface.get_video();
        call();

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

                switch_1.setChecked(vid);
                al1.setText("Above 2 million followers");

                al2.setVisibility(View.GONE);
                switch_2.setVisibility(View.GONE);


                switch_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked){
                            vid = true;
                            editor = context.getSharedPreferences("vid", MODE_PRIVATE).edit();
                            editor.putBoolean("vid", true);
                            editor.apply();
                            manage(true,"vid");
                        }else{
                            vid = false;
                            editor = context.getSharedPreferences("vid", MODE_PRIVATE).edit();
                            editor.putBoolean("vid", false);
                            editor.apply();
                            manage(false,"vid");
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
                al1.setText(R.string.info_vid);


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

    @Override
    public void onResume() {

        if(is_refresh()){
            mListView.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.VISIBLE);
            call = api_interface.get_video();
            call();
        }

        super.onResume();
    }

    public void call(){

        call.enqueue(new Callback<List<video>>() {
            @Override
            public void onResponse(Call<List<video>> call, Response<List<video>> response) {

                loading.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    load_from_sp();
                    return;
                }

                symlist.clear();
                try {
                    List<video> videos = response.body();
                    symlist.addAll(videos);
                }catch (Exception e){

                }

                Collections.reverse(symlist);
                save_sp(symlist);

                adapter = new video_adapter(context, R.layout.layout_video, symlist);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<video>> call, Throwable t) {
                load_from_sp();
            }
        });
    }

    public void load_from_sp(){


        Gson gson = new Gson();
        String json;
        Type type;
        ArrayList<video> list;

        if(!symlist.isEmpty()){ return;}

        preferences = context.getSharedPreferences("videos", Context.MODE_PRIVATE);
        json = preferences.getString("videos", "");
        type = new TypeToken<List<video>>() {
        }.getType();
        list = gson.fromJson(json, type);
        if(list != null && !list.isEmpty()) {

            if (adapter == null) {
                adapter = new video_adapter(context, R.layout.layout_video, list);
                mListView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();

            loading.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

    }

    public void save_sp(ArrayList<video> list){

        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor = context.getSharedPreferences("videos", MODE_PRIVATE).edit();
        editor.putString("videos", json);
        editor.apply();

    }

    public boolean is_refresh(){

        preferences = context.getSharedPreferences("refresh_vid", Context.MODE_PRIVATE);
        boolean refresh = preferences.getBoolean("refresh_vid", false);

        editor = context.getSharedPreferences("refresh_vid", MODE_PRIVATE).edit();
        editor.putBoolean("refresh_vid", false);
        editor.apply();

        return refresh;
    }

    @Override
    public void onDestroy() {
        if(call != null) {
            call.cancel();
        }
        super.onDestroy();
    }




}