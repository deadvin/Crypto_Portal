package com.upperhand.cryptoterminal;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
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

import static android.content.Context.MODE_PRIVATE;

public class FragmentBitcoinWords extends Fragment {

    ArrayList<Integer> list_trends_flat;
    ArrayList<Integer> list_trends_vol;
    Call<List<word>> call;
    Context context;
    ImageButton info;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String api_url;
    ImageView greed_fear_index;
    Drawable d;
    LineChart linechart_flat;
    LineChart linechart_vol;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        api_url = getString(R.string.url);
        list_trends_flat = new ArrayList<>();
        list_trends_vol = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_keywords_btc, container, false);
        greed_fear_index = view.findViewById(R.id.imageView);
        linechart_flat = view.findViewById(R.id.chart1);
        linechart_vol = view.findViewById(R.id.chart2);
        info = view.findViewById(R.id.button12);

        thread_greed_fear.start();

        call = RetrofitSingleton.get().getData().get_trend_btc();
        call();

        info.setOnClickListener( new View.OnClickListener() {

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
                al1.setText(R.string.info_keywords_bitcoin);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customDialog.dismiss();
                    }
                });

                customDialog.show();
            }
        });


        return view;
    }

    public void add_linechart(){

        //        ============   CREATE HOURS ARRAY

        //region HOURS ARRAY

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);
        boolean half;
        int num_elements = 101;
        int count = 50;

        ArrayList<String> mylist = new ArrayList<String>();
        if (mins > 30){
            mylist.add(hours + ":30");
            half = true;
            if(mins < 45) {
                num_elements = 103;
                count = 51;
            }else {
                num_elements = 102;
                count = 50;
            }
        }else {
            mylist.add(hours + ":00");
            half = false;
            hours -=1;
            if(mins >= 15) {
                num_elements = 102;
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


        ArrayList<Entry> dataVals_vol  = new ArrayList<Entry>();
        ArrayList<Entry> dataVals_flat  = new ArrayList<Entry>();
        list_trends_vol = new ArrayList<>(list_trends_vol.subList(list_trends_vol.size()-Math.min(list_trends_vol.size(),num_elements), list_trends_vol.size()));
        list_trends_flat = new ArrayList<>(list_trends_flat.subList(list_trends_flat.size()-Math.min(list_trends_flat.size(),num_elements), list_trends_flat.size()));

        for (int i = 0; i < list_trends_vol.size(); i++) {
            int entry = list_trends_vol.get(i);
            if (entry < 0) {
                entry = 0;
            }
            dataVals_vol.add(new BarEntry((float)(i*0.5), entry));
        }

        for (int i = 0; i < list_trends_flat.size(); i++) {
            int entry = list_trends_flat.get(i);
            if (entry < 0) {
                entry = 0;
            }
            dataVals_flat.add(new BarEntry((float)(i*0.5), entry));
        }

        LineDataSet linedataset_vol = new LineDataSet(dataVals_vol,"dataset");
        linedataset_vol.setDrawCircles(false);
        ArrayList<ILineDataSet> dataSets_vol = new ArrayList<>();
        dataSets_vol.add(linedataset_vol);
        linedataset_vol.setDrawValues(false);
        linedataset_vol.setColor(context.getResources().getColor(R.color.blue));
        linedataset_vol.setLineWidth(1);
        LineData data_vol = new LineData(dataSets_vol);

        LineDataSet linedataset_flat = new LineDataSet(dataVals_flat,"dataset");
        linedataset_flat.setDrawCircles(false);
        ArrayList<ILineDataSet> dataSets_flat = new ArrayList<>();
        dataSets_flat.add(linedataset_flat);
        linedataset_flat.setDrawValues(false);
        linedataset_flat.setColor(context.getResources().getColor(R.color.blue));
        linedataset_flat.setLineWidth(1);

        LineData data_flat = new LineData(dataSets_flat);

//        =========== SET FILL COLOUR

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_blue);
        linedataset_vol.setFillDrawable(drawable);
        linedataset_vol.setDrawFilled(true);
        linedataset_flat.setFillDrawable(drawable);
        linedataset_flat.setDrawFilled(true);

//        ================  SET DATA

        linechart_vol.setData(data_vol);
        linechart_vol.invalidate();
        linechart_vol.animateY(1500);
        linechart_vol.setData(data_vol);

        linechart_flat.setData(data_flat);
        linechart_flat.invalidate();
        linechart_flat.animateY(1500);
        linechart_flat.setData(data_flat);


//        ================  VISUAL

        Legend l_v =  linechart_vol.getLegend();
        l_v.setEnabled(false);
        linechart_vol.getAxisLeft().setDrawLabels(false);
        linechart_vol.getDescription().setEnabled(false);
        linechart_vol.setVisibleXRangeMaximum(15);
        linechart_vol.moveViewToX(180);
        linechart_vol.setScaleEnabled(false);

        Legend l_f =  linechart_flat.getLegend();
        l_f.setEnabled(false);
        linechart_flat.getAxisLeft().setDrawLabels(false);
        linechart_flat.getDescription().setEnabled(false);
        linechart_flat.setVisibleXRangeMaximum(15);
        linechart_flat.moveViewToX(180);
        linechart_flat.setScaleEnabled(false);


//        ================  SET TOP HOURS

        XAxis xAxis = linechart_vol.getXAxis();
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


        xAxis = linechart_flat.getXAxis();
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

    Thread thread_greed_fear = new Thread(new Runnable() {

        @Override
        public void run() {
            try  {
                try {
                    InputStream is = (InputStream) new URL("https://alternative.me/crypto/fear-and-greed-index.png").getContent();
                     d = Drawable.createFromStream(is, "src name");
                     greed_fear_index.setImageDrawable(d);
                } catch (Exception e) {
                    Log.e("asd",e + " oooooooo ");
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
                    load_from_sp();
                    return;
                }
                List<word> words = response.body();
                if(words == null || words.isEmpty()){
                    return;
                }

                list_trends_flat.clear();
                list_trends_vol.clear();
                list_trends_flat.addAll(words.get(0).getNumbers());
                list_trends_vol.addAll(words.get(0).getNumbers_vol());

                save_sp(words);
                add_linechart();
            }
            @Override
            public void onFailure(Call<List<word>> call, Throwable t) {
                load_from_sp();
            }
        });
    }

    public void load_from_sp(){

        Gson gson = new Gson();
        String json = null;
        Type type;

        if(!list_trends_flat.isEmpty() && !list_trends_vol.isEmpty()){ return;}

        preferences = context.getSharedPreferences("trends_btc", Context.MODE_PRIVATE);
        json = preferences.getString("trends_btc", "");


        type = new TypeToken<List<word>>(){}.getType();
        List<word> list_words = gson.fromJson(json, type);
        if(list_words != null && !list_words.isEmpty()) {

            list_trends_flat.addAll(list_words.get(0).getNumbers());
            list_trends_vol.addAll(list_words.get(0).getNumbers_vol());
            add_linechart();

        }
    }

    public void save_sp(List<word> words){

        Gson gson = new Gson();
        String json = gson.toJson(words);

        editor = context.getSharedPreferences("trends_btc", MODE_PRIVATE).edit();
        editor.putString("trends_btc", json);
        editor.apply();

    }

    public boolean is_refresh(){

        preferences = context.getSharedPreferences("refresh_keywords_btc", Context.MODE_PRIVATE);
        boolean refresh = preferences.getBoolean("refresh_keywords_btc", false);
        Log.e("asd", " bolean " +refresh);

        editor = context.getSharedPreferences("refresh_keywords_btc", MODE_PRIVATE).edit();
        editor.putBoolean("refresh_keywords_btc", false);
        editor.apply();

        return refresh;
    }

    @Override
    public void onResume() {
        Log.e("asd", " on resumee ");
        if(is_refresh()){
            Log.e("asd", " refresh bitkon trend");
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