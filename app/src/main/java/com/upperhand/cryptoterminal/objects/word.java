package com.upperhand.cryptoterminal.objects;


import android.util.Log;

import java.util.ArrayList;

public class word {

    private final String name;
    private final ArrayList<Integer> trend;
    private final ArrayList<String> words;
    private final int trend_av;

    public word(String name, ArrayList<Integer> trend, ArrayList<String> words, int trend_av) {

        this.name = name;
        this.trend = trend;
        this.words = words;
        this.trend_av = trend_av;
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

    public int getAv(){
        return trend_av;
    }

    public float getLastAv() {

        int avg;
        avg = getAv();
        float total = 0;
        float entry;

        for(int i = trend.size()- 10; i < trend.size(); i++) {

            entry = ((float) (trend.get(i))/avg);
            total = total + entry;
        }
        return total;
    }

}

