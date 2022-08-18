package com.upperhand.cryptoterminal.objects;


public class post {

    private String text;
    private String link;
    private String source;
    private String time;
    private int fow;
    private String followers;
    private String likes;
    private String retweet;

    public post(String text, String source, int fow, String time, String link, String likes, String retweet) {
        this.text = text;
        this.link = link;
        this.source = source;
        this.time = time;
        this.fow = fow;
        this.likes = likes;
        this.retweet = retweet;
    }

    public String getText() {

        if(text != null && !text .isEmpty()) {

            return text;
        }else {
            return " ";
        }

    }

    public String getLink() {
        if(link != null && !link .isEmpty()) {

            return link;
        }else {
            return " ";
        }
    }

    public String getSource() {
        return source;
    }

    public String gettime() {
        return time;
    }

    public int getfow() {
        return fow;
    }

    public String get_likes() {
        return likes;
    }

    public String get_retweets() {
        return retweet;
    }



}

