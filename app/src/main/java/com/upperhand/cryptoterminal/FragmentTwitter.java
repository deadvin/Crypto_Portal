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
    ImageButton btnInfoRep;
    ImageButton btnAlertRep;
    RecyclerView recyclerView;
    TweetsAdapter tweetsAdapter;
    BreakingAdapter breakingAdapter;
    LinearLayout layoutBitcoin;
    LinearLayout layoutAltcoinBreaking;
    String selected = "";
    Call<List<tweet>> call;
    Call<String> callPost;
    RelativeLayout loadingLayout;
    Context context;
    boolean alertBreaking;
    boolean alertAlts;

    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
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

        loadingLayout = view.findViewById(R.id.loadingPanel);
        layoutBitcoin =  view.findViewById(R.id.layoutBitcoin);
        layoutAltcoinBreaking =  view.findViewById(R.id.layoutAltcoinBreaking);
        recyclerView =  view.findViewById(R.id.recyclerView);
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
        btnInfoRep = view.findViewById(R.id.button14);

        breakingAdapter = new BreakingAdapter(context, activeList);
        tweetsAdapter = new TweetsAdapter(context, activeList);
        recyclerView.setAdapter(tweetsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

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
                btnFilter.setText("Filtered");
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
                btnFilter.setText("Crypto");
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

                Utils.buildAlertDialogue(R.layout.alert_all,context);

                TextView al1 = Utils.getAlertDialogue().findViewById(R.id.textView4);
                TextView al2 = Utils.getAlertDialogue().findViewById(R.id.textView5);
                androidx.appcompat.widget.SwitchCompat switchAltsAlerts = Utils.getAlertDialogue().findViewById(R.id.switch1);
                androidx.appcompat.widget.SwitchCompat switchBreakingAlerts = Utils.getAlertDialogue().findViewById(R.id.switch2);

                switchAltsAlerts.setChecked(alertAlts);
                switchBreakingAlerts.setChecked(alertBreaking);
                al1.setText("Altcoins official accounts");
                al2.setText("    Breaking news    ");

                switchAltsAlerts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        alertAlts = isChecked;
                        Utils.setSharedPref("alertAlts", alertAlts, context);
                        Utils.firebaseSubscribe(alertAlts, "alts", context);
                    }
                });

                switchBreakingAlerts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        alertBreaking = isChecked;
                        Utils.setSharedPref("alertBreaking", alertBreaking, context);
                        Utils.firebaseSubscribe(alertBreaking, "breaking", context);
                    }
                });

                Utils.getAlertDialogue().show();
            }
        });

        btnInfoAll.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.buildAlertDialogue(R.layout.dialogue_info,context);
                TextView alertText = Utils.getAlertDialogue().findViewById(R.id.textView2);
                alertText.setText(R.string.info_all);
                Utils.getAlertDialogue().show();
            }
        });

        btnInfoRep.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.buildAlertDialogue(R.layout.dialogue_info,context);
                TextView alertText = Utils.getAlertDialogue().findViewById(R.id.textView2);
                if(selected.equals("breaking") || selected.equals("breaking_f")){
                    alertText.setText(R.string.info_breaking);
                }else{
                    alertText.setText(R.string.info_alts);
                }
                Utils.getAlertDialogue().show();
            }
        });

        btnBitcoin.performClick();

//        ==============   HIDE POSTS

//        recyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
            loadingLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
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
            recyclerView.setAdapter(breakingAdapter);
            breakingAdapter.notifyDataSetChanged();
        }else {
            recyclerView.setAdapter(tweetsAdapter);
            tweetsAdapter.notifyDataSetChanged();
        }
    }

    public void call(){

        call.enqueue(new Callback<List<tweet>>() {
            @Override
            public void onResponse(@NotNull Call<List<tweet>> call, @NotNull Response<List<tweet>> response) {

                loadingLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    Utils.makeToast("Loading old data", context);
                    loadFromSp();
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
                notifyAdapter();

                saveIntoSp();
            }
            @Override
            public void onFailure(Call<List<tweet>> call, Throwable t) {

                Utils.makeToast("Loading old data", context);
                loadFromSp();
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

    public void loadFromSp(){

        Gson gson = new Gson();
        String json = null;
        Type type;
        ArrayList<tweet> list;

        json = Utils.getSharedPref(selected, "", context);
        type = new TypeToken<List<tweet>>(){}.getType();
        list = gson.fromJson(json, type);

        Log.e("asd" , list + " zzzzzzzzzzzzzzzzzzzzzzzz ");

        if(list != null && !list.isEmpty()) {

            curList().clear();
            curList().addAll(list);

            activeList.clear();
            activeList.addAll(curList());
            notifyAdapter();

            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    public void saveIntoSp(){

        Gson gson = new Gson();
        String json = gson.toJson(activeList);
        Utils.setSharedPref(selected, json, context);

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

        boolean refresh = Utils.getSharedPref("refresh_" + selected, false, context);
        Utils.setSharedPref("refresh_" + selected,false, context);

        return refresh;
    }


    @Override
    public void onResume() {

        //   =============   OPEN NOTIFIED TAB

        alertAlts = Utils.getSharedPref("alertAlts",false,context);
        alertBreaking = Utils.getSharedPref("alertBreaking",false,context);
        String topic = Utils.getSharedPref("topic","none",context);

        if(topic.equals("breaking")){
            selected = "braking_f";
            btnBreaking.performClick();
            Utils.setSharedPref("topic","none", context);
        }else if(topic.equals("alts")){
            selected = "sym";
            btnAltcoin.performClick();
            Utils.setSharedPref("topic","none", context);
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