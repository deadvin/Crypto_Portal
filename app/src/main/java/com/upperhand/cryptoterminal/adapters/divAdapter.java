package com.upperhand.cryptoterminal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

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
import com.upperhand.cryptoterminal.objects.last_price;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class divAdapter extends ArrayAdapter<div>  {

    Context mContext;
    private int mResource;
    int count = 0;
    int num_elements = 0;
    public boolean isAnimate;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<last_price> last_prices;
    String name_alt;
    float interval_size;
    int hours;
    int mins;
    int visible_elements;
    boolean half;
    String selected;
    public boolean price_update = false;
    Calendar calendar = Calendar.getInstance();
    String[] month_names = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    ArrayList<Float> saved_x = new ArrayList<>();


    private static class ViewHolder {
        LineChart chart;
        TextView name;
        TextView name_full;
        TextView price;
        ImageView icon;
    }

    public divAdapter(Context context, int resource, ArrayList<div> objects, ArrayList<last_price> last_prices) {
        super(context, resource, objects);
        this.mContext = context;
        this.last_prices = last_prices;
        mResource = resource;

        saved_x.clear();
        for (int i = 0; i < 60; i++) {
            saved_x.add(-1f);
        }

    }

    public void setMode(String mode){

        calendar = Calendar.getInstance();
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        mins = calendar.get(Calendar.MINUTE);
        list.clear();

        selected = mode;

        switch (mode) {
            case "1 MIN":
                //region 1 MIN
                visible_elements = 15;
                interval_size = 0.4f;

                int mins_10 = mins % 10;
                int mins_5 = mins % 5;

                if (mins_10 == 5 || mins_10 == 6 || mins_10 == 0) {
                    num_elements = 151 - (5 - mins_5);
                    count = (num_elements / 4) - 7;
                } else if (mins_10 == 1) {
                    num_elements = 148 - mins_5;
                    count = (num_elements / 4) - 7;
                } else if (mins_10 < 3) {
                    num_elements = 150 - mins_5;
                    count = (num_elements / 4) - 8;
                } else if (mins_10 < 5) {
                    num_elements = 151 - (5 - mins_5);
                    count = (num_elements / 4) - 8;
                } else {
                    num_elements = 151 - (5 - mins_5);
                    count = (num_elements / 4) - 8;
                }

                mins = (mins / 5) * 5;

                if (mins_5 == 0) {
                    list.add(" ");
                    list.add(" ");
                } else {
                    if (mins < 10) {
                        list.add(hours + ":0" + mins);
                        list.add(hours + ":0" + mins);
                    } else {
                        list.add(hours + ":" + mins);
                        list.add(hours + ":" + mins);
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
                        list.add(hours + ":0" + mins);
                        list.add(hours + ":0" + mins);
                    } else {
                        list.add(hours + ":" + mins);
                        list.add(hours + ":" + mins);
                    }
                }
                Collections.reverse(list);

                //endregion
                break;
            case "15 MIN":
                //region HOURS ARRAY 15 MIN

                visible_elements = 10;
                interval_size = 0.5f;
                hours = calendar.get(Calendar.HOUR_OF_DAY);
                mins = calendar.get(Calendar.MINUTE);

                if (mins > 30) {
                    list.add(hours + ":30");
                    half = true;
                    if (mins < 45) {
                        num_elements = 149;
                        count = 74;
                    } else {
                        num_elements = 150;
                        count = 74;
                    }
                } else {
                    list.add(hours + ":00");
                    half = false;
                    hours -= 1;
                    if (mins >= 15) {
                        num_elements = 150;
                        count = 74;
                    } else {
                        num_elements = 149;
                        count = 74;
                    }
                }
                for (int i = 0; i < count; i++) {
                    if (half) {
                        half = false;
                        list.add(hours + ":00");
                        hours -= 1;
                    } else {
                        half = true;
                        list.add(hours + ":30");
                    }
                    if (hours < 0) {
                        hours = 23;
                    }
                }
                Collections.reverse(list);


                //endregion
                break;
            case "1 HOUR":
                //region HOURS ARRAY 1 HOUR

                hours = calendar.get(Calendar.HOUR_OF_DAY);

                visible_elements = 10;
                interval_size = 0.5f;
                num_elements = 150;
                count = 75;

                hours -= 1;
                if (hours < 0) {
                    hours = 23;
                }
                for (int i = 0; i < count; i++) {
                    list.add(hours + ":00");
                    hours -= 2;
                    if (hours == -2) {
                        hours = 22;
                    } else if (hours == -1) {
                        hours = 23;
                    }
                }
                Collections.reverse(list);

                //endregion
                break;
            case "1 DAY":
                //region HOURS ARRAY 1 DAY

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);

                String month_name = month_names[month];

                visible_elements = 10;
                interval_size = 0.5f;
                num_elements = 150;
                count = 75;

                day -= 1;
                if (day == 0) {
                    month -= 1;
                    month_name = month_names[month];
                    day = get_last_month_days(month);
                }

                for (int i = 0; i < count; i++) {
                    list.add(day + month_name);
                    day -= 2;
                    if (day == -1) {
                        month -= 1;
                        month_name = month_names[month];
                        day = get_last_month_days(month) - 1;
                    } else if (day == 0) {
                        month -= 1;
                        month_name = month_names[month];
                        day = get_last_month_days(month);
                    }
                }
                Collections.reverse(list);

                //endregion
                break;
        }
    }

    public void refresh_saved_x(){
        price_update = false;
        saved_x.clear();
        for (int i = 0; i < 60; i++) {
            saved_x.add(-1f);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //   ============   CREATE HOLDER VIEW

        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder.chart = convertView.findViewById(R.id.chart1);
            holder.name = convertView.findViewById(R.id.name_alt);
            holder.name_full = convertView.findViewById(R.id.name_full);
            holder.icon = convertView.findViewById(R.id.imageView);
            holder.price = convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        name_alt = getItem(position).getName().toLowerCase();
        holder.name.setText(name_alt);
        holder.name_full.setText(getItem(position).getName_full());

        //    =========   CREATE DATA FROM  OBJECTS

        ArrayList<Float> numbers;
        numbers = getItem(position).getPrice();
        ArrayList<Entry> dataVals_pos  = new ArrayList<>();
        ArrayList<Entry> dataVals_neg  = new ArrayList<>();
        numbers = new ArrayList<>(numbers.subList(numbers.size()-Math.min(numbers.size(),num_elements), numbers.size()));

        //   =========   BARCHART DATA

        boolean only_pos = true;
        boolean only_neg = true;

        for (int i = 0; i < numbers.size(); i++) {
            Float entry = numbers.get(i);
            if(entry < -80){
                entry = 0f;
            }
            if(entry > 0){
                dataVals_neg.add(new BarEntry((i * interval_size), 0));
                dataVals_pos.add(new BarEntry((i * interval_size), entry));
                only_neg = false;
            }else{
                dataVals_pos.add(new BarEntry((i * interval_size), 0));
                dataVals_neg.add(new BarEntry((i * interval_size), entry));
                only_pos = false;
            }
        }


        //   ============   POSITIVE DATA SET

        LineDataSet linedataset_pos = new LineDataSet(dataVals_pos,"dataset");
        linedataset_pos.setDrawValues(false);
        linedataset_pos.setDrawCircles(false);
        linedataset_pos.setColor(R.color.blue);
        linedataset_pos.setColor(mContext.getResources().getColor(R.color.blue));
        linedataset_pos.setLineWidth(0.5f);
        Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_blue);
        linedataset_pos.setFillDrawable(drawable);
        linedataset_pos.setDrawFilled(true);

        //   ============   NEGATIVE DATA SET

        LineDataSet linedataset_neg = new LineDataSet(dataVals_neg,"dataset");
        linedataset_neg.setDrawValues(false);
        linedataset_neg.setDrawCircles(false);
        linedataset_neg.setColor(R.color.red);
        linedataset_neg.setColor(mContext.getResources().getColor(R.color.red));
        linedataset_neg.setLineWidth(0.5f);

        drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_red);
        linedataset_neg.setFillDrawable(drawable);
        linedataset_neg.setDrawFilled(true);

        //   ============   ADD LINESETS TO DATASET

        ArrayList<ILineDataSet> dataSets_vol = new ArrayList<>();
        if(!only_neg){dataSets_vol.add(linedataset_pos);}
        if(!only_pos){dataSets_vol.add(linedataset_neg);}
        LineData data = new LineData(dataSets_vol);


        //   ============   SET IMAGE AND CURRENT PRICE


        if(name_alt.equals("1inch")){
            name_alt = "oneinch";
        }
        String uri = "@drawable/" + name_alt;
        int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
        Drawable res = ContextCompat.getDrawable(mContext,imageResource);
        holder.icon.setImageDrawable(res);


        for (int i = 0; i < last_prices.size(); i++) {
           if((getItem(position).getName() + "USDT").equals(last_prices.get(i).getName())){
               holder.price.setText(" " + last_prices.get(i).getPrice() + "$");
           }
        }

        //     ================  SET DATA

        holder.chart.setData(data);
        holder.chart.invalidate();
        if(isAnimate) {
            holder.chart.animateY(650);
        }

        //      ================  VISUAL

        Legend l_v =   holder.chart.getLegend();
        l_v.setEnabled(false);

        holder.chart.getDescription().setXOffset(10);
        holder.chart.getAxisLeft().setDrawLabels(false);
        holder.chart.setVisibleXRangeMaximum(visible_elements);
        holder.chart.moveViewToX(180);
        holder.chart.setScaleEnabled(false);
        holder.chart.setViewPortOffsets(10f, 10f, 100f, 10f);
        holder.chart.getAxisRight().setDrawGridLines(false);
        holder.chart.getAxisLeft().setEnabled(true);
        holder.chart.getAxisLeft().setLabelCount(0, true);
        holder.chart.getAxisLeft().setDrawZeroLine(true);
        holder.chart.getDescription().setEnabled(false);


        if(price_update) {
            if(saved_x.get(position) >=0) {
                holder.chart.moveViewToX(saved_x.get(position));
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
                saved_x.set(position, lowX);
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
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return list.get((int) value);
            }
        });

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0.5f);
        xAxis.setYOffset(-15);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawGridLinesBehindData(true);
        xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.gray_super_light));

        return convertView;
    }


    public int get_last_month_days(int month){
        month+=1;
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else if (month == 2) {
            return 28;
        } else {
            return 31;
        }
    }
}

























