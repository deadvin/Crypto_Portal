package com.upperhand.cryptoterminal.objects;


import android.util.Log;

import java.util.ArrayList;

public class word {

    private final String name;
    private final ArrayList<Integer> trend;
    private ArrayList<Integer> trend_vol;
    private final ArrayList<String> words;
    private final int trendAverage;
    private final int trendVolAverage;

    public word(String name, ArrayList<Integer> trend, ArrayList<Integer> trend_vol, ArrayList<String> words, int trend_av, int trend_vol_av) {

        this.name = name;
        this.trend = trend;
        this.trend_vol = trend_vol;
        this.words = words;
        this.trendAverage = trend_av;
        this.trendVolAverage = trend_vol_av;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getNumbers() {
        return trend;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public ArrayList<Integer> getNumbersVol() {
        return trend_vol;
    }

    public int getAv(){
        return trendAverage;
    }

    public int getAvVol(){
        return trendVolAverage;
    }

    public float getLastAv(boolean vol) {

        int avg;
        if(!vol){
            avg = getAv();
        }else {
            avg = getAvVol();
        }
        float total = 0;
        float entry;

        for(int i =  trend.size()- 10; i < trend.size(); i++) {
            if(vol){
                entry = ((float) (trend.get(i))-avg*0.2f);
                if(entry < 0){entry = 0;}
            }else {
                entry = ((float) (trend.get(i))/avg);
            }
            total = total + entry;
        }
        return total;
    }

}

