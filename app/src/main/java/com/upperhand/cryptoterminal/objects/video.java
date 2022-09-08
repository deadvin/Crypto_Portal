package com.upperhand.cryptoterminal.objects;


public class video{

    private String name;
    private String text;
    private String link;
    private String time;

    public video(String name, String text, String link,String time) {
        this.name = name;
        this.text = text;
        this.link = link;
        this.time = time;
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

}

