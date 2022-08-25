package com.upperhand.cryptoterminal;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.adapters.tweet_adapter;
import com.upperhand.cryptoterminal.adapters.breaking_adapter;
import com.upperhand.cryptoterminal.interfaces.get_data;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.upperhand.cryptoterminal.interfaces.post_data;
import com.upperhand.cryptoterminal.objects.div;
import com.upperhand.cryptoterminal.objects.tweet;

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

public class fragment_twitter extends Fragment {

    //region VARS
    Button btn1;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn11;
    Button[] btn_list2 = new Button[4];
    ArrayList<tweet> list_all_1;
    ArrayList<tweet> list_all_100;
    ArrayList<tweet> list_all_10;
    ArrayList<tweet> list_all_500;
    ArrayList<tweet> list_sym;
    ArrayList<tweet> list_sym_f;
    ArrayList<tweet> list_rep_f;
    ArrayList<tweet> list_rep;
    ImageButton info_all;
    ImageButton alert_all;
    ImageButton info_rep;
    ImageButton alert_rep;
    Toast toast;
    ListView mListView;
    tweet_adapter adapter;
    breaking_adapter breaking_adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    LinearLayout hsv;
    LinearLayout hsv2;
    LinearLayout linearLayout;
    boolean two_tabs;
    String selected = "";
    get_data get_interface;
    post_data post_interface;
    Call<List<tweet>> call;
    Call<String> call2;
    RelativeLayout loading;
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    boolean alert_breaking;
    boolean alert_alts;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String api_url;
    AdView adView;
    Toast mToast;



    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
//        api_url = mFirebaseRemoteConfig.getString("url");
        api_url = getString(R.string.url);

        list_all_1 = new ArrayList<>();
        list_all_10 = new ArrayList<>();
        list_all_100 = new ArrayList<>();
        list_all_500 = new ArrayList<>();
        list_sym = new ArrayList<>();
        list_sym_f = new ArrayList<>();
        list_rep = new ArrayList<>();
        list_rep_f = new ArrayList<>();

        //   ===================   RETROFIT INSTANCE   ===================

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

        get_interface = retrofit.create(get_data.class);
        post_interface  = retrofit.create(post_data.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_twitter, container, false);

        //   ===================   ADVIEW   ===================

        //region ads

//        adView = new AdView(context);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-1582921325835661/4374148333");
//        adView = view.findViewById(R.id.adView);
//
//        MobileAds.initialize(context, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//            }
//        });
//
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                adView.setVisibility(View.VISIBLE);
//                super.onAdLoaded();
//            }
//        });
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

        //endregion

        loading = view.findViewById(R.id.loadingPanel);
        hsv =  view.findViewById(R.id.linearLayout1);
        hsv2 =  view.findViewById(R.id.linearLayout2);
        linearLayout =  view.findViewById(R.id.linearLayout3);
        mListView =  view.findViewById(R.id.listView);
        btn1 = view.findViewById(R.id.button1);
        btn3 = view.findViewById(R.id.button3);
        btn4 = view.findViewById(R.id.button4);
        btn5 = view.findViewById(R.id.button5);
        btn6 = view.findViewById(R.id.button6);
        btn7 = view.findViewById(R.id.button7);
        btn8 = view.findViewById(R.id.button8);
        btn9 = view.findViewById(R.id.button9);
        btn11 = view.findViewById(R.id.button11);
        info_all = view.findViewById(R.id.button12);
        alert_all = view.findViewById(R.id.button10);
        alert_rep = view.findViewById(R.id.button13);
        info_rep = view.findViewById(R.id.button14);
        btn_list2[0] = btn4;
        btn_list2[1] = btn5;
        btn_list2[2] = btn6;
        btn_list2[3] = btn7;


        btn1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hsv2.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                two_tabs = false;

                reset_colour2();
                btn1.setTextColor(Color.parseColor("#0A75FF"));
                btn3.setTextColor(Color.parseColor("#FFFFFF"));
                btn11.setTextColor(Color.parseColor("#FFFFFF"));
                btn6.setTextColor(Color.parseColor("#0A75FF"));
                selected = "1";
                if(list_all_1.isEmpty() || is_refresh()){
                    mListView.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    call = get_interface.get_1();
                    call(list_all_1);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_1);
                    mListView.setAdapter(adapter);
                }
            }
        }); //     CRYPTO

        btn3.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hsv2.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                btn9.setText("filter");

                btn1.setTextColor(Color.parseColor("#FFFFFF"));
                btn3.setTextColor(Color.parseColor("#0A75FF"));
                btn8.setTextColor(Color.parseColor("#0A75FF"));
                btn11.setTextColor(Color.parseColor("#FFFFFF"));
                btn9.setTextColor(Color.parseColor("#FFFFFF"));
                selected = "sym";

                if(list_sym.isEmpty() || is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_sym();
                    call(list_sym);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_sym);
                    mListView.setAdapter(adapter);
                }

            }
        });    //   ALTCOINS

        btn11.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hsv2.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                btn9.setText("crypto");

                btn1.setTextColor(Color.parseColor("#FFFFFF"));
                btn3.setTextColor(Color.parseColor("#FFFFFF"));
                btn8.setTextColor(Color.parseColor("#FFFFFF"));
                btn9.setTextColor(Color.parseColor("#0A75FF"));
                btn11.setTextColor(Color.parseColor("#0A75FF"));
                selected = "breaking_f";

                if(list_rep_f.isEmpty() || is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_rep_f();
                    call(list_rep_f);
                }else{
                    breaking_adapter = new breaking_adapter(context, R.layout.layout_breaking,list_rep_f);
                    mListView.setAdapter(breaking_adapter);
                }

            }
        });    //   BREAKING

        btn4.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset_colour2();
                btn4.setTextColor(Color.parseColor("#0A75FF"));
                selected = "100";

                if(list_all_100.isEmpty() || is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_100();
                    call(list_all_100);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_100);
                    mListView.setAdapter(adapter);
                }
            }
        });    //    100

        btn5.setOnClickListener( new View.OnClickListener() {    // 500

            @Override
            public void onClick(View v) {

                reset_colour2();
                btn5.setTextColor(Color.parseColor("#0A75FF"));
                selected = "500";

                if(list_all_500.isEmpty() || is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_500();
                    call(list_all_500);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_500);
                    mListView.setAdapter(adapter);
                }
            }
        });  // 500

        btn6.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset_colour2();
                btn6.setTextColor(Color.parseColor("#0A75FF"));
                selected = "1";

                if(list_all_1.isEmpty() || is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_1();
                    call(list_all_1);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_1);
                    mListView.setAdapter(adapter);
                }
            }
        });   //    2m

        btn7.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset_colour2();
                btn7.setTextColor(Color.parseColor("#0A75FF"));
                selected = "10";

                if(list_all_10.isEmpty() || is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_10();
                    call(list_all_10);

                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_10);
                    mListView.setAdapter(adapter);
                }
            }
        });    // 10m

        btn8.setOnClickListener( new View.OnClickListener() {   //   ALL

            @Override
            public void onClick(View v) {

                btn8.setTextColor(Color.parseColor("#0A75FF"));
                btn9.setTextColor(Color.parseColor("#FFFFFF"));

                if (selected.equals("sym") || selected.equals("sym_f")){
                    selected = "sym";
                    if(list_sym.isEmpty() || is_refresh()){
                        loading.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.INVISIBLE);
                        call = get_interface.get_sym();
                        call(list_sym);

                    }else{
                        adapter = new tweet_adapter(context, R.layout.layout_tweet,list_sym);
                        mListView.setAdapter(adapter);
                    }
                }else{
                    selected = "breaking";
                    if(list_rep.isEmpty() || is_refresh()){
                        loading.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.INVISIBLE);
                        call = get_interface.get_rep();
                        call(list_rep);
                    }else{
                        breaking_adapter = new breaking_adapter(context, R.layout.layout_breaking,list_rep);
                        mListView.setAdapter(breaking_adapter);
                    }
                }
            }
        });   //   ALL

        btn9.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btn9.setTextColor(Color.parseColor("#0A75FF"));
                btn8.setTextColor(Color.parseColor("#FFFFFF"));

                if (selected.equals("sym") || selected.equals("sym_f")) {

                    selected = "sym_f";
                    if (list_sym_f.isEmpty() || is_refresh()) {
                        loading.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.INVISIBLE);
                        call = get_interface.get_sym_f();
                        call(list_sym_f);
                    } else {
                        adapter = new tweet_adapter(context, R.layout.layout_tweet, list_sym_f);
                        mListView.setAdapter(adapter);
                    }
                }else{
                    selected = "breaking_f";
                    if(list_rep_f.isEmpty() || is_refresh()){
                        loading.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.INVISIBLE);
                        call = get_interface.get_rep_f();
                        call(list_rep_f);
                    }else{
                        breaking_adapter = new breaking_adapter(context, R.layout.layout_breaking,list_rep_f);
                        mListView.setAdapter(breaking_adapter);
                    }
                }
            }
        });   //     FILTERED

        alert_rep.setOnClickListener( new View.OnClickListener() {    //     FILTERED

            @Override
            public void onClick(View v) {

                final Dialog customDialog = new Dialog(context);
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.setContentView(R.layout.alert_all);
                customDialog.setCancelable(true);
                Window window = customDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                customDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                Button cancel =  customDialog.findViewById(R.id.button);

                TextView al1 = customDialog.findViewById(R.id.textView4);
                TextView al2 = customDialog.findViewById(R.id.textView5);
                androidx.appcompat.widget.SwitchCompat switch_1 = customDialog.findViewById(R.id.switch1);
                androidx.appcompat.widget.SwitchCompat switch_2 = customDialog.findViewById(R.id.switch2);

                switch_1.setChecked(alert_alts);
                switch_2.setChecked(alert_breaking);
                al1.setText("Altcoin official accounts");
                al2.setText("    Breaking news    ");


                switch_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            alert_alts = true;
                            editor = context.getSharedPreferences("alert_alts", MODE_PRIVATE).edit();
                            editor.putBoolean("alert_alts", true);
                            editor.apply();
                            manage(true, "alts");
                        } else {
                            alert_alts = false;
                            editor = context.getSharedPreferences("alert_alts", MODE_PRIVATE).edit();
                            editor.putBoolean("alert_alts", false);
                            editor.apply();
                            manage(false, "alts");
                        }
                    }
                });

                switch_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            alert_breaking = true;
                            editor = context.getSharedPreferences("alert_breaking", MODE_PRIVATE).edit();
                            editor.putBoolean("alert_breaking", true);
                            editor.apply();
                            manage(true, "alts");
                        } else {
                            alert_alts = false;
                            editor = context.getSharedPreferences("alert_breaking", MODE_PRIVATE).edit();
                            editor.putBoolean("alert_breaking", false);
                            editor.apply();
                            manage(false, "alts");
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

        info_all.setOnClickListener( new View.OnClickListener() {    //     FILTERED

            @Override
            public void onClick(View v) {

                final Dialog customDialog = new Dialog(context);
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.setContentView(R.layout.info);
                customDialog.setCancelable(true);
                Window window = customDialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                customDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                Button cancel =  customDialog.findViewById(R.id.button);

                TextView al1 = customDialog.findViewById(R.id.textView2);
                al1.setText(R.string.info_all);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customDialog.dismiss();
                    }
                });

                customDialog.show();
            }
        });

        info_rep.setOnClickListener( new View.OnClickListener() {    //     FILTERED

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
                if(selected.equals("breaking") || selected.equals("breaking_f")){
                    al1.setText(R.string.info_breaking);
                }else{
                    al1.setText(R.string.info_alts);
                }

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });

                customDialog.show();
            }
        });

        btn1.performClick();

//        ==============   HIDE POSTS

//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int pos, long id) {
//
//
//                tweet tweet =  breaking_adapter.getItem(pos);
////                Log.d("Yourtag", post.getLink());
//
//                switch (selected) {
//                    case "100":
//
//                        break;
//                    case "500":
//
//                        break;
//                    case "1":
//
//                        break;
//                    case "10":
//
//                        break;
//                    case "sym":
//
//                        break;
//                    case "breaking_f":
//                        call2 = post_interface.postlink_rep_f(tweet.getLink() + " 468259731");
//                        send_link();
//                        break;
//                }
//
//
//                return true;
//            }
//        });
//
        return view;
    }


    public void call(ArrayList<tweet> list){

        call.enqueue(new Callback<List<tweet>>() {
            @Override
            public void onResponse(Call<List<tweet>> call, Response<List<tweet>> response) {

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
                List<tweet> tweets = response.body();
                if(tweets == null || tweets.isEmpty()){
                    return;
                }

                list.clear();
                list.addAll(tweets);
                Collections.reverse(list);
                save_sp(list);

                if(selected.equals("breaking") || selected.equals("breaking_f")){
                    breaking_adapter = new breaking_adapter(context, R.layout.layout_breaking, list);
                    mListView.setAdapter(breaking_adapter);
                    breaking_adapter.notifyDataSetChanged();
                }else {
                    adapter = new tweet_adapter(context, R.layout.layout_tweet, list);
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onFailure(Call<List<tweet>> call, Throwable t) {
                if(list_all_1.isEmpty() || is_refresh()) {
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(context, "Problem connecting the server. Loading old data.",
                            Toast.LENGTH_LONG);
                    mToast.show();
                }
                load_from_sp();
            }
        });
    }



    public void load_from_sp(){

        Gson gson = new Gson();
        String json = null;
        Type type;
        ArrayList<tweet> list;

        switch (selected) {
            case "100":
                if(!list_all_100.isEmpty()){ return; }
                preferences = context.getSharedPreferences("100", Context.MODE_PRIVATE);
                json = preferences.getString("100", "");
                break;
            case "500":
                if(!list_all_500.isEmpty()){ return; }
                preferences = context.getSharedPreferences("500", Context.MODE_PRIVATE);
                json = preferences.getString("500", "");
                break;
            case "1":
                if(!list_all_1.isEmpty()){ return; }
                preferences = context.getSharedPreferences("1", Context.MODE_PRIVATE);
                json = preferences.getString("1", "");
                break;
            case "10":
                if(!list_all_10.isEmpty()){ return; }
                preferences = context.getSharedPreferences("10", Context.MODE_PRIVATE);
                json = preferences.getString("10", "");
                break;
            case "sym":
                if(!list_sym.isEmpty()){ return; }
                preferences = context.getSharedPreferences("sym", Context.MODE_PRIVATE);
                json = preferences.getString("sym", "");
                break;
            case "sym_f":
                if(!list_sym_f.isEmpty()){ return; }
                preferences = context.getSharedPreferences("sym_f", Context.MODE_PRIVATE);
                json = preferences.getString("sym_f", "");
                break;
            case "breaking":
                if(!list_rep.isEmpty()){ return; }
                preferences = context.getSharedPreferences("breaking", Context.MODE_PRIVATE);
                json = preferences.getString("breaking", "");
                break;
            case "breaking_f":
                if(!list_rep_f.isEmpty()){ return; }
                preferences = context.getSharedPreferences("breaking_f", Context.MODE_PRIVATE);
                json = preferences.getString("breaking_f", "");
                break;
        }


        type = new TypeToken<List<tweet>>(){}.getType();
        list = gson.fromJson(json, type);
        if(list != null && !list.isEmpty()) {

            if(selected.equals("breaking") || selected.equals("breaking_f")){
                breaking_adapter = new breaking_adapter(context, R.layout.layout_breaking, list);
                mListView.setAdapter(breaking_adapter);
                breaking_adapter.notifyDataSetChanged();
            }else {
                adapter = new tweet_adapter(context, R.layout.layout_tweet, list);
                mListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            loading.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);

//       ===========================  FILL GLOBAL LIST

            switch (selected) {
                case "100":
                    list_all_100 = list;
                    break;
                case "500":
                    list_all_500 = list;
                    break;
                case "1":
                    list_all_1 = list;
                    break;
                case "10":
                    list_all_10 = list;
                    break;
                case "sym":
                    list_sym = list;
                    break;
                case "sym_f":
                    list_sym_f = list;
                    break;
                case "breaking":
                    list_rep = list;
                    break;
                case "breaking_f":
                    list_rep_f = list;
                    break;
            }

        }
    }

    public void save_sp(ArrayList<tweet> list){

        Gson gson = new Gson();
        String json = gson.toJson(list);

        switch (selected) {  //              CHANGE ALL  WITH SELECTER ===============================================================
            case "100":
                editor = context.getSharedPreferences("100", MODE_PRIVATE).edit();
                editor.putString("100", json);
                editor.apply();
                break;
            case "500":
                editor = context.getSharedPreferences("500", MODE_PRIVATE).edit();
                editor.putString("500", json);
                editor.apply();
                break;
            case "1":
                editor = context.getSharedPreferences("1", MODE_PRIVATE).edit();
                editor.putString("1", json);
                editor.apply();
                break;
            case "10":
                editor = context.getSharedPreferences("10", MODE_PRIVATE).edit();
                editor.putString("10", json);
                editor.apply();
                break;
            case "sym":
                editor = context.getSharedPreferences("sym", MODE_PRIVATE).edit();
                editor.putString("sym", json);
                editor.apply();
                break;
            case "sym_f":
                editor = context.getSharedPreferences("sym_f", MODE_PRIVATE).edit();
                editor.putString("sym_f", json);
                editor.apply();
                break;
            case "breaking":
                editor = context.getSharedPreferences("breaking", MODE_PRIVATE).edit();
                editor.putString("breaking", json);
                editor.apply();
                break;
            case "breaking_f":
                editor = context.getSharedPreferences("breaking_f", MODE_PRIVATE).edit();
                editor.putString("breaking_f", json);
                editor.apply();
                break;
        }
    }

    public void send_link(){

        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("see",t.getMessage());
                Toast.makeText(context, "Problem connecting the server." ,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void reset_colour2() {
        for (int i = 0; i < 4; i++) {
            btn_list2[i].setBackgroundColor(Color.parseColor("#3b3b3b"));
            btn_list2[i].setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public boolean is_refresh(){

        preferences = context.getSharedPreferences("refresh_" + selected, Context.MODE_PRIVATE);
        boolean refresh = preferences.getBoolean("refresh_" + selected, false);

        editor = context.getSharedPreferences("refresh_" + selected, MODE_PRIVATE).edit();
        editor.putBoolean("refresh_" + selected, false);
        editor.apply();

        return refresh;
    }

    public void manage(boolean sub, String tag) {

        if(sub) {
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

        //   =============   SHARED PREFS

        context = this.getActivity();

        preferences = context.getSharedPreferences("alert_alts", Context.MODE_PRIVATE);
        alert_alts = preferences.getBoolean("alert_alts", false);

        preferences = context.getSharedPreferences("alert_breaking", Context.MODE_PRIVATE);
        alert_breaking = preferences.getBoolean("alert_breaking", false);

        preferences = context.getSharedPreferences("topic", Context.MODE_PRIVATE);
        String topic = preferences.getString("topic", "none");

        if(topic.equals("breaking")){
            selected = "braking_f";
            btn11.performClick();
            editor = context.getSharedPreferences("topic", MODE_PRIVATE).edit();
            editor.putString("topic", "none");
            editor.apply();
        }else if(topic.equals("alts")){
            selected = "sym";
            btn3.performClick();
            editor = context.getSharedPreferences("topic", MODE_PRIVATE).edit();
            editor.putString("topic", "none");
            editor.apply();
        }

        switch (selected) {
            case "100":
                if(is_refresh()){
                    mListView.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    call = get_interface.get_100();
                    call(list_all_100);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_100);
                    mListView.setAdapter(adapter);
                }
                break;
            case "500":
                if(is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_500();
                    call(list_all_500);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_500);
                    mListView.setAdapter(adapter);
                }
                break;
            case "1":
                if(is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_1();
                    call(list_all_1);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_1);
                    mListView.setAdapter(adapter);
                }
                break;
            case "10":
                if(is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_10();
                    call(list_all_10);

                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_all_10);
                    mListView.setAdapter(adapter);
                }
                break;
            case "sym":
                if(is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_sym();
                    call(list_sym);
                }else{
                    adapter = new tweet_adapter(context, R.layout.layout_tweet,list_sym);
                    mListView.setAdapter(adapter);
                }
                break;
            case "sym_f":
                if (is_refresh()) {
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_sym_f();
                    call(list_sym_f);
                } else {
                    adapter = new tweet_adapter(context, R.layout.layout_tweet, list_sym_f);
                    mListView.setAdapter(adapter);
                }
                break;
            case "breaking":
                if(is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_rep();
                    call(list_rep);
                }else{
                    breaking_adapter = new breaking_adapter(context, R.layout.layout_breaking,list_rep);
                    mListView.setAdapter(breaking_adapter);
                }
                break;
            case "breaking_f":
                if(is_refresh()){
                    loading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.INVISIBLE);
                    call = get_interface.get_rep_f();
                    call(list_rep_f);
                }else{
                    breaking_adapter = new breaking_adapter(context, R.layout.layout_breaking,list_rep_f);
                    mListView.setAdapter(breaking_adapter);
                }
                break;
        }
        super.onResume();
    }

    @Override
    public void onPause() {

        Log.e("see", "Twitter paused.");

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

}