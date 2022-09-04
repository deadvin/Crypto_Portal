package com.upperhand.cryptoterminal.objects;


public class tweet {

    private String text;
    private String link;
    private String source;
    private String time;
    private int fow;
    private float eng_score;
    private String likes;
    private String retweet;

    public tweet(String text, String source, int fow, String time, String link, String likes, String retweet,float eng_score) {
        this.text = text;
        this.link = link;
        this.source = source;
        this.time = time;
        this.fow = fow;
        this.likes = likes;
        this.retweet = retweet;
        this.eng_score = eng_score;
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

    public float get_eng_score() {
        return eng_score;
    }

    public String get_likes() {
        return likes;
    }

    public String get_retweets() {
        return retweet;
    }


}

