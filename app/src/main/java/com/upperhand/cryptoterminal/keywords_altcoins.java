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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.interfaces.get_data;
import com.upperhand.cryptoterminal.objects.word;
import com.upperhand.cryptoterminal.adapters.wordsAdapter;
import java.lang.reflect.Type;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

public class keywords_altcoins extends Fragment {


    Button btn_flat;
    Button btn_scaled;
    ArrayList<word> list_words;
    ListView mListView;
    wordsAdapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    boolean scale_vol;
    get_data api_interface;
    Call<List<word>> call;
    RelativeLayout loading;
    Context context;
    ImageButton info;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String api_url;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        api_url = getString(R.string.url);
        list_words = new ArrayList<>();


        //   =============   RETROFIT INTERFACES


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

        api_interface  = retrofit.create(get_data.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_keywords_altoins, container, false);


        loading = view.findViewById(R.id.loadingPanel_altcoins);
        mListView =  view.findViewById(R.id.listView);
        btn_flat = view.findViewById(R.id.button4);
        btn_scaled = view.findViewById(R.id.button5);
        info = view.findViewById(R.id.button12);


        btn_flat.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btn_flat.setTextColor(Color.parseColor("#0A75FF"));
                btn_scaled.setTextColor(Color.parseColor("#FFFFFF"));
                scale_vol = false;

                if(list_words.isEmpty() || is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = api_interface.get_trend();
                    call();

                }else{
                    adapter = new wordsAdapter(context, R.layout.layout_words, list_words, scale_vol);
                    mListView.setAdapter(adapter);
                }

            }
        });

        btn_scaled.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btn_scaled.setTextColor(Color.parseColor("#0A75FF"));
                btn_flat.setTextColor(Color.parseColor("#FFFFFF"));
                scale_vol = true;

                if(list_words.isEmpty() || is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = api_interface.get_trend();
                    call();

                }else{
                    adapter = new wordsAdapter(context, R.layout.layout_words, list_words, scale_vol);
                    mListView.setAdapter(adapter);
                }

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
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                customDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                Button cancel =  customDialog.findViewById(R.id.button);
                TextView al1 = customDialog.findViewById(R.id.textView2);
                al1.setText(R.string.info_keywords_altcoins);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customDialog.dismiss();
                    }
                });

                customDialog.show();
            }
        });

        btn_flat.performClick();

        return view;
    }


    public void call(){

        call.enqueue(new Callback<List<word>>() {
            @Override
            public void onResponse(Call<List<word>> call, Response<List<word>> response) {

                loading.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                if (!response.isSuccessful()) {
                    load_from_sp();
                    return;
                }

                List<word> words = response.body();

                if(words == null || words.isEmpty()){
                    return;
                }

                list_words.clear();
                list_words.addAll(words);
                list_words.sort(new Comparator<word>() {
                    public int compare(word o1, word o2) {
                        return Float.compare(o1.get_last_av(scale_vol), o2.get_last_av(scale_vol));
                    }
                });
                Collections.reverse(list_words);
                save_sp();
                adapter = new wordsAdapter(context, R.layout.layout_words, list_words, scale_vol);
                mListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<word>> call, Throwable t) {

                load_from_sp();
            }
        });
    }


    public void load_from_sp(){

        Log.e("asd","er load altcoins");

        Gson gson = new Gson();
        String json = null;
        Type type;

        if(scale_vol) {
            preferences = context.getSharedPreferences("trends_vol", Context.MODE_PRIVATE);
            json = preferences.getString("trends_vol", "");
        }else{
            preferences = context.getSharedPreferences("trends", Context.MODE_PRIVATE);
            json = preferences.getString("trends", "");
        }

        type = new TypeToken<List<word>>(){}.getType();
        list_words = gson.fromJson(json, type);
        if(list_words != null && !list_words.isEmpty()) {
            if (adapter == null) {
                adapter = new wordsAdapter(context, R.layout.layout_words, list_words, scale_vol);
                mListView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    public void save_sp(){

        Gson gson = new Gson();
        String json = gson.toJson(list_words);

        if(scale_vol) {
            editor = context.getSharedPreferences("trends_vol", MODE_PRIVATE).edit();
            editor.putString("trends_vol", json);
            editor.apply();
        }else {
            editor = context.getSharedPreferences("trends", MODE_PRIVATE).edit();
            editor.putString("trends", json);
            editor.apply();
        }
    }

    public boolean is_refresh(){

        preferences = context.getSharedPreferences("refresh_keywords_altcoins", Context.MODE_PRIVATE);
        boolean refresh = preferences.getBoolean("refresh_keywords_altcoins", false);

        Log.e("asd", " bolean2 " +refresh);

        editor = context.getSharedPreferences("refresh_keywords_altcoins", MODE_PRIVATE).edit();
        editor.putBoolean("refresh_keywords_altcoins", false);
        editor.apply();

        return refresh;
    }

    @Override
    public void onResume() {

        if(is_refresh()){
            loading.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
            call = api_interface.get_trend();
            call();
        }
        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}