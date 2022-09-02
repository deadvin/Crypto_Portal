package com.upperhand.cryptoterminal;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.adapters.BreakingAdapter;
import com.upperhand.cryptoterminal.adapters.TweetsAdapter;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.upperhand.cryptoterminal.objects.tweet;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.content.Context.MODE_PRIVATE;

public class FragmentTwitter extends Fragment {

    //region VARS
    Button btnBitcoin;
    Button btnAltcoin;
    Button btn100;
    Button btn500;
    Button btn2m;
    Button btn10m;
    Button btnAll;
    Button btnFilter;
    Button btnBreaking;
    ArrayList<tweet> listAll_1;
    ArrayList<tweet> listAll_100;
    ArrayList<tweet> listAll_10;
    ArrayList<tweet> listAll_500;
    ArrayList<tweet> listSym;
    ArrayList<tweet> listSym_f;
    ArrayList<tweet> listRep_f;
    ArrayList<tweet> listRep;
    ArrayList<tweet> activeList;
    ImageButton btnInfoAll;
    ImageButton btnAlertAll;
    ImageButton btninfoRep;
    ImageButton btnAlertRep;
    RecyclerView mRecyclerView;
    TweetsAdapter tweetsAdapter;
    BreakingAdapter breakingAdapter;
    LinearLayout layoutBitcoin;
    LinearLayout layoutAltcoinBreaking;
    String selected = "";
    Call<List<tweet>> call;
    Call<String> callPost;
    RelativeLayout loading;
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    boolean alertBreaking;
    boolean alertAlts;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    AdView adView;


    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();


        listAll_1 = new ArrayList<>();
        listAll_10 = new ArrayList<>();
        listAll_100 = new ArrayList<>();
        listAll_500 = new ArrayList<>();
        listSym = new ArrayList<>();
        listSym_f = new ArrayList<>();
        listRep = new ArrayList<>();
        listRep_f = new ArrayList<>();
        activeList = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_twitter, container, false);

        //   ===================   ADVIEW   ===================

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
        layoutBitcoin =  view.findViewById(R.id.layoutBitcoin);
        layoutAltcoinBreaking =  view.findViewById(R.id.layoutAltcoinBreaking);
        mRecyclerView =  view.findViewById(R.id.recyclerView);
        btnBitcoin = view.findViewById(R.id.btnBitcoin);
        btnAltcoin = view.findViewById(R.id.btnAltcoin);
        btnBreaking = view.findViewById(R.id.btnBreaking);
        btn100 = view.findViewById(R.id.btn100);
        btn500 = view.findViewById(R.id.btn500);
        btn2m = view.findViewById(R.id.btn2m);
        btn10m = view.findViewById(R.id.btn10m);
        btnAll = view.findViewById(R.id.btnAll);
        btnFilter = view.findViewById(R.id.btnFilter);

        btnInfoAll = view.findViewById(R.id.button12);
        btnAlertAll = view.findViewById(R.id.button10);
        btnAlertRep = view.findViewById(R.id.button13);
        btninfoRep = view.findViewById(R.id.button14);

        breakingAdapter = new BreakingAdapter(context, activeList);
        tweetsAdapter = new TweetsAdapter(context, activeList);
        mRecyclerView.setAdapter(tweetsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        btnBitcoin.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                layoutBitcoin.setVisibility(View.VISIBLE);
                layoutAltcoinBreaking.setVisibility(View.INVISIBLE);
                selected = "1";
                setButtonColours();
                loadList();
            }
        });

        btnAltcoin.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                layoutBitcoin.setVisibility(View.INVISIBLE);
                layoutAltcoinBreaking.setVisibility(View.VISIBLE);
                selected = "sym";
                setButtonColours();
                loadList();
            }
        });

        btnBreaking.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                layoutBitcoin.setVisibility(View.INVISIBLE);
                layoutAltcoinBreaking.setVisibility(View.VISIBLE);
                selected = "breaking_f";
                setButtonColours();
                loadList();
            }
        });

        btn100.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selected = "100";
                setButtonColours();
                loadList();
            }
        });

        btn500.setOnClickListener( new View.OnClickListener() {    // 500

            @Override
            public void onClick(View v) {
                selected = "500";
                setButtonColours();
                loadList();
            }
        });

        btn2m.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selected = "1";
                setButtonColours();
                loadList();
            }
        });

        btn10m.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selected = "10";
                setButtonColours();
                loadList();
            }
        });

        btnAll.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (selected.equals("sym") || selected.equals("sym_f")){
                    selected = "sym";
                }else{
                    selected = "breaking";
                }
                loadList();
                setButtonColours();
            }
        });

        btnFilter.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (selected.equals("sym") || selected.equals("sym_f")) {
                    selected = "sym_f";
                }else{
                    selected = "breaking_f";
                }
                loadList();
                setButtonColours();
            }
        });

        btnAlertRep.setOnClickListener( new View.OnClickListener() {

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

                switch_1.setChecked(alertAlts);
                switch_2.setChecked(alertBreaking);
                al1.setText("Altcoin official accounts");
                al2.setText("    Breaking news    ");


                switch_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            alertAlts = true;
                            editor = context.getSharedPreferences("alertAlts", MODE_PRIVATE).edit();
                            editor.putBoolean("alertAlts", true);
                            editor.apply();
                            manage(true, "alts");
                        } else {
                            alertAlts = false;
                            editor = context.getSharedPreferences("alertAlts", MODE_PRIVATE).edit();
                            editor.putBoolean("alertAlts", false);
                            editor.apply();
                            manage(false, "alts");
                        }
                    }
                });

                switch_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            alertBreaking = true;
                            editor = context.getSharedPreferences("alertBreaking", MODE_PRIVATE).edit();
                            editor.putBoolean("alertBreaking", true);
                            editor.apply();
                            manage(true, "alts");
                        } else {
                            alertAlts = false;
                            editor = context.getSharedPreferences("alertBreaking", MODE_PRIVATE).edit();
                            editor.putBoolean("alertBreaking", false);
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

        btnInfoAll.setOnClickListener( new View.OnClickListener() {    //     FILTERED

            @Override
            public void onClick(View v) {

                final Dialog customDialog = new Dialog(context);
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.setContentView(R.layout.dialogue_info);
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

        btninfoRep.setOnClickListener( new View.OnClickListener() {    //     FILTERED

            @Override
            public void onClick(View v) {

                final Dialog customDialog = new Dialog(context);
                customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customDialog.setContentView(R.layout.dialogue_info);
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

        btnBitcoin.performClick();

//        ==============   HIDE POSTS

//        mRecyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

    public void loadList(){

        if(!curList().isEmpty() && !isRefresh()){
            activeList.clear();
            activeList.addAll(curList());
            notifyAdapter();
        }else{
            loading.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            buildCall();
            call();
        }
    }

    public void buildCall(){

        switch (selected) {
            case "100":
                call = RetrofitSingleton.get().getData().get_100();
                break;
            case "500":
                call = RetrofitSingleton.get().getData().get_500();
                break;
            case "1":
                call = RetrofitSingleton.get().getData().get_1();
                break;
            case "10":
                call = RetrofitSingleton.get().getData().get_10();
                break;
            case "sym":
                call = RetrofitSingleton.get().getData().get_sym();
                break;
            case "sym_f":
                call = RetrofitSingleton.get().getData().get_sym_f();
                break;
            case "breaking":
                call = RetrofitSingleton.get().getData().get_rep();
                break;
            case "breaking_f":
                call = RetrofitSingleton.get().getData().get_rep_f();
                break;
        }
    }

    public void notifyAdapter(){

        if(selected.equals("breaking") || selected.equals("breaking_f")){
            mRecyclerView.setAdapter(breakingAdapter);
            breakingAdapter.notifyDataSetChanged();
        }else {
            mRecyclerView.setAdapter(tweetsAdapter);
            tweetsAdapter.notifyDataSetChanged();
        }
    }

    public void call(){

        call.enqueue(new Callback<List<tweet>>() {
            @Override
            public void onResponse(@NotNull Call<List<tweet>> call, @NotNull Response<List<tweet>> response) {

                loading.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    Utils.makeToast(context,"Error Code" + response.code());
//                    load_from_sp();
                    return;
                }

                List<tweet> tweets = response.body();
                if(tweets == null || tweets.isEmpty()){
                    return;
                }

                curList().clear();
                curList().addAll(tweets);
                Collections.reverse(curList());

                activeList.clear();
                activeList.addAll(curList());
//                save_sp(activeList);

                notifyAdapter();

            }
            @Override
            public void onFailure(Call<List<tweet>> call, Throwable t) {
//                if(listAll_1.isEmpty() || isRefresh()) {
//                    if (mToast != null) {
//                        mToast.cancel();
//                    }
//                    mToast = Toast.makeText(context, "Problem connecting the server. Loading old data.",
//                            Toast.LENGTH_LONG);
//                    mToast.show();
//                }
//                load_from_sp();
            }
        });
    }

    public ArrayList<tweet> curList(){

        switch (selected) {
            case "100":
                return listAll_100;
            case "500":
                return listAll_500;
            case "1":
                return listAll_1;
            case "10":
                return listAll_10;
            case "sym":
                return listSym;
            case "sym_f":
                return listSym_f;
            case "breaking":
                return listRep;
            case "breaking_f":
                return listRep_f;
        }
        return null;
    }

    public void load_from_sp(){

        Gson gson = new Gson();
        String json = null;
        Type type;
        ArrayList<tweet> list;

        switch (selected) {
            case "100":
                if(!listAll_100.isEmpty()){ return; }
                preferences = context.getSharedPreferences("100", Context.MODE_PRIVATE);
                json = preferences.getString("100", "");
                break;
            case "500":
                if(!listAll_500.isEmpty()){ return; }
                preferences = context.getSharedPreferences("500", Context.MODE_PRIVATE);
                json = preferences.getString("500", "");
                break;
            case "1":
                if(!listAll_1.isEmpty()){ return; }
                preferences = context.getSharedPreferences("1", Context.MODE_PRIVATE);
                json = preferences.getString("1", "");
                break;
            case "10":
                if(!listAll_10.isEmpty()){ return; }
                preferences = context.getSharedPreferences("10", Context.MODE_PRIVATE);
                json = preferences.getString("10", "");
                break;
            case "sym":
                if(!listSym.isEmpty()){ return; }
                preferences = context.getSharedPreferences("sym", Context.MODE_PRIVATE);
                json = preferences.getString("sym", "");
                break;
            case "sym_f":
                if(!listSym_f.isEmpty()){ return; }
                preferences = context.getSharedPreferences("sym_f", Context.MODE_PRIVATE);
                json = preferences.getString("sym_f", "");
                break;
            case "breaking":
                if(!listRep.isEmpty()){ return; }
                preferences = context.getSharedPreferences("breaking", Context.MODE_PRIVATE);
                json = preferences.getString("breaking", "");
                break;
            case "breaking_f":
                if(!listRep_f.isEmpty()){ return; }
                preferences = context.getSharedPreferences("breaking_f", Context.MODE_PRIVATE);
                json = preferences.getString("breaking_f", "");
                break;
        }


        type = new TypeToken<List<tweet>>(){}.getType();
        list = gson.fromJson(json, type);
        if(list != null && !list.isEmpty()) {

            if(selected.equals("breaking") || selected.equals("breaking_f")){

                mRecyclerView.setAdapter(tweetsAdapter);
                breakingAdapter.notifyDataSetChanged();
            }else {

                mRecyclerView.setAdapter(tweetsAdapter);
                tweetsAdapter.notifyDataSetChanged();
            }
            loading.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);


            switch (selected) {
                case "100":
                    listAll_100 = list;
                    break;
                case "500":
                    listAll_500 = list;
                    break;
                case "1":
                    listAll_1 = list;
                    break;
                case "10":
                    listAll_10 = list;
                    break;
                case "sym":
                    listSym = list;
                    break;
                case "sym_f":
                    listSym_f = list;
                    break;
                case "breaking":
                    listRep = list;
                    break;
                case "breaking_f":
                    listRep_f = list;
                    break;
            }

        }
    }

    public void save_sp(ArrayList<tweet> list){

        Gson gson = new Gson();
        String json = gson.toJson(list);

        switch (selected) {
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

        callPost.enqueue(new Callback<String>() {
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

    public void setButtonColours() {

        btnBitcoin.setTextColor(Color.parseColor("#FFFFFF"));
        btnAltcoin.setTextColor(Color.parseColor("#FFFFFF"));
        btn100.setTextColor(Color.parseColor("#FFFFFF"));
        btn500.setTextColor(Color.parseColor("#FFFFFF"));
        btn2m.setTextColor(Color.parseColor("#FFFFFF"));
        btn10m.setTextColor(Color.parseColor("#FFFFFF"));
        btnAll.setTextColor(Color.parseColor("#FFFFFF"));
        btnFilter.setTextColor(Color.parseColor("#FFFFFF"));
        btnBreaking.setTextColor(Color.parseColor("#FFFFFF"));

        switch (selected) {
            case "100":
                btnBitcoin.setTextColor(Color.parseColor("#0A75FF"));
                btn100.setTextColor(Color.parseColor("#0A75FF"));
                break;
            case "500":
                btnBitcoin.setTextColor(Color.parseColor("#0A75FF"));
                btn500.setTextColor(Color.parseColor("#0A75FF"));
                break;
            case "1":
                btnBitcoin.setTextColor(Color.parseColor("#0A75FF"));
                btn2m.setTextColor(Color.parseColor("#0A75FF"));
                break;
            case "10":
                btnBitcoin.setTextColor(Color.parseColor("#0A75FF"));
                btn10m.setTextColor(Color.parseColor("#0A75FF"));
                break;
            case "sym":
                btnAltcoin.setTextColor(Color.parseColor("#0A75FF"));
                btnAll.setTextColor(Color.parseColor("#0A75FF"));
                break;
            case "sym_f":
                btnAltcoin.setTextColor(Color.parseColor("#0A75FF"));
                btnFilter.setTextColor(Color.parseColor("#0A75FF"));
                break;
            case "breaking":
                btnBreaking.setTextColor(Color.parseColor("#0A75FF"));
                btnAll.setTextColor(Color.parseColor("#0A75FF"));
                break;
            case "breaking_f":
                btnBreaking.setTextColor(Color.parseColor("#0A75FF"));
                btnFilter.setTextColor(Color.parseColor("#0A75FF"));
                break;
        }
    }

    public boolean isRefresh(){

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
                    Utils.makeToast(context, "Alert On");
                }});
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic(tag).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Utils.makeToast(context, "Alert Off");
                }});
        }
    }

    @Override
    public void onResume() {

        //   =============   OPEN NOTIFIED TAB

        context = this.getActivity();

        preferences = context.getSharedPreferences("alertAlts", Context.MODE_PRIVATE);
        alertAlts = preferences.getBoolean("alertAlts", false);

        preferences = context.getSharedPreferences("alertBreaking", Context.MODE_PRIVATE);
        alertBreaking = preferences.getBoolean("alertBreaking", false);

        preferences = context.getSharedPreferences("topic", Context.MODE_PRIVATE);
        String topic = preferences.getString("topic", "none");

        if(topic.equals("breaking")){
            selected = "braking_f";
            btnBreaking.performClick();
            editor = context.getSharedPreferences("topic", MODE_PRIVATE).edit();
            editor.putString("topic", "none");
            editor.apply();
        }else if(topic.equals("alts")){
            selected = "sym";
            btnAltcoin.performClick();
            editor = context.getSharedPreferences("topic", MODE_PRIVATE).edit();
            editor.putString("topic", "none");
            editor.apply();
        }

        loadList();
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
        super.onDestroy();
    }

}