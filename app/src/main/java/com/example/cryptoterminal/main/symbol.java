package com.example.cryptoterminal.main;


public class symbol {


    private String val1;
    private String val2;
    private String val3;
    private int seen;
    private String name;


    public symbol(String name, String val1,String val2,String val3, int seen) {
        this.name = name;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.seen = seen;
    }

    public String getName() {
        return name;
    }

    public String getVal1() {
        return val1;
    }

    public String getVal2() {
        return val2;
    }

    public String getVal3() {
        return val3;
    }

    public int getAlarm() {
        return seen;
    }



}

