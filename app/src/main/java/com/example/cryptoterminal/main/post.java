package com.example.cryptoterminal.main;


public class post {

    private String text;
    private String link;
    private String source;
    private String time;
    private int seen;
    private String followers;
    private String likes;
    private String retweet;

    public post(String text, String source, int seen, String time, String link, String likes, String retweet) {
        this.text = text;
        this.link = link;
        this.source = source;
        this.time = time;
        this.seen = seen;
        this.likes = likes;
        this.retweet = retweet;
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

    public String get_likes() {
        return likes;
    }

    public String get_retweets() {
        return retweet;
    }




}

