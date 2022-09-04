package com.upperhand.cryptoterminal;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.adapters.BreakingAdapter;
import com.upperhand.cryptoterminal.adapters.TweetsAdapter;
import com.upperhand.cryptoterminal.adapters.VideoAdapter;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import com.upperhand.cryptoterminal.objects.event;
import com.upperhand.cryptoterminal.objects.tweet;
import com.upperhand.cryptoterminal.objects.video;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FragmentNews extends Fragment {

    ArrayList<video> listAll;
    ArrayList<video> listTop;
    ArrayList<video> activeList;
    VideoAdapter adapter;
    RecyclerView recyclerView;
    Call<List<video>> call;
    RelativeLayout loadingLayout;
    Button btnAll;
    Button btnTop;
    Context context;
    ImageButton btnInfo;
    ImageButton btnAlert;
    String selected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listAll = new ArrayList<>();
        listTop = new ArrayList<>();
        context = this.getActivity();
        activeList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_news, container, false);

        btnAll = view.findViewById(R.id.button1);
        btnTop = view.findViewById(R.id.button2);
        btnInfo = view.findViewById(R.id.button4);
        btnAlert = view.findViewById(R.id.button3);
        loadingLayout = view.findViewById(R.id.loadingPanel);
        recyclerView =  view.findViewById(R.id.listView);

        adapter = new VideoAdapter(context, activeList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        btnAll.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnAll.setTextColor(Color.parseColor("#0A75FF"));
                btnTop.setTextColor(Color.parseColor("#FFFFFF"));
                selected ="news";
                loadList();
            }
        });

        btnTop.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnAll.setTextColor(Color.parseColor("#FFFFFF"));
                btnTop.setTextColor(Color.parseColor("#0A75FF"));
                selected ="news_top";
                loadList();
            }
        });

        btnInfo.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.buildAlertDialogue(R.layout.dialogue_info, context);
                TextView text = Utils.getAlertDialogue().findViewById(R.id.textView2);
                text.setText(R.string.info_news);
                Utils.getAlertDialogue().show();
            }
        });

        btnTop.performClick();

        return view;
    }

    public void call(){

        call.enqueue(new Callback<List<video>>() {
            @Override
            public void onResponse(Call<List<video>> call, Response<List<video>> response) {

                loadingLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    Utils.makeToast("Loading old data", context);
                    loadFromSp();
                    return;
                }

                List<video> videos = response.body();
                if(videos == null || videos.isEmpty()){
                    return;
                }

                curList().clear();
                curList().addAll(videos);
                Collections.reverse(curList());

                activeList.clear();
                activeList.addAll(curList());
                adapter.notifyDataSetChanged();

                saveIntoSp();
            }
            @Override
            public void onFailure(Call<List<video>> call, Throwable t) {
                loadFromSp();
            }
        });
    }

    public void loadList(){

        if(!curList().isEmpty() && !isRefresh()){
            activeList.clear();
            activeList.addAll(curList());
            adapter.notifyDataSetChanged();
        }else{
            loadingLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            buildCall();
            call();
        }
    }

    public void buildCall(){

        switch (selected) {
            case "news":
                call = RetrofitSingleton.get().getData().get_news();
                break;
            case "news_top":
                call = RetrofitSingleton.get().getData().get_news_f();
                break;
        }
    }

    public void loadFromSp(){

        Gson gson = new Gson();
        String json;
        Type type;

        json = Utils.getSharedPref(selected, "", context);
        type = new TypeToken<List<video>>() {}.getType();

        ArrayList<video> list = gson.fromJson(json, type);

        if(list != null && !list.isEmpty()) {
            curList().clear();
            curList().addAll(list);
            adapter.notifyDataSetChanged();
            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void saveIntoSp(){
        Gson gson = new Gson();
        String json = gson.toJson(curList());
        Utils.setSharedPref(selected, json, context);
    }

    public ArrayList<video> curList(){

        switch (selected) {
            case "news":
                return listAll;
            case "news_top":
                return listTop;
        }
        return null;
    }

    public boolean isRefresh(){
        boolean refresh = Utils.getSharedPref("refresh_" + selected, false, context);
        Utils.setSharedPref("refresh_" + selected, false, context);
        return refresh;
    }

    @Override
    public void onDestroy() {
        if(call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        context = this.getActivity();
        super.onResume();
    }
}