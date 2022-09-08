package com.upperhand.cryptoterminal.objects;


import java.util.ArrayList;

public class div {

    private final String name;
    private final String full_name;
    private final ArrayList<Float> price;

    public div(String name,String full_name, ArrayList<Float> price) {
        this.name = name;
        this.full_name = full_name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return full_name;
    }

    public ArrayList<Float> getPrice() {
        return price;
    }

    public float getAverageDiv(){
        float total = 0;
        for(int i =  price.size()- 30; i < price.size(); i++) {
            if(price.get(i) > - 80) {
                total = total + price.get(i);
            }
        }
        return total;
    }

}

