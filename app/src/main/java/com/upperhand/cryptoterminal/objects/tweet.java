package com.upperhand.cryptoterminal.objects;


public class tweet {

    private final String text;
    private final String link;
    private final String source;
    private final String time;
    private final int fow;
    private final float eng_score;
    private final String likes;
    private final String retweet;

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

        if(text != null && !text.isEmpty()) {
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

    public float getEngScore() {
        return eng_score;
    }

    public String getLikes() {
        return likes;
    }

    public String getRetweets() {
        return retweet;
    }


}

