package com.upperhand.cryptoterminal.objects;


public class breaking {

    private String text;
    private String time;


    public breaking(String text, String time) {
        this.text = text;
        this.time = time;
    }

    public String getText() {

        if(text != null && !text.isEmpty()) {
            return text;
        }else {
            return " ";
        }
    }

    public String gettime() {
        return time;
    }

}

