package com.example.cryptoterminal.main;


public class post {

    private String text;
    private String link;
    private String source;
    private String time;
    private int seen;
    private String followers;

    public post(String text, String source, int seen, String time, String link, String followers) {
        this.text = text;
        this.link = link;
        this.source = source;
        this.time = time;
        this.seen = seen;
        this.followers = followers;
    }

    public String getText() {
        return text;
    }

    public String getLink() {
        return link;
    }

    public String getSource() {
        return source;
    }

    public String gettime() {
        return time;
    }

    public int getseen() {
        return seen;
    }

    public String getfollow() {
        return followers;
    }




}
