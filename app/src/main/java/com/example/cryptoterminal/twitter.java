package com.example.cryptoterminal;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cryptoterminal.main.PostAdapter;
import com.example.cryptoterminal.main.get_1;
import com.example.cryptoterminal.main.get_10;
import com.example.cryptoterminal.main.get_100;
import com.example.cryptoterminal.main.get_500;
import com.example.cryptoterminal.main.get_rep;
import com.example.cryptoterminal.main.get_rep_f;
import com.example.cryptoterminal.main.get_sym;
import com.example.cryptoterminal.main.get_sym_f;
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

import static android.content.ContentValues.TAG;

public class twitter extends Fragment {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button[] btn_list = new Button[3];
    Button[] btn_list2 = new Button[4];
    ArrayList<post> postlist;
    ListView mListView;
    PostAdapter adapter;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    HorizontalScrollView hsv;
    HorizontalScrollView hsv2;
    LinearLayout linearLayout;
    boolean rep;
    boolean hidden;
    boolean two_tabs;
    String selected = "";
    private int mInterval = 5000;
    private Handler mHandler;
    boolean running = false;
    get_1 api_interface_1;
    get_100 api_interface_100;
    get_500 api_interface_500;
    get_10 api_interface_10;
    get_rep api_interface_rep;
    get_rep_f api_interface_rep_f;
    get_sym api_interface_sym;
    get_sym_f api_interface_sym_f;
    Call<List<post>> call;
    RelativeLayout loading;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        running = false;
        postlist = new ArrayList<>();
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

        api_interface_1 = retrofit.create(get_1.class);
        api_interface_100  = retrofit.create(get_100.class);
        api_interface_500  = retrofit.create(get_500.class);
        api_interface_10  = retrofit.create(get_10.class);
        api_interface_rep  = retrofit.create(get_rep.class);
        api_interface_rep_f  = retrofit.create(get_rep_f.class);
        api_interface_sym  = retrofit.create(get_sym.class);
        api_interface_sym_f  = retrofit.create(get_sym_f.class);



        Log.e("asd","creaate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_twitter, container, false);

        if(savedInstanceState == null){
            Log.e("asd","viuuuuuu");
        }


        loading = view.findViewById(R.id.loadingPanel);
        hsv =  view.findViewById(R.id.hsv);
        hsv2 =  view.findViewById(R.id.hsv2);
        linearLayout =  view.findViewById(R.id.linearLayout3);
        mListView =  view.findViewById(R.id.listView);
        btn1 = view.findViewById(R.id.button1);
        btn2 = view.findViewById(R.id.button2);
        btn3 = view.findViewById(R.id.button3);
        btn4 = view.findViewById(R.id.button4);
        btn5 = view.findViewById(R.id.button5);
        btn6 = view.findViewById(R.id.button6);
        btn7 = view.findViewById(R.id.button7);
        btn8 = view.findViewById(R.id.button8);
        btn9 = view.findViewById(R.id.button9);
        btn_list[0] = btn1;
        btn_list[1] = btn2;
        btn_list[2] = btn3;
        btn_list2[0] = btn4;
        btn_list2[1] = btn5;
        btn_list2[2] = btn6;
        btn_list2[3] = btn7;

        btn1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                hsv2.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                two_tabs = false;

                reset_colour();
                reset_colour2();
                btn1.setTextColor(Color.parseColor("#0A75FF"));
                btn4.setTextColor(Color.parseColor("#0A75FF"));
                mListView.setVisibility(View.VISIBLE);

                call = api_interface_10.getPosts();
                call();

                selected = "100";

            }
        }); //     CRYPTO

        btn2.setOnClickListener( new View.OnClickListener() {    //   BREAKING

            @Override
            public void onClick(View v) {

                hsv2.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                two_tabs = true;

                reset_colour();
                btn2.setTextColor(Color.parseColor("#0A75FF"));
                btn8.setTextColor(Color.parseColor("#0A75FF"));
                btn9.setTextColor(Color.parseColor("#989898"));
                mListView.setVisibility(View.VISIBLE);
                rep = true;
                call = api_interface_rep.getPosts();
                call();
                selected = "rep";
            }
        });  //    BREAKING

        btn3.setOnClickListener( new View.OnClickListener() {           //   ALTCOINS

            @Override
            public void onClick(View v) {

                hsv2.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

                reset_colour();
                btn3.setTextColor(Color.parseColor("#0A75FF"));
                btn8.setTextColor(Color.parseColor("#0A75FF"));
                btn9.setTextColor(Color.parseColor("#989898"));
                mListView.setVisibility(View.VISIBLE);
                rep = false;
                call = api_interface_sym.getPosts();
                call();
                selected = "sym";
            }
        });

        btn4.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset_colour2();
                btn4.setTextColor(Color.parseColor("#0A75FF"));
                mListView.setVisibility(View.VISIBLE);
                call = api_interface_100.getPosts();
                call();
                selected = "100";
            }
        });

        btn5.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset_colour2();
                btn5.setTextColor(Color.parseColor("#0A75FF"));
                mListView.setVisibility(View.VISIBLE);
                call = api_interface_500.getPosts();
                call();
                selected = "500";
            }
        });

        btn6.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset_colour2();
                btn6.setTextColor(Color.parseColor("#0A75FF"));
                mListView.setVisibility(View.VISIBLE);
                call = api_interface_1.getPosts();
                call();
                selected = "1";
            }
        });

        btn7.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                reset_colour2();
                btn7.setTextColor(Color.parseColor("#0A75FF"));
                mListView.setVisibility(View.VISIBLE);
                call = api_interface_10.getPosts();
                call();
                selected = "10";
            }
        });

        btn8.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {                      //      ALLLL

                btn8.setTextColor(Color.parseColor("#0A75FF"));
                btn9.setTextColor(Color.parseColor("#989898"));
                mListView.setVisibility(View.VISIBLE);
                if(rep) {
                    call = api_interface_rep.getPosts();
                    call();
                    selected = "rep";
                }else{
                    call = api_interface_sym.getPosts();
                    call();
                    selected = "sym";
                }
            }
        });

        btn9.setOnClickListener( new View.OnClickListener() {    //     FILTERED

            @Override
            public void onClick(View v) {

                btn9.setTextColor(Color.parseColor("#0A75FF"));
                btn8.setTextColor(Color.parseColor("#989898"));
                mListView.setVisibility(View.VISIBLE);
                if(rep) {
                    call = api_interface_rep_f.getPosts();
                    call();
                    selected = "rep_f";
                }else{
                    call = api_interface_sym_f.getPosts();
                    call();
                    selected = "sym_f";
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {


                if(hidden){
                    hsv.setVisibility(View.VISIBLE);
                    if(two_tabs){
                        hsv2.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    }else{
                        linearLayout.setVisibility(View.GONE);
                        hsv2.setVisibility(View.VISIBLE);
                    }
                    hidden = false;
                }else{
                    linearLayout.setVisibility(View.GONE);
                    hsv2.setVisibility(View.GONE);
                    hsv.setVisibility(View.GONE);
                    hidden = true;
                }


                return true;
            }
        });


//        btn1.performClick();

        if(!running) {

//            mStatusChecker.run();
        }

        return view;
    }

    public void refresh(boolean on){

       if(on){
           mStatusChecker.run();
       }else{
           mHandler.removeCallbacks(mStatusChecker);
       }
    }

    public void call(){

        if(adapter != null) {
            adapter.clear();
        }
        postlist.clear();
        loading.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<List<post>>() {
            @Override
            public void onResponse(Call<List<post>> call, Response<List<post>> response) {
                loading.setVisibility(View.GONE);
                if (!response.isSuccessful()) {
                    btn1.setText("Code: " + response.code());
                    return;
                }
                try {
                    List<post> posts = response.body();
                    postlist.addAll(posts);
                }catch (Exception e){ }


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

    public void reset_colour() {
        for (int i = 0; i < 3; i++) {
            btn_list[i].setBackgroundColor(Color.parseColor("#3b3b3b"));
           btn_list[i].setTextColor(Color.parseColor("#989898"));
        }
    }

    public void reset_colour2() {
        for (int i = 0; i < 4; i++) {
            btn_list2[i].setBackgroundColor(Color.parseColor("#3b3b3b"));
            btn_list2[i].setTextColor(Color.parseColor("#989898"));
        }
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            Log.v("see", "twitter run");

            switch (selected){
                case "100":
                    call = api_interface_100.getPosts();
                    call();
                    break;
                case "500":
                    call = api_interface_500.getPosts();
                    call();
                    break;
                case "1":
                    call = api_interface_1.getPosts();
                    call();
                    break;
                case "10":
                    call = api_interface_10.getPosts();
                    call();
                    break;
                case "rep":
                    call = api_interface_rep.getPosts();
                    call();
                    break;
                case "sym":
                    call = api_interface_sym.getPosts();
                    call();
                    break;
                case "rep_f":
                    call = api_interface_rep_f.getPosts();
                    call();
                    break;
                case "sym_f":
                    call = api_interface_sym_f.getPosts();
                    call();
                    break;
            }

            mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    @Override
    public void onPause() {


        super.onPause();
    }

    @Override
    public void onDestroy () {

        super.onDestroy ();
    }

    @Override
    public void onStart() {

        super.onStart();
    }


}