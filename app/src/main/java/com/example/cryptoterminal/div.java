package com.example.cryptoterminal;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.cryptoterminal.R;
import com.example.cryptoterminal.main.SymbolAdapter;
import com.example.cryptoterminal.main.get_div;
import com.example.cryptoterminal.main.symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class div extends Fragment {

    Button btn1;
    ArrayList<symbol> symlist;
    ListView mListView;
    SymbolAdapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    private int mInterval = 5000;
    private Handler mHandler;
    boolean running = false;
    get_div api_interface;
    Call<List<symbol>> call;
    RelativeLayout loading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        symlist = new ArrayList<>();
        mHandler = new Handler();

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                //.baseUrl("http://daniel.onecreative.eu:5000/")
                .baseUrl("http://34.142.9.8:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        api_interface = retrofit.create(get_div.class);

        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_div, container, false);

        loading = view.findViewById(R.id.loadingPanel);
        mListView =  view.findViewById(R.id.listView);
        call = api_interface.getsymbols();
        call();

        return view;
    }



    public void call(){


//        loading.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<symbol>>() {
            @Override
            public void onResponse(Call<List<symbol>> call, Response<List<symbol>> response) {
//                loading.setVisibility(View.GONE);
                if (!response.isSuccessful()) {
                    btn1.setText("Code: " + response.code());
                    return;
                }
                try {
                    List<symbol> symbols = response.body();

                    if(adapter != null) {
                        adapter.clear();
                    }
                    symlist.clear();


                    symlist.addAll(symbols);
                }catch (Exception e){ }


                Collections.reverse(symlist);
                adapter = new SymbolAdapter(getActivity(), R.layout.symbol_layout, symlist);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<symbol>> call, Throwable t) {
              Log.e("see",t.getMessage());
            }
        });
    }


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            Log.e("see", "dif refreshed");
            call = api_interface.getsymbols();
            call();
            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    public void refresh(boolean on){

        if(on){
            mStatusChecker.run();
        }else{
            mHandler.removeCallbacks(mStatusChecker);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mStatusChecker.run();

        }else {
            if (mHandler != null) {
                mHandler.removeCallbacks(mStatusChecker);
            }
        }
    }
}