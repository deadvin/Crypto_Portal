package com.upperhand.cryptoterminal;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import androidx.fragment.app.Fragment;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.adapters.breaking_adapter;
import com.upperhand.cryptoterminal.adapters.div_adapter;

import com.upperhand.cryptoterminal.interfaces.get_data;
import com.upperhand.cryptoterminal.objects.div;
import com.upperhand.cryptoterminal.objects.last_price;

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


public  class fragment_prices extends Fragment {

    //region VARS

    ArrayList<div> list_div_1m;
    ArrayList<div> list_div_15m;
    ArrayList<div> list_div_1h;
    ArrayList<div> list_div_1d;
    ArrayList<div> active_list;
    ArrayList<last_price> list_last_prices;
    ListView mListView;
    div_adapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    Retrofit retrofit_last_price;
    get_data api_interface;
    get_data api_interface_last_price;
    Call<List<div>> call;
    Call<List<last_price>> call_last_price;
    RelativeLayout loading;
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String api_url;
    String api_binance_url;
    String api_binance_base_url;
    Spinner time_period;
    LinearLayout price_filter;
    LinearLayout name_filter;
    ImageView price_img;
    ImageView name_img;
    public boolean visible;
    public boolean price_up;
    public boolean name_up;
    public boolean name_selected;
    ArrayAdapter<String> spiner_adapter;
    String selected = "1 MIN";

    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        api_url = getString(R.string.url);
        String s = "[\"WAVESUSDT\",\"TFUELUSDT\",\"ZECUSDT\",\"NEOUSDT\",\"HBARUSDT\",\"LINKUSDT\",\"HOTUSDT\",\"AGLDUSDT\",\"APEUSDT\",\"FLOWUSDT\",\"FTTUSDT\",\"BATUSDT\",\"AUDIOUSDT\",\"ADAUSDT\",\"SOLUSDT\",\"DOGEUSDT\",\"MATICUSDT\",\"LUNAUSDT\",\"DOTUSDT\",\"INJUSDT\",\"AVAXUSDT\",\"EOSUSDT\",\"THETAUSDT\",\"IOTAUSDT\",\"CHZUSDT\",\"ONEUSDT\",\"KSMUSDT\",\"NEARUSDT\",\"XTZUSDT\",\"ALGOUSDT\",\"EGLDUSDT\",\"ARUSDT\",\"AXSUSDT\",\"XRPUSDT\",\"FILUSDT\",\"VETUSDT\",\"UNIUSDT\",\"CAKEUSDT\",\"ICPUSDT\",\"ETCUSDT\",\"ZILUSDT\",\"XLMUSDT\",\"XMRUSDT\",\"SANDUSDT\",\"RVNUSDT\",\"MANAUSDT\",\"LTCUSDT\",\"GALAUSDT\",\"BNBUSDT\",\"AAVEUSDT\",\"ANKRUSDT\",\"1INCHUSDT\",\"COTIUSDT\",\"ENJUSDT\",\"ATOMUSDT\",\"ETHUSDT\",\"FTMUSDT\"]";
        api_binance_url = "https://api.binance.com/api/v3/ticker/price?symbols=" + s;
        api_binance_base_url = "https://api.binance.com/api/v3/ticker/" ;
        list_div_1m = new ArrayList<>();
        list_div_15m = new ArrayList<>();
        list_div_1h = new ArrayList<>();
        list_div_1d = new ArrayList<>();
        active_list = new ArrayList<>();
        list_last_prices = new ArrayList<>();
        price_up = true;
        name_selected = false;
        adapter = new div_adapter(context, R.layout.layout_div,active_list, list_last_prices);

//==========================   RETROFIT INTERFACES

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

        retrofit_last_price = new Retrofit.Builder()
                .baseUrl(api_binance_base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        api_interface  = retrofit.create(get_data.class);
        api_interface_last_price  = retrofit_last_price.create(get_data.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_div, container, false);

        loading = view.findViewById(R.id.loadingPanel_altcoins);
        mListView =  view.findViewById(R.id.listView);
        time_period = view.findViewById(R.id.spinner_price);
        name_filter = view.findViewById(R.id.ll_name);
        price_filter = view.findViewById(R.id.price);
        price_img = view.findViewById(R.id.filter_price);
        name_img = view.findViewById(R.id.filter_name);
        mListView.setAdapter(adapter);

        name_filter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                price_img.setImageResource(R.drawable.sort_off);
                name_up =! name_up;
                if(!name_selected){
                    name_up = true;
                }
                name_selected = true;
                if(name_up){
                    name_img.setImageResource(R.drawable.sort_up);
                }else{
                    name_img.setImageResource(R.drawable.sort_down);
                }
                sort();
                adapter.setMode(selected);
                adapter.isAnimate = false;
                adapter.refresh_saved_x();
                adapter.notifyDataSetChanged();
                mListView.setSelection(0);
            }
        });

        price_filter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_img.setImageResource(R.drawable.sort_off);
                price_up =! price_up;
                if(name_selected){
                    price_up = true;
                }
                name_selected = false;
                if(price_up){
                    price_img.setImageResource(R.drawable.sort_up);
                }else{
                    price_img.setImageResource(R.drawable.sort_down);
                }
                sort();
                adapter.setMode(selected);
                adapter.isAnimate = false;
                adapter.refresh_saved_x();
                adapter.notifyDataSetChanged();
                mListView.setSelection(0);
            }
        });

//==========================   SPINNER

        String[] items = new String[]{"1 MIN","15 MIN","1 HOUR","1 DAY"};

        spiner_adapter = new ArrayAdapter<String>(getContext(),  R.layout.spin_item_dark_main, items);
        spiner_adapter.setDropDownViewResource(R.layout.spin_item_dark);
        time_period.setAdapter(spiner_adapter);
        time_period.setSelection(0, true);

        time_period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selected = String.valueOf(time_period.getAdapter().getItem(position));

                mListView.setAdapter(adapter);
                adapter.setMode(selected);
                adapter.isAnimate = true;
                adapter.refresh_saved_x();
                call(false);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

//==========================   LOAD ADAPTER DATA

        call_price(false);
        adapter.setMode(selected);
        call(false);


        return view;
    }

    public void call_price(boolean notify){

        call_last_price = api_interface_last_price.get_last_price(api_binance_url);

        call_last_price.enqueue(new Callback<List<last_price>>() {
            @Override
            public void onResponse(Call<List<last_price>> call, Response<List<last_price>> response) {
                if (!response.isSuccessful()) {
                    Log.e("asd", "error call_Last_price" );
                    return;
                }

                List<last_price> divs = response.body();
                if(divs != null) {
                    list_last_prices.clear();
                    list_last_prices.addAll(divs);
                    if(notify) {
                        adapter.price_update = true;
                        adapter.isAnimate = false;
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<List<last_price>> call, Throwable t) {
            }
        });
    }

    public void call(final boolean is_refresh){

        switch (selected) {
            case "1 MIN":
                if(!list_div_1m.isEmpty() && !is_refresh){
                    active_list.clear();
                    active_list.addAll(list_div_1m);
                    sort();
                    mListView.setSelection(0);
                    adapter.notifyDataSetChanged();
                    return;
                }
                call = api_interface.get_div_1m();
                break;
            case "15 MIN":
                if(!list_div_15m.isEmpty() && !is_refresh){
                    active_list.clear();
                    active_list.addAll(list_div_15m);
                    sort();
                    mListView.setSelection(0);
                    adapter.notifyDataSetChanged();
                    return;
                }
                call = api_interface.get_div_15m();
                break;
            case "1 HOUR":
                if(!list_div_1h.isEmpty() && !is_refresh){
                    active_list.clear();
                    active_list.addAll(list_div_1h);
                    sort();
                    mListView.setSelection(0);
                    adapter.notifyDataSetChanged();
                    return;
                }
                call = api_interface.get_div_1h();
                break;
            case "1 DAY":
                if(!list_div_1d.isEmpty() && !is_refresh){
                    active_list.clear();
                    active_list.addAll(list_div_1d);
                    sort();
                    mListView.setSelection(0);
                    adapter.notifyDataSetChanged();
                    return;
                }
                call = api_interface.get_div_1d();
                break;
        }

        if(!is_refresh) {
            loading.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }

        call.enqueue(new Callback<List<div>>() {
            @Override
            public void onResponse(Call<List<div>> call, Response<List<div>> response) {

                if (!response.isSuccessful()) {
                    return;
//                    List<div> divs = load_from_sp();
                   }

                List<div> divs = response.body();
                if(divs == null || divs.isEmpty()){
                    return;
                }
                active_list.addAll(divs);

                switch (selected) {
                    case "1 MIN":
                        list_div_1m.clear();
                        list_div_1m.addAll(divs);
                        active_list.clear();
                        active_list.addAll(list_div_1m);
                        break;
                    case "15 MIN":
                        list_div_15m.clear();
                        list_div_15m.addAll(divs);
                        active_list.clear();
                        active_list.addAll(list_div_15m);
                        break;
                    case "1 HOUR":
                        list_div_1h.clear();
                        list_div_1h.addAll(divs);
                        active_list.clear();
                        active_list.addAll(list_div_1h);
                        break;
                    case "1 DAY":
                        list_div_1d.clear();
                        list_div_1d.addAll(divs);
                        active_list.clear();
                        active_list.addAll(list_div_1d);
                        break;
                }

                sort();
                if(!is_refresh) {
                    mListView.setSelection(0);
                }
                adapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);

//                save_sp();
            }
            @Override
            public void onFailure(Call<List<div>> call, Throwable t) {
//                load_from_sp();
            }
        });


    }

//    public ArrayList<div> load_from_sp(){
//
//        Gson gson = new Gson();
//        String json = null;
//        Type type;
//        ArrayList<div> list = null;
//
//        preferences = context.getSharedPreferences("div" + selected, Context.MODE_PRIVATE);
//        json = preferences.getString("div" + selected, "");
//
//
//        type = new TypeToken<List<div>>(){}.getType();
//        list = gson.fromJson(json, type);
//
//        return list;
//    }
//
//    public void save_sp(){
//
//        Gson gson = new Gson();
//        String json = gson.toJson(active_list);
//
//        editor = context.getSharedPreferences("div" + selected, MODE_PRIVATE).edit();
//        editor.putString("div", json);
//        editor.apply();
//    }


    public  void refresh(boolean big){

        if(!big){
            Log.e("asd", " refresh  prices small");
            call_price(true);
        }else {
            adapter.setMode(selected);
            adapter.isAnimate = false;
            call(true);
            Log.e("asd", " refresh  prices big");
        }


    }


    public void sort(){

        if(name_selected){
            if(name_up){
                active_list.sort(new Comparator<div>() {
                    public int compare(div o1, div o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
            }else{
                active_list.sort(new Comparator<div>() {
                    public int compare(div o1, div o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                Collections.reverse(active_list);
            }
        }else{
            if(price_up){
                active_list.sort(new Comparator<div>() {
                    public int compare(div o1, div o2) {
                        return Float.compare(o1.get_av_div(), o2.get_av_div());
                    }
                });
                Collections.reverse(active_list);
            }else{
                active_list.sort(new Comparator<div>() {
                    public int compare(div o1, div o2) {
                        return Float.compare(o1.get_av_div(), o2.get_av_div());
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {

        loading.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);

        adapter.setMode(selected);
        adapter.isAnimate = false;
        call(true);

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(call != null) {
            call.cancel();
        }
        if(call_last_price != null) {
            call_last_price.cancel();
        }
        super.onDestroy();
    }

}

