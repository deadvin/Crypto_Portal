package com.upperhand.cryptoterminal.objects;


public class symbol {


    private float now;
    private float vol;
    private float a_15m;
    private float a_4h;
    private float a_10h;
    private String name;


    public symbol(String name, float now , float a_15m,float a_4h,float a_10h, float vol) {

        this.name = name;
        this.vol = vol;
        this.a_15m = a_15m;
        this.a_4h = a_4h;
        this.a_10h = a_10h;
        this.now = now;
    }

    public String getName() {
        return name;
    }

    public float getvol() {
        return vol;
    }

    public float get15m() {
        return a_15m;
    }

    public float get4h() {
        return a_4h;
    }

    public float get10h() {
        return a_10h;
    }

    public float getNow() {
        return now;
    }



}

