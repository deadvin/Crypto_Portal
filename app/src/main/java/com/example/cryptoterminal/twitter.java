package com.example.cryptoterminal;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.cryptoterminal.main.PostAdapter;
import com.example.cryptoterminal.main.get_700;
import com.example.cryptoterminal.main.post;

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

public class twitter extends Fragment {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    Button[] btn_list = new Button[6];

    ArrayList<post> postlist;
    ListView mListView;
    PostAdapter adapter;
    Typeface face ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postlist = new ArrayList<>();






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_twitter, container, false);

        btn1 = view.findViewById(R.id.button1);
        btn2 = view.findViewById(R.id.button2);
        btn3 = view.findViewById(R.id.button3);
        btn4 = view.findViewById(R.id.button4);


        mListView =  view.findViewById(R.id.listView);
        btn_list[0] = btn1;
        btn_list[1] = btn2;
        btn_list[2] = btn3;
        btn_list[3] = btn4;



        btn1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btn1.setBackgroundColor(Color.parseColor("#878787"));
                mListView.setVisibility(View.VISIBLE);
                twitter_700();
            }
        });



        return view;
    }


    public void twitter_700(){

        postlist.clear();
        if(adapter != null) {
            adapter.clear();
        }


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://daniel.onecreative.eu:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        get_700 dede = retrofit.create(get_700.class);
        Call<List<post>> call = dede.getPosts();
        call.enqueue(new Callback<List<post>>() {
            @Override
            public void onResponse(Call<List<post>> call, Response<List<post>> response) {
                if (!response.isSuccessful()) {
                    btn1.setText("Code: " + response.code());
                    return;
                }

                try {
                    List<post> posts = response.body();
                    postlist.addAll(posts);
                }catch (Exception e){

                }

                Collections.reverse(postlist);
                adapter = new PostAdapter(getActivity(), R.layout.adapter_view_layout, postlist);
                mListView.setAdapter(adapter);


            }
            @Override
            public void onFailure(Call<List<post>> call, Throwable t) {
                btn1.setText(t.getMessage());
            }
        });

    }
}