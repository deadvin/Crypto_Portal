package com.example.cryptoterminal.main;


public class video {


    private String name;
    private String text;
    private String link;
    private String time;
    private int seen;



    public video(String name, String text, String link,String time, int seen) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.time = time;
        this.seen = seen;
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
        return seen;
    }



}

