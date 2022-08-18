package com.upperhand.cryptoterminal.objects;


import java.util.ArrayList;

public class trend {


    private String name;
    private ArrayList<Integer> trend;
    private ArrayList<Integer> trend_vol;

    public trend(String name, ArrayList<Integer> trend, ArrayList<Integer> trend_vol) {
        this.name = name;
        this.trend = trend;
        this.trend_vol = trend_vol;

    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getNumbers() {
        return trend;
    }

    public ArrayList<Integer> getNumbers_vol() {
        return trend_vol;
    }

}

