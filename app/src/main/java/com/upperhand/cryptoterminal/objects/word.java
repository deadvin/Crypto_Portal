package com.upperhand.cryptoterminal.objects;


import java.util.ArrayList;

public class word {


    private String name;
    private ArrayList<Integer> trend;
    private ArrayList<Integer> trend_vol;
    private ArrayList<String> words;
    private int trend_av;
    private int trend_vol_av;

    public word(String name, int trend_av, int trend_vol_av, ArrayList<Integer> trend, ArrayList<Integer> trend_vol, ArrayList<String> words) {
        this.name = name;
        this.trend = trend;
        this.trend_vol = trend_vol;
        this.words = words;
        this.trend_av = trend_av;
        this.trend_vol_av = trend_vol_av;

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

    public ArrayList<Integer> getNumbers_vol() {
        return trend_vol;
    }

    public int get_av(){
        return trend_av;
    }

    public int get_av_vol(){
        return trend_vol_av;
    }

    public float get_last_av(boolean vol) {

        int avg;
        if(!vol){
            avg = get_av();
        }else {
            avg = get_av_vol();
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

