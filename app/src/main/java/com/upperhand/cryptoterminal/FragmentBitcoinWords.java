package com.upperhand.cryptoterminal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.upperhand.cryptoterminal.dependencies.RetrofitSingleton;
import com.upperhand.cryptoterminal.objects.word;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentBitcoinWords extends Fragment {

    ArrayList<Integer> listTrendsFlat;
    ArrayList<Integer> listTrendsVol;
    Call<List<word>> call;
    Context context;
    ImageButton btnInfo;
    ImageView greedFearIndex;
    Drawable drawable;
    LineChart linechartFlat;
    LineChart linechartVol;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this.getActivity();
        listTrendsFlat = new ArrayList<>();
        listTrendsVol = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_keywords_btc, container, false);
        greedFearIndex = view.findViewById(R.id.imageView);
        linechartFlat = view.findViewById(R.id.chart1);
        linechartVol = view.findViewById(R.id.chart2);
        btnInfo = view.findViewById(R.id.button12);

        loadGreedFearImage.start();

        call = RetrofitSingleton.get().getData().get_trend_btc();
        call();

        btnInfo.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Utils.buildAlertDialogue(R.layout.dialogue_info, context);
                TextView text = Utils.getAlertDialogue().findViewById(R.id.textView2);
                text.setText(R.string.info_keywords_bitcoin);
                Utils.getAlertDialogue().show();
            }
        });
        
        return view;
    }

    public void inflateLinecharts(){

        //region HOURS ARRAY

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);
        boolean half;
        int numElements = 101;
        int count = 50;

        ArrayList<String> mylist = new ArrayList<String>();
        if (mins > 30){
            mylist.add(hours + ":30");
            half = true;
            if(mins < 45) {
                numElements = 103;
                count = 51;
            }else {
                numElements = 102;
                count = 50;
            }
        }else {
            mylist.add(hours + ":00");
            half = false;
            hours -=1;
            if(mins >= 15) {
                numElements = 102;
            }
        }

        for (int i = 0; i < count; i++) {
            if(half){
                half = !half;
                mylist.add(hours + ":00");
                hours -=1;
            }else{
                half = !half;
                mylist.add(hours + ":30");
            }
            if(hours < 0){
                hours = 23;
            }
        }
        Collections.reverse(mylist);

        //endregion
        
        ArrayList<Entry> dataValsVol  = new ArrayList<Entry>();
        ArrayList<Entry> dataValsFlat  = new ArrayList<Entry>();
        listTrendsVol = new ArrayList<>(listTrendsVol.subList(listTrendsVol.size()-Math.min(listTrendsVol.size(),numElements), listTrendsVol.size()));
        listTrendsFlat = new ArrayList<>(listTrendsFlat.subList(listTrendsFlat.size()-Math.min(listTrendsFlat.size(),numElements), listTrendsFlat.size()));

        for (int i = 0; i < listTrendsVol.size(); i++) {
            int entry = listTrendsVol.get(i);
            if (entry < 0) {
                entry = 0;
            }
            dataValsVol.add(new BarEntry((float)(i*0.5), entry));
        }

        for (int i = 0; i < listTrendsFlat.size(); i++) {
            int entry = listTrendsFlat.get(i);
            if (entry < 0) {
                entry = 0;
            }
            dataValsFlat.add(new BarEntry((float)(i*0.5), entry));
        }

        LineDataSet linedatasetVol = new LineDataSet(dataValsVol,"dataset");
        linedatasetVol.setDrawCircles(false);
        ArrayList<ILineDataSet> dataSets_vol = new ArrayList<>();
        dataSets_vol.add(linedatasetVol);
        linedatasetVol.setDrawValues(false);
        linedatasetVol.setColor(context.getResources().getColor(R.color.blue));
        linedatasetVol.setLineWidth(1);
        LineData data_vol = new LineData(dataSets_vol);

        LineDataSet linedatasetFlat = new LineDataSet(dataValsFlat,"dataset");
        linedatasetFlat.setDrawCircles(false);
        ArrayList<ILineDataSet> dataSets_flat = new ArrayList<>();
        dataSets_flat.add(linedatasetFlat);
        linedatasetFlat.setDrawValues(false);
        linedatasetFlat.setColor(context.getResources().getColor(R.color.blue));
        linedatasetFlat.setLineWidth(1);

        LineData data_flat = new LineData(dataSets_flat);

//        =========== SET FILL COLOUR

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_blue);
        linedatasetVol.setFillDrawable(drawable);
        linedatasetVol.setDrawFilled(true);
        linedatasetFlat.setFillDrawable(drawable);
        linedatasetFlat.setDrawFilled(true);

//        ================  SET DATA

        linechartVol.setData(data_vol);
        linechartVol.invalidate();
        linechartVol.animateY(1500);
        linechartVol.setData(data_vol);

        linechartFlat.setData(data_flat);
        linechartFlat.invalidate();
        linechartFlat.animateY(1500);
        linechartFlat.setData(data_flat);
        
//        ================  VISUAL

        Legend legendVol =  linechartVol.getLegend();
        legendVol.setEnabled(false);
        linechartVol.getAxisLeft().setDrawLabels(false);
        linechartVol.getDescription().setEnabled(false);
        linechartVol.setVisibleXRangeMaximum(15);
        linechartVol.moveViewToX(180);
        linechartVol.setScaleEnabled(false);

        Legend legendFlat =  linechartFlat.getLegend();
        legendFlat.setEnabled(false);
        linechartFlat.getAxisLeft().setDrawLabels(false);
        linechartFlat.getDescription().setEnabled(false);
        linechartFlat.setVisibleXRangeMaximum(15);
        linechartFlat.moveViewToX(180);
        linechartFlat.setScaleEnabled(false);
        
//        ================  SET TOP HOURS

        XAxis xAxis = linechartVol.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return mylist.get((int) value);
            }
        });

        xAxis.setAxisMinimum(0.5f);
        xAxis.setYOffset(-15);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawGridLinesBehindData(true);
        xAxis.setGridColor(ContextCompat.getColor(context, R.color.gray_super_light));
        
        xAxis = linechartFlat.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return mylist.get((int) value);
            }
        });

        xAxis.setAxisMinimum(0.5f);
        xAxis.setYOffset(-15);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawGridLinesBehindData(true);
        xAxis.setGridColor(ContextCompat.getColor(context, R.color.gray_super_light));
    }

    Thread loadGreedFearImage = new Thread(new Runnable() {

        @Override
        public void run() {
            try  {
                try {
                    InputStream is = (InputStream) new URL("https://alternative.me/crypto/fear-and-greed-index.png").getContent();
                     drawable = Drawable.createFromStream(is, "src name");
                     greedFearIndex.setImageDrawable(drawable);
                } catch (Exception e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    public void call(){

        call.enqueue(new Callback<List<word>>() {
            @Override
            public void onResponse(Call<List<word>> call, Response<List<word>> response) {

                if (!response.isSuccessful()) {
                    loadFromSp();
                    return;
                }
                List<word> words = response.body();
                if(words == null || words.isEmpty()){
                    return;
                }



                listTrendsFlat.clear();
                listTrendsVol.clear();
                listTrendsFlat.addAll(words.get(0).getNumbers());
                listTrendsVol.addAll(words.get(0).getNumbersVol());

                saveIntoSp(words);
                inflateLinecharts();
            }
            @Override
            public void onFailure(Call<List<word>> call, Throwable t) {
                loadFromSp();
            }
        });
    }

    public void loadFromSp(){
        Gson gson = new Gson();
        String json;
        Type type;

        if(!listTrendsFlat.isEmpty() && !listTrendsVol.isEmpty()){ return;}
        json = Utils.getSharedPref("trends_btc", "", context);
        type = new TypeToken<List<word>>(){}.getType();
        List<word> list_words = gson.fromJson(json, type);

        if(list_words != null && !list_words.isEmpty()) {
            listTrendsFlat.addAll(list_words.get(0).getNumbers());
            listTrendsVol.addAll(list_words.get(0).getNumbersVol());
            inflateLinecharts();
        }
    }

    public void saveIntoSp(List<word> words){
        Gson gson = new Gson();
        String json = gson.toJson(words);
        Utils.setSharedPref("trends_btc", json, context);
    }

    public boolean isRefresh(){
        boolean refresh = Utils.getSharedPref("refresh_keywords_btc", false, context);
        Utils.setSharedPref("refresh_keywords_btc", false, context);
        return refresh;
    }

    @Override
    public void onResume() {
        if(isRefresh()){
            call = RetrofitSingleton.get().getData().get_trend_btc();
            call();
        }
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