package com.upperhand.cryptoterminal;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.SymbolAdapter;
import com.upperhand.cryptoterminal.objects.get_div;
import com.upperhand.cryptoterminal.objects.symbol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    boolean reversed = false;
    get_div api_interface;
    Call<List<symbol>> call;
    RelativeLayout loading;

    Button btn_h10;
    Button btn_h4;
    Button btn_m30;
    Button btn_vol;
    Button btn_sym;

    Button[] btn_list = new Button[5];



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

        btn_h10 = view.findViewById(R.id.btn_10h);
        btn_h4 = view.findViewById(R.id.btn_4h);
        btn_m30 = view.findViewById(R.id.btn_1h);
        btn_vol = view.findViewById(R.id.btn_vol);
        btn_sym = view.findViewById(R.id.btn_sym);

        btn_list[0] = btn_h10;
        btn_list[1] = btn_h4;
        btn_list[2] = btn_m30;
        btn_list[3] = btn_vol;
        btn_list[4] = btn_sym;


        btn_h10.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset_colour();
                btn_h10.setTextColor(Color.parseColor("#0A75FF"));


                symlist.sort(new Comparator<symbol>() {
                    public int compare(symbol o1, symbol o2) {
//                        return o1.getName().compareTo(o2.getName());
                        return Float.compare(o1.get10h(), o2.get10h());

                    }});

                adapter = new SymbolAdapter(getActivity(), R.layout.symbol_layout, symlist);
                mListView.setAdapter(adapter);

            }});

        btn_h4.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                symlist.sort(new Comparator<symbol>() {
                    public int compare(symbol o1, symbol o2) {
//                        return o1.getName().compareTo(o2.getName());
                        return Float.compare(o1.get4h(), o2.get4h());

                    }});

                adapter = new SymbolAdapter(getActivity(), R.layout.symbol_layout, symlist);
                mListView.setAdapter(adapter);
            }});




        return view;
    }



    public void call(){


//        loading.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<symbol>>() {
            @Override
            public void onResponse(Call<List<symbol>> call, Response<List<symbol>> response) {
//                loading.setVisibility(View.GONE);
                if (!response.isSuccessful()) {

                    Log.e("kur","Code: " + response.code());
                    return;
                }
                try {
                    List<symbol> symbols = response.body();
                    if(adapter != null) {
                        adapter.clear();
                    }
                    symlist.clear();
                    symlist.addAll(symbols);

                    symlist.sort(new Comparator<symbol>() {
                        public int compare(symbol o1, symbol o2) {
//                        return o1.getName().compareTo(o2.getName());

                          return Float.compare(o1.get10h(), o2.get10h());
                        }
                    });



                }catch (Exception e){ }


//                Collections.reverse(symlist);
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

    public void reset_colour(int btn) {



        for (int i = 0; i < 5; i++) {
            btn_list[i].setTextColor(Color.parseColor("#FFFFFF"));
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