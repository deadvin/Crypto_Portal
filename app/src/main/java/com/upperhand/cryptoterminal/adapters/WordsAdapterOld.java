//package com.upperhand.cryptoterminal.adapters;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.formatter.ValueFormatter;
//import com.github.mikephil.charting.utils.ColorTemplate;
//import com.upperhand.cryptoterminal.R;
//import com.upperhand.cryptoterminal.objects.word;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//
//
//public class WordsAdapterOld extends RecyclerView.Adapter<WordsAdapterOld.ViewHolder> {
//
//    Context mContext;
//    public boolean isVolume;
//    int numberElements;
//    int count;
//    ArrayList<String> listHours;
//    ArrayList<word> wordsList;
//
//    public WordsAdapterOld(Context context, ArrayList<word> wordsList, boolean volume) {
//        this.mContext = context;
//        this.isVolume = volume;
//        this.wordsList = wordsList;
//    }
//
//    @Override
//    public int getItemCount() {
//        return wordsList.size();
//    }
//
//    private void createHoursArray(){
//
//        Calendar calendar = Calendar.getInstance();
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//        int mins = calendar.get(Calendar.MINUTE);
//        boolean half;
//        numberElements = 101;
//        count = 50;
//
//        listHours = new ArrayList<String>();
//        if (mins > 30){
//            listHours.add(hours + ":30");
//            half = true;
//            if(mins < 45) {
//                numberElements = 103;
//                count = 51;
//            }else {
//                numberElements = 102;
//                count = 50;
//            }
//        }else {
//            listHours.add(hours + ":00");
//            half = false;
//            hours -=1;
//            if(mins >= 15) {
//                numberElements = 102;
//            }
//        }
//
//        for (int i = 0; i < count; i++) {
//            if(half){
//                half = false;
//                listHours.add(hours + ":00");
//                hours -=1;
//            }else{
//                half = true;
//                listHours.add(hours + ":30");
//            }
//            if(hours < 0){
//                hours = 23;
//            }
//        }
//        Collections.reverse(listHours);
//    }
//
//    @NotNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.layout_words, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
//
//        createHoursArray();
//
//        int average;
//        float scale;
//        ArrayList<Integer> numbers;
//        if(!isVolume) {
//            numbers = wordsList.get(position).getNumbers();
//            average =  wordsList.get(position).getAv();
//            scale = 0.8f;
//        }else {
//            numbers = wordsList.get(position).getNumbersVol();
//            average = wordsList.get(position).getAvVol();
//            scale = 0.2f;
//        }
//        ArrayList<String> words = wordsList.get(position).getWords();
//        numbers = new ArrayList<>(numbers.subList(numbers.size()-Math.min(numbers.size(),numberElements), numbers.size()));
//
//        //   ============  CREATE BARDATASET
//
//        ArrayList data_list = new ArrayList();
//        for (int i = 0; i < numbers.size(); i++) {
//            int entry = numbers.get(i) - (int)(average * scale);
//            if (entry < 0){
//                entry = 0;
//            }
//
//            data_list.add(new BarEntry((float)(i*0.5),entry));
//        }
//
//        BarDataSet bardataset = new BarDataSet(data_list, "");
//        BarData data = new BarData(bardataset);
//        bardataset.setDrawValues(false);
//        bardataset.setColors(ColorTemplate.PASTEL_COLORS);
//        data.setBarWidth(0.5f);
//
//        int startColor = ContextCompat.getColor(mContext, R.color.black70);
//        int endColor = ContextCompat.getColor(mContext, R.color.blue);
//        bardataset.setGradientColor(startColor, endColor);
//
//        // ============ SETTING KEYWORDS TEXTVIEWS
//
//        int size = words.size();
//        try {
//            holder.name1.setText(words.get(0));
//            holder.name2.setText(words.get(1));
//            if(size > 2){
//                holder.name3.setVisibility(View.VISIBLE);
//                holder.name3.setText(words.get(2));
//            }else {
//                holder.name3.setVisibility(View.GONE);
//            }
//            if(size > 3){
//                holder.name4.setVisibility(View.VISIBLE);
//                holder.name4.setText(words.get(3));
//            }else {
//                holder.name4.setVisibility(View.GONE);
//            }
//        }catch (Exception e){
//        }
//
//        //=================   VISUAL
//
//        holder.chart.animateY(500);
//        holder.chart.setData(data);
//
//        Legend l =  holder.chart.getLegend();
//        l.setEnabled(false);
//        holder.chart.getAxisLeft().setDrawLabels(false);
//        holder.chart.getAxisRight().setDrawLabels(false);
//        holder.chart.getDescription().setEnabled(false);
//        holder.chart.setVisibleXRangeMaximum(15);
//        holder.chart.getAxisRight().setDrawGridLines(false);
//        holder.chart.getAxisLeft().setDrawGridLines(false);
//        holder.chart.setViewPortOffsets(0f, 0f, 0f, -20f);
//        holder.chart.moveViewToX(180);
//        holder.chart.setExtraRightOffset(30);
//        holder.chart.setScaleEnabled(false);
//
//        //=================   SET HOURS ON TOP
//
//        XAxis xAxis = holder.chart.getXAxis();
//        try {
//                 xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                return listHours.get((int) value);
//            }
//        });
//        }catch (Exception e){
//        }
//
//
//
//        xAxis.setAxisMinimum(0.5f);
//        xAxis.setYOffset(-15);
//        xAxis.setDrawGridLines(true);
//        xAxis.setDrawGridLinesBehindData(true);
//        xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.gray_super_light));
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView name1;
//        TextView name2;
//        TextView name3;
//        TextView name4;
//        BarChart chart;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            chart = itemView.findViewById(R.id.chart1);
//            name1 = itemView.findViewById(R.id.name1);
//            name2 = itemView.findViewById(R.id.name2);
//            name3 = itemView.findViewById(R.id.name3);
//            name4 = itemView.findViewById(R.id.name4);
//        }
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
