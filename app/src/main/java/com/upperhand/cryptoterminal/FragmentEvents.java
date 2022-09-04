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
import com.upperhand.cryptoterminal.adapters.EventAdapter;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import com.upperhand.cryptoterminal.objects.event;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentEvents extends Fragment {

    ArrayList<event> eventsList;
    EventAdapter adapter;
    Call<List<event>> call;
    Context context;
    ImageButton btnInfo;
    ImageButton btnAlert;
    RecyclerView recyclerView;
    RelativeLayout loadingLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventsList = new ArrayList<>();
        context = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_events, container, false);

        btnInfo = view.findViewById(R.id.button14);
        btnAlert = view.findViewById(R.id.button13);
        loadingLayout = view.findViewById(R.id.loadingPanel);
        recyclerView =  view.findViewById(R.id.listView);

        adapter = new EventAdapter(context, eventsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        loadList();

        btnInfo.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.buildAlertDialogue(R.layout.dialogue_info, context);
                TextView text = Utils.getAlertDialogue().findViewById(R.id.textView2);
                text.setText(R.string.info_events);
                Utils.getAlertDialogue().show();
            }
        });

        return view;
    }


    public void call(){

        call.enqueue(new Callback<List<event>>() {
            @Override
            public void onResponse(Call<List<event>> call, Response<List<event>> response) {

                loadingLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    Utils.makeToast("LoadingLayout old data", context);
                    loadFromSp();
                    return;
                }

                List<event> events = response.body();
                if(events == null || events.isEmpty()){
                    return;
                }

                eventsList.clear();
                eventsList.addAll(events);
                Collections.reverse(eventsList);
                adapter.notifyDataSetChanged();

                saveIntoSp();
            }

            @Override
            public void onFailure(Call<List<event>> call, Throwable t) {
                Utils.makeToast("LoadingLayout old data", context);
                loadFromSp();
            }
        });
    }

    public void loadList(){

        if(eventsList.isEmpty() || isRefresh()){
            loadingLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            call = RetrofitSingleton.get().getData().get_events();
            call();
        }
    }

    public void loadFromSp(){

        Gson gson = new Gson();
        String json;
        Type type;

        json = Utils.getSharedPref("events", "", context);
        type = new TypeToken<List<event>>() {}.getType();

        ArrayList<event> list = gson.fromJson(json, type);

        if(list != null && !list.isEmpty()) {
            eventsList.clear();
            eventsList.addAll(list);
            adapter.notifyDataSetChanged();
            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void saveIntoSp(){
        Gson gson = new Gson();
        String json = gson.toJson(eventsList);
        Utils.setSharedPref("events", json, context);
    }

    public boolean isRefresh(){
        boolean refresh = Utils.getSharedPref("refresh_events" , false, context);
        Utils.setSharedPref("refresh_events", false, context);
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