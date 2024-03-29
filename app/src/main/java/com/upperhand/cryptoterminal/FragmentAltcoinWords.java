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
import com.upperhand.cryptoterminal.adapters.WordsAdapter;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import com.upperhand.cryptoterminal.objects.word;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAltcoinWords extends Fragment {

    ArrayList<word> listWords;
    RecyclerView recyclerView;
    WordsAdapter adapter;
    Call<List<word>> call;
    RelativeLayout loadingLayout;
    Context context;
    ImageButton btnInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        listWords = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_keywords_altoins, container, false);
        loadingLayout = view.findViewById(R.id.loadingPanel_altcoins);
        recyclerView =  view.findViewById(R.id.listView);
        btnInfo = view.findViewById(R.id.button12);

        adapter = new WordsAdapter(context, listWords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        btnInfo.setOnClickListener( new View.OnClickListener() {    //     FILTERED

            @Override
            public void onClick(View v) {
                Utils.buildAlertDialogue(R.layout.dialogue_info, context);
                TextView text = Utils.getAlertDialogue().findViewById(R.id.textView2);
                text.setText(R.string.info_keywords_altcoins);
                Utils.getAlertDialogue().show();
            }
        });


        return view;
    }

    public void call(){
        call.enqueue(new Callback<List<word>>() {
            @Override
            public void onResponse(@NotNull Call<List<word>> call, @NotNull Response<List<word>> response) {

                loadingLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (!response.isSuccessful()) {
                    Utils.makeToast("Loading old data", context);
                    loadFromSp();
                    return; }

                List<word> words = response.body();
                if(words == null || words.isEmpty()){
                    return;
                }
                listWords.clear();
                listWords.addAll(words);
                listWords.sort(new Comparator<word>() {
                    public int compare(word o1, word o2) {
                        return Float.compare(o1.getLastAv(), o2.getLastAv());
                    }});
                Collections.reverse(listWords);
                adapter.notifyDataSetChanged();

                saveIntoSp();
            }

            @Override
            public void onFailure(Call<List<word>> call, Throwable t) {
                Utils.makeToast("Loading old data", context);
                loadFromSp();
            }
        });
    }

    public void loadList(){

        if(listWords.isEmpty() || isRefresh()){
            loadingLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            call = RetrofitSingleton.get().getData().get_words_trend();
            call();
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    public void loadFromSp(){
        Gson gson = new Gson();
        String json;
        Type type;

        json = Utils.getSharedPref("trends", "", context);
        type = new TypeToken<List<word>>() {}.getType();
        ArrayList<word> list = gson.fromJson(json, type);

        if(list != null && !list.isEmpty()) {
            listWords.clear();
            listWords.addAll(list);
            adapter.notifyDataSetChanged();
            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void saveIntoSp(){
        Gson gson = new Gson();
        String json = gson.toJson(listWords);
        Utils.setSharedPref("trends", json, context);
    }

    public boolean isRefresh(){
        boolean refresh = Utils.getSharedPref("refresh_keywords_altcoins", false, context);
        Utils.setSharedPref("refresh_keywords_altcoins", false, context);
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
        loadList();
        super.onResume();
    }

}