package com.example.cryptoterminal.main;


public class symbol {


    private String val1;
    private String val2;
    private String val3;
    private String val4;
    private String val5;
    private int alarm;
    private String name;


    public symbol(String name, String val1,String val2,String val3,String val4,String val5, int alarm) {
        this.name = name;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.val4 = val4;
        this.val5 = val5;
        this.alarm = alarm;
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

    public String getVal4() {
        return val4;
    }

    public String getVal5() { return val5; }

    public int getAlarm() {
        return alarm;
    }



}

