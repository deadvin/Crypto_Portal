package com.example.cryptoterminal.main;


public class video {


    private String name;
    private String text;
    private String link;
    private String time;
    private int alarm;



    public video(String name, String text, String link,String time, int alarm) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.time = time;
        this.alarm = alarm;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public String getTime() {
        return time;
    }

    public int getAlarm() {
        return alarm;
    }



}

