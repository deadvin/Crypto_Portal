package com.example.cryptoterminal.main;


public class event {

    private String sym;
    private String name;
    private String date;
    private String text;
    private String time;
    private String link;
    private int seen;


    public event(String sym,String name,String date, String text, String time, String link, int seen) {
        this.text = text;
        this.link = link;
        this.date = date;
        this.sym = sym;
        this.name = name;
        this.time = time;
        this.seen = seen;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getSym() {
        return sym;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getseen() {
        return seen;
    }






}

