package com.upperhand.cryptoterminal.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.upperhand.cryptoterminal.R;
import com.upperhand.cryptoterminal.objects.div;
import com.upperhand.cryptoterminal.objects.lastPrice;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class DivAdapter extends RecyclerView.Adapter<DivAdapter.ViewHolder> {

    Context mContext;
    int count = 0;
    int numberElements = 0;
    public boolean isAnimate;
    ArrayList<String> listHours = new ArrayList<>();
    ArrayList<lastPrice> listLastPrices ;
    ArrayList<div> listDivs = new ArrayList<>();;
    float intervalSize;
    int hours;
    int mins;
    int visibleElements;
    public static final String PAYLOAD_NAME = "PRICE_UPDATE";
    public boolean isSaveX;
    String[] monthNames = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    ArrayList<Float> savedX = new ArrayList<>();
    
    
    public DivAdapter(Context context, ArrayList<div> listDivs, ArrayList<lastPrice> listLastPrices) {
        this.mContext = context;
        this.listLastPrices = listLastPrices;
        this.listDivs = listDivs;
        savedX.clear();
        for (int i = 0; i < 60; i++) {
            savedX.add(-1f);
        }
    }

    @Override
    public int getItemCount() {
        return listDivs.size();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_div, parent, false);
        return new ViewHolder(view);
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        
        LineChart chart;
        TextView name;
        TextView name_full;
        TextView price;
        ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            chart = itemView.findViewById(R.id.chart1);
            name = itemView.findViewById(R.id.name_alt);
            name_full = itemView.findViewById(R.id.name_full);
            icon = itemView.findViewById(R.id.imageView);
            price = itemView.findViewById(R.id.price);
        }
    }
    
    public void setMode(String mode){

        Calendar calendar = Calendar.getInstance();
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        mins = calendar.get(Calendar.MINUTE);
        listHours.clear();
        boolean half;
        
        switch (mode) {
            case "1 MIN":
                //region 1 MIN
                visibleElements = 15;
                intervalSize = 0.4f;
                int mins_10 = mins % 10;
                int mins_5 = mins % 5;

                if (mins_10 == 5 || mins_10 == 6 || mins_10 == 0) {
                    numberElements = 151 - (5 - mins_5);
                    count = (numberElements / 4) - 7;
                } else if (mins_10 == 1) {
                    numberElements = 148 - mins_5;
                    count = (numberElements / 4) - 7;
                } else if (mins_10 < 3) {
                    numberElements = 150 - mins_5;
                    count = (numberElements / 4) - 8;
                } else if (mins_10 < 5) {
                    numberElements = 151 - (5 - mins_5);
                    count = (numberElements / 4) - 8;
                } else {
                    numberElements = 151 - (5 - mins_5);
                    count = (numberElements / 4) - 8;
                }
                mins = (mins / 5) * 5;
                if (mins_5 == 0) {
                    listHours.add(" ");
                    listHours.add(" ");
                } else {
                    if (mins < 10) {
                        listHours.add(hours + ":0" + mins);
                        listHours.add(hours + ":0" + mins);
                    } else {
                        listHours.add(hours + ":" + mins);
                        listHours.add(hours + ":" + mins);
                    }
                }
                for (int i = 0; i < count; i++) {
                    mins -= 5;
                    if (mins < 0) {
                        hours -= 1;
                        mins = 55;
                    }
                    if (hours < 0) {
                        hours = 23;
                    }
                    if (mins < 10) {
                        listHours.add(hours + ":0" + mins);
                        listHours.add(hours + ":0" + mins);
                    } else {
                        listHours.add(hours + ":" + mins);
                        listHours.add(hours + ":" + mins);
                    }
                }
                Collections.reverse(listHours);
                //endregion
                break;
            case "15 MIN":
                //region HOURS ARRAY 15 MIN
                visibleElements = 10;
                intervalSize = 0.5f;
                if (mins > 30) {
                    listHours.add(hours + ":30");
                    half = true;
                    if (mins < 45) {
                        numberElements = 149;
                        count = 74;
                    } else {
                        numberElements = 150;
                        count = 74;
                    }
                } else {
                    listHours.add(hours + ":00");
                    half = false;
                    hours -= 1;
                    if (mins >= 15) {
                        numberElements = 150;
                        count = 74;
                    } else {
                        numberElements = 149;
                        count = 74;
                    }
                }
                for (int i = 0; i < count; i++) {
                    if (half) {
                        half = false;
                        listHours.add(hours + ":00");
                        hours -= 1;
                    } else {
                        half = true;
                        listHours.add(hours + ":30");
                    }
                    if (hours < 0) {
                        hours = 23;
                    }
                }
                Collections.reverse(listHours);
                //endregion
                break;
            case "1 HOUR":
                //region HOURS ARRAY 1 HOUR
                visibleElements = 10;
                intervalSize = 0.5f;
                numberElements = 150;
                count = 75;
                hours -= 1;
                if (hours < 0) {
                    hours = 23;
                }
                for (int i = 0; i < count; i++) {
                    listHours.add(hours + ":00");
                    hours -= 2;
                    if (hours == -2) {
                        hours = 22;
                    } else if (hours == -1) {
                        hours = 23;
                    }
                }
                Collections.reverse(listHours);
                //endregion
                break;
            case "1 DAY":
                //region HOURS ARRAY 1 DAY
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                String monthName = monthNames[month];
                visibleElements = 10;
                intervalSize = 0.5f;
                numberElements = 150;
                count = 75;
                day -= 1;
                if (day == 0) {
                    month -= 1;
                    monthName = monthNames[month];
                    day = getLastMonthDays(month);
                }
                for (int i = 0; i < count; i++) {
                    listHours.add(day + monthName);
                    day -= 2;
                    if (day == -1) {
                        month -= 1;
                        monthName = monthNames[month];
                        day = getLastMonthDays(month) - 1;
                    } else if (day == 0) {
                        month -= 1;
                        monthName = monthNames[month];
                        day = getLastMonthDays(month);
                    }
                }
                Collections.reverse(listHours);
                //endregion
                break;
        }
    }

    public void refreshSavedX(){

        isSaveX = false;
        savedX.clear();
        for (int i = 0; i < 60; i++) {
            savedX.add(-1f);
        }
    }

    public int getLastMonthDays(int month){
        month+=1;
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else if (month == 2) {
            return 28;
        } else {
            return 31;
        }
    }

    public void onBindViewHolder(@NotNull DivAdapter.ViewHolder holder, int position, @NotNull final List<Object> payloads) {

        if (!payloads.isEmpty()) {
            for (int i = 0; i < listLastPrices.size(); i++) {
                if((listDivs.get(position).getName() + "USDT").equals(listLastPrices.get(i).getName())){
                    holder.price.setText(" " + listLastPrices.get(i).getPrice() + "$");
                }
            if(savedX.get(position) >=0) {
                holder.chart.moveViewToX(savedX.get(position));
               }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }


    public void onBindViewHolder(@NotNull DivAdapter.ViewHolder holder, int position) {

        //   ============   CREATE HOLDER VIEW
        
        String name_alt;
        
        name_alt = listDivs.get(position).getName().toLowerCase();
        holder.name.setText(name_alt);
        holder.name_full.setText(listDivs.get(position).getFullName());

        //    =========   CREATE DATA FROM  OBJECTS

        ArrayList<Float> numbers;
        numbers = listDivs.get(position).getPrice();
        ArrayList<Entry> dataValsPos  = new ArrayList<>();
        ArrayList<Entry> dataValsNeg  = new ArrayList<>();
        numbers = new ArrayList<>(numbers.subList(numbers.size()-Math.min(numbers.size(),numberElements), numbers.size()));

        //   =========   BARCHART DATA

        boolean isOnlyPos = true;
        boolean isOnlyNeg = true;

        for (int i = 0; i < numbers.size(); i++) {
            Float entry = numbers.get(i);
            if(entry < -80){
                entry = 0f;
            }
            if(entry > 0){
                dataValsNeg.add(new BarEntry((i * intervalSize), 0));
                dataValsPos.add(new BarEntry((i * intervalSize), entry));
                isOnlyNeg = false;
            }else{
                dataValsPos.add(new BarEntry((i * intervalSize), 0));
                dataValsNeg.add(new BarEntry((i * intervalSize), entry));
                isOnlyPos = false;
            }
        }
        
        //   ============   POSITIVE DATA SET

        LineDataSet linedatasetPos = new LineDataSet(dataValsPos,"dataset");
        linedatasetPos.setDrawValues(false);
        linedatasetPos.setDrawCircles(false);
        linedatasetPos.setColor(R.color.blue);
        linedatasetPos.setColor(mContext.getResources().getColor(R.color.blue));
        linedatasetPos.setLineWidth(0.5f);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_blue);
        linedatasetPos.setFillDrawable(drawable);
        linedatasetPos.setDrawFilled(true);

        //   ============   NEGATIVE DATA SET

        LineDataSet linedatasetNeg = new LineDataSet(dataValsNeg,"dataset");
        linedatasetNeg.setDrawValues(false);
        linedatasetNeg.setDrawCircles(false);
        linedatasetNeg.setColor(R.color.red);
        linedatasetNeg.setColor(mContext.getResources().getColor(R.color.red));
        linedatasetNeg.setLineWidth(0.5f);

        drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_red);
        linedatasetNeg.setFillDrawable(drawable);
        linedatasetNeg.setDrawFilled(true);

        //   ============   ADD LINESETS TO DATASET

        ArrayList<ILineDataSet> dataSets_vol = new ArrayList<>();
        if(!isOnlyNeg){dataSets_vol.add(linedatasetPos);}
        if(!isOnlyPos){dataSets_vol.add(linedatasetNeg);}
        LineData data = new LineData(dataSets_vol);

        //   ============   SET IMAGE AND CURRENT PRICE

        if(name_alt.equals("1inch")){
            name_alt = "oneinch";
        }
        String uri = "@drawable/" + name_alt;
        int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
        Drawable res = ContextCompat.getDrawable(mContext,imageResource);
        holder.icon.setImageDrawable(res);

        for (int i = 0; i < listLastPrices.size(); i++) {
            if((listDivs.get(position).getName() + "USDT").equals(listLastPrices.get(i).getName())){
                holder.price.setText(" " + listLastPrices.get(i).getPrice() + "$");
            }
        }

        //     ================  SET DATA

        holder.chart.setData(data);
        holder.chart.invalidate();
        if(isAnimate) {
            holder.chart.animateY(650);
        }

        //      ================  VISUAL

        Legend legend = holder.chart.getLegend();
        legend.setEnabled(false);

        holder.chart.getDescription().setXOffset(10);
        holder.chart.getAxisLeft().setDrawLabels(false);
        holder.chart.setVisibleXRangeMaximum(visibleElements);
        holder.chart.moveViewToX(180);
        holder.chart.setScaleEnabled(false);
        holder.chart.setViewPortOffsets(10f, 10f, 100f, 10f);
        holder.chart.getAxisRight().setDrawGridLines(false);
        holder.chart.getAxisLeft().setEnabled(true);
        holder.chart.getAxisLeft().setLabelCount(0, true);
        holder.chart.getAxisLeft().setDrawZeroLine(true);
        holder.chart.getDescription().setEnabled(false);

        if(isSaveX) {
            if(savedX.get(position) >=0) {
                holder.chart.moveViewToX(savedX.get(position));
            }
        }

        holder.chart.setDragDecelerationEnabled(false);
        holder.chart.setOnChartGestureListener(new OnChartGestureListener() {

            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                float lowX = holder.chart.getLowestVisibleX();
                savedX.set(position, lowX);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }
            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }
            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }
            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }
            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
            }
        });

//        ================  SET TOP HOURS

        XAxis xAxis =  holder.chart.getXAxis();
        try {
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return listHours.get((int) value);
                }
            });
        }catch (Exception e){}

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0.5f);
        xAxis.setYOffset(-15);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawGridLinesBehindData(true);
        xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.gray_super_light));
    }
    
}

























