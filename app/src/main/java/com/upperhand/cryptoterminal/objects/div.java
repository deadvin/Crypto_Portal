package com.upperhand.cryptoterminal.objects;


import java.util.ArrayList;

public class div {

    private String name;
    private String full_name;
    private ArrayList<Float> price;

    public div(String name,String full_name, ArrayList<Float> price, int price_av) {
        this.name = name;
        this.full_name = full_name;
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public String getName_full() {
        return full_name;
    }

    public ArrayList<Float> getPrice() {
        return price;
    }

    public float get_av_div(){

        float total = 0;

        for(int i =  price.size()- 30; i < price.size(); i++) {
            if(price.get(i) > - 80) {
                total = total + price.get(i);
            }
        }

        return total;
    }

}

