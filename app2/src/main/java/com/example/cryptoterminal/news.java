package com.upperhand.cryptoterminal;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.get_news;
import com.upperhand.cryptoterminal.objects.video;
import com.upperhand.cryptoterminal.objects.videoAdapter;

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

public class news extends Fragment {


    ArrayList<video> symlist;
    ListView mListView;
    videoAdapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    private int mInterval = 5000;
    private Handler mHandler;
    get_news api_interface;
    Call<List<video>> call;
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
                .baseUrl("http://34.142.9.8:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        api_interface = retrofit.create(get_news.class);

        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_video, container, false);

        loading = view.findViewById(R.id.loadingPanel);
        mListView =  view.findViewById(R.id.listView);
        call = api_interface.getvideos();
        call();

        return view;
    }



    public void call(){

        if(adapter != null) {
            adapter.clear();
        }
        symlist.clear();
        loading.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<video>>() {
            @Override
            public void onResponse(Call<List<video>> call, Response<List<video>> response) {
                loading.setVisibility(View.GONE);
                if (!response.isSuccessful()) {

                    return;
                }
                try {
                    List<video> videos = response.body();
                    symlist.addAll(videos);
                }catch (Exception e){ }

                Collections.reverse(symlist);
                adapter = new videoAdapter(getActivity(), R.layout.video_layout, symlist);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<video>> call, Throwable t) {
              Log.e("see",t.getMessage());
            }
        });
    }


    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            Log.e("see", "wprk work");
            call = api_interface.getvideos();
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