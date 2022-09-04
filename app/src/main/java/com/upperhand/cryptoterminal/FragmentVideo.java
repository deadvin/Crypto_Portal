package com.upperhand.cryptoterminal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.adapters.VideoAdapter;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import com.upperhand.cryptoterminal.objects.event;
import com.upperhand.cryptoterminal.objects.video;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentVideo extends Fragment {


    ArrayList<video> videoList;
    RecyclerView recyclerView;
    VideoAdapter adapter;
    Call<List<video>> call;
    RelativeLayout loadingLayout;
    Context context;
    ImageButton btnInfo;
    ImageButton btnAlert;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        videoList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_video, container, false);

        btnInfo = view.findViewById(R.id.button14);
        btnAlert = view.findViewById(R.id.button13);
        loadingLayout = view.findViewById(R.id.loadingPanel);
        recyclerView =  view.findViewById(R.id.listView);

        adapter = new VideoAdapter(context, videoList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        loadList();

        btnInfo.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.buildAlertDialogue(R.layout.dialogue_info, context);
                TextView text = Utils.getAlertDialogue().findViewById(R.id.textView2);
                text.setText(R.string.info_vid);
                Utils.getAlertDialogue().show();
            }
        });

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

                videoList.clear();
                videoList.addAll(videos);
                Collections.reverse(videoList);
                adapter.notifyDataSetChanged();

                saveIntoSp();
            }

            @Override
            public void onFailure(Call<List<video>> call, Throwable t) {
                Utils.makeToast("Loading old data", context);
                loadFromSp();
            }
        });
    }

    public void loadList(){

        if(videoList.isEmpty() || isRefresh()){
            loadingLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            call = RetrofitSingleton.get().getData().get_video();
            call();
        }
    }

    public void loadFromSp(){

        Gson gson = new Gson();
        String json;
        Type type;

        json = Utils.getSharedPref("videos", "", context);
        type = new TypeToken<List<video>>() {}.getType();

        ArrayList<video> list = gson.fromJson(json, type);

        if(list != null && !list.isEmpty()) {
            videoList.clear();
            videoList.addAll(list);
            adapter.notifyDataSetChanged();

            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void saveIntoSp(){
        Gson gson = new Gson();
        String json = gson.toJson(videoList);
        Utils.setSharedPref("videos", json, context);
    }

    public boolean isRefresh(){
        boolean refresh = Utils.getSharedPref("refresh_vid" , false, context);
        Utils.setSharedPref("refresh_vid", false, context);
        return refresh;
    }

    @Override
    public void onResume() {
        loadList();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if(call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

}