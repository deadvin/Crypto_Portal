package com.upperhand.cryptoterminal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.adapters.DivAdapter;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import com.upperhand.cryptoterminal.objects.div;
import com.upperhand.cryptoterminal.objects.lastPrice;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class FragmentPrices extends Fragment {

    //region VARS

    ArrayList<div> listDiv_1m;
    ArrayList<div> listDiv_15m;
    ArrayList<div> listDiv_1h;
    ArrayList<div> listDiv_1d;
    ArrayList<div> activeList;
    ArrayList<lastPrice> listLastPrices;
    RecyclerView recyclerView;
    DivAdapter adapter;
    Call<List<div>> call;
    Call<List<lastPrice>> callLastPrice;
    RelativeLayout loadingLayout;
    Context context;
    Spinner spinnerTimePeriod;
    LinearLayout btnPriceFilter;
    LinearLayout btnNameFilter;
    ImageView priceImg;
    ImageView nameImg;
    public boolean isPriceUp;
    public boolean isNameUp;
    public boolean isNameSelected;
    ArrayAdapter<String> spinerAdapter;
    String selected = "1 MIN";
    private static final String s = "[\"WAVESUSDT\",\"TFUELUSDT\",\"ZECUSDT\",\"NEOUSDT\",\"HBARUSDT\",\"LINKUSDT\",\"HOTUSDT\",\"AGLDUSDT\",\"APEUSDT\",\"FLOWUSDT\",\"FTTUSDT\",\"BATUSDT\",\"AUDIOUSDT\",\"ADAUSDT\",\"SOLUSDT\",\"DOGEUSDT\",\"MATICUSDT\",\"LUNAUSDT\",\"DOTUSDT\",\"INJUSDT\",\"AVAXUSDT\",\"EOSUSDT\",\"THETAUSDT\",\"IOTAUSDT\",\"CHZUSDT\",\"ONEUSDT\",\"KSMUSDT\",\"NEARUSDT\",\"XTZUSDT\",\"ALGOUSDT\",\"EGLDUSDT\",\"ARUSDT\",\"AXSUSDT\",\"XRPUSDT\",\"FILUSDT\",\"VETUSDT\",\"UNIUSDT\",\"CAKEUSDT\",\"ICPUSDT\",\"ETCUSDT\",\"ZILUSDT\",\"XLMUSDT\",\"XMRUSDT\",\"SANDUSDT\",\"RVNUSDT\",\"MANAUSDT\",\"LTCUSDT\",\"GALAUSDT\",\"BNBUSDT\",\"AAVEUSDT\",\"ANKRUSDT\",\"1INCHUSDT\",\"COTIUSDT\",\"ENJUSDT\",\"ATOMUSDT\",\"ETHUSDT\",\"FTMUSDT\"]";
    private static final String apiBinanceUrl = "https://api.binance.com/api/v3/ticker/price?symbols=" + s;

    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this.getActivity();
        listDiv_1m = new ArrayList<>();
        listDiv_15m = new ArrayList<>();
        listDiv_1h = new ArrayList<>();
        listDiv_1d = new ArrayList<>();
        activeList = new ArrayList<>();
        listLastPrices = new ArrayList<>();
        isPriceUp = true;
        isNameSelected = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_div, container, false);
        loadingLayout = view.findViewById(R.id.loadingPanel);
        recyclerView =  view.findViewById(R.id.recView);
        spinnerTimePeriod = view.findViewById(R.id.spinner_price);
        btnNameFilter = view.findViewById(R.id.ll_name);
        btnPriceFilter = view.findViewById(R.id.price);
        priceImg = view.findViewById(R.id.filter_price);
        nameImg = view.findViewById(R.id.filter_name);

        adapter = new DivAdapter(context, activeList, listLastPrices);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        btnNameFilter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                priceImg.setImageResource(R.drawable.sort_off);
                isNameUp =! isNameUp;
                if(!isNameSelected){
                    isNameUp = true; }
                isNameSelected = true;
                if(isNameUp){
                    nameImg.setImageResource(R.drawable.sort_up);
                }else{
                    nameImg.setImageResource(R.drawable.sort_down); }

                loadList("filter");
            }
        });

        btnPriceFilter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameImg.setImageResource(R.drawable.sort_off);
                isPriceUp =! isPriceUp;
                if(isNameSelected){
                    isPriceUp = true;
                }
                isNameSelected = false;
                if(isPriceUp){
                    priceImg.setImageResource(R.drawable.sort_up);
                }else{
                    priceImg.setImageResource(R.drawable.sort_down);
                }

                loadList("filter");
            }
        });

        //==========================   SPINNER

        String[] items = new String[]{"1 MIN","15 MIN","1 HOUR","1 DAY"};

        spinerAdapter = new ArrayAdapter<String>(getContext(),  R.layout.spin_item_dark_main, items);
        spinerAdapter.setDropDownViewResource(R.layout.spin_item_dark);
        spinnerTimePeriod.setAdapter(spinerAdapter);
        spinnerTimePeriod.setSelection(0, true);
        spinnerTimePeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selected = String.valueOf(spinnerTimePeriod.getAdapter().getItem(position));
                loadList("time");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        loadList("create");
        return view;
    }

    public void loadList(String type){

        switch (type) {
            case "create":
                loadingLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                buildCall();
                adapter.setMode(selected);
                adapter.isAnimate = true;
                call_price(false);
                call(true);
                break;
            case "filter":
                sortList();
                adapter.setMode(selected);
                adapter.isAnimate = false;
                adapter.refreshSavedX();
                adapter.notifyDataSetChanged();
                recyclerView.getLayoutManager().scrollToPosition(0);
                break;
            case "time":
                if(curList().isEmpty()){
                    loadingLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    buildCall();

                    adapter.refreshSavedX();
                    recyclerView.setAdapter(adapter);
                    adapter.setMode(selected);
                    adapter.isAnimate = true;
                    call(true);
                }else{
                    recyclerView.setAdapter(adapter);
                    adapter.setMode(selected);
                    adapter.isAnimate = true;
                    adapter.refreshSavedX();
                    adapter.notifyDataSetChanged();
                }
                break;
            case "refresh":
                call_price(true);
                break;
            case "refreshBig":
                buildCall();
                adapter.setMode(selected);
                adapter.isSaveX = true;
                adapter.isAnimate = false;
                call(false);
                break;
        }
    }

    public void call_price(boolean notify){

        callLastPrice = RetrofitSingleton.get().getDataPrices().get_last_price(apiBinanceUrl);

        callLastPrice.enqueue(new Callback<List<lastPrice>>() {
            @Override
            public void onResponse(Call<List<lastPrice>> call, Response<List<lastPrice>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                List<lastPrice> divs = response.body();
                if(divs != null) {
                    listLastPrices.clear();
                    listLastPrices.addAll(divs);
                    if(notify) {
                        adapter.notifyItemRangeChanged(0,adapter.getItemCount(), DivAdapter.PAYLOAD_NAME);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<lastPrice>> call, Throwable t) {
            }
        });
    }

    public void call(boolean resetScroll){

        call.enqueue(new Callback<List<div>>() {
            @Override
            public void onResponse(Call<List<div>> call, Response<List<div>> response) {
                if (!response.isSuccessful()) {
                    loadFromSp(resetScroll);
                    return;
                   }

                List<div> divs = response.body();
                if(divs == null || divs.isEmpty()){
                    return;
                }

                curList().clear();
                curList().addAll(divs);
                activeList.clear();
                activeList.addAll(curList());
                sortList();

                if(resetScroll) {
                    recyclerView.getLayoutManager().scrollToPosition(0);
                }
                adapter.notifyDataSetChanged();
                loadingLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                saveIntoSp();
            }
            @Override
            public void onFailure(Call<List<div>> call, Throwable t) {
                loadFromSp(resetScroll);
            }
        });
    }

    public void loadFromSp(boolean resetScroll){

        Gson gson = new Gson();
        String json = null;
        Type type;
        ArrayList<div> list;

        json = Utils.getSharedPref("div" + selected, "", context);
        type = new TypeToken<List<div>>(){}.getType();
        list = gson.fromJson(json, type);

        if(list != null && !list.isEmpty()) {

            curList().clear();
            curList().addAll(list);
            activeList.clear();
            activeList.addAll(listDiv_1m);
            sortList();
            if(resetScroll) {
                recyclerView.getLayoutManager().scrollToPosition(0);
            }
            adapter.notifyDataSetChanged();

            loadingLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void saveIntoSp(){

        Gson gson = new Gson();
        String json = gson.toJson(activeList);
        Utils.setSharedPref("div" + selected, json, context);

    }

    public void refresh(boolean big){

        if(!big){
            loadList("refresh");
        }else {
            loadList("refreshBig");
        }
    }

    public void buildCall(){

        switch (selected) {
            case "1 MIN":
                call = RetrofitSingleton.get().getData().get_div_1m();
                break;
            case "15 MIN":
                call = RetrofitSingleton.get().getData().get_div_15m();
                break;
            case "1 HOUR":
                call = RetrofitSingleton.get().getData().get_div_1h();
                break;
            case "1 DAY":
                call = RetrofitSingleton.get().getData().get_div_1d();
                break;
        }
    }

    public ArrayList<div> curList(){

        switch (selected) {
            case "1 MIN":
                return listDiv_1m;
            case "15 MIN":
                return listDiv_15m;
            case "1 HOUR":
                return listDiv_1h;
            case "1 DAY":
                return listDiv_1d;
        }
        return null;
    }

    public void sortList(){

        if(isNameSelected){
            if(isNameUp){
                activeList.sort(new Comparator<div>() {
                    public int compare(div o1, div o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
            }else{
                activeList.sort(new Comparator<div>() {
                    public int compare(div o1, div o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                Collections.reverse(activeList);
            }
        }else{
            if(isPriceUp){
                activeList.sort(new Comparator<div>() {
                    public int compare(div o1, div o2) {
                        return Float.compare(o1.getAverageDiv(), o2.getAverageDiv());
                    }
                });
                Collections.reverse(activeList);
            }else{
                activeList.sort(new Comparator<div>() {
                    public int compare(div o1, div o2) {
                        return Float.compare(o1.getAverageDiv(), o2.getAverageDiv());
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {


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
        if(callLastPrice != null) {
            callLastPrice.cancel();
        }
        super.onDestroy();
    }

}

