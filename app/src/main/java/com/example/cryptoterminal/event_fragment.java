package com.example.cryptoterminal;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.cryptoterminal.main.EventAdapter;
import com.example.cryptoterminal.main.event;
import com.example.cryptoterminal.main.get_events;
import com.example.cryptoterminal.main.video;
import com.example.cryptoterminal.main.videoAdapter;

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

public class event_fragment extends Fragment {


    ArrayList<event> vidlist;
    ListView mListView;
    EventAdapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    private int mInterval = 5000;
    private Handler mHandler;
    get_events api_interface;
    Call<List<event>> call;
    RelativeLayout loading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vidlist = new ArrayList<>();
        mHandler = new Handler();

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://34.142.9.8:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        api_interface = retrofit.create(get_events.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_events, container, false);

        loading = view.findViewById(R.id.loadingPanel);
        mListView =  view.findViewById(R.id.listView);
        call = api_interface.getevents();
        call();

        return view;
    }



    public void call(){

        if(adapter != null) {
            adapter.clear();
        }
        vidlist.clear();
        loading.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<event>>() {
            @Override
            public void onResponse(Call<List<event>> call, Response<List<event>> response) {
                loading.setVisibility(View.GONE);
                if (!response.isSuccessful()) {

                    return;
                }
                try {
                    List<event> videos = response.body();
                    vidlist.addAll(videos);
                }catch (Exception e){ }

                Collections.reverse(vidlist);
                adapter = new EventAdapter(getActivity(), R.layout.event_layout, vidlist);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<event>> call, Throwable t) {
              Log.e("see",t.getMessage());
            }
        });
    }


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            Log.e("see", "wprk work");
            call = api_interface.getevents();
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
            Log.e("see", "see");
        }else {
            Log.e("see", "dont see");
        }

    }
}