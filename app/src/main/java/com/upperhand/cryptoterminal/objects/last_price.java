package com.upperhand.cryptoterminal.objects;


import java.util.ArrayList;

public class last_price {

    private String symbol;
    private float price;


    public last_price(String symbol, float price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getName() {
        return symbol;
    }
    public float getPrice() {
        return price;
    }


}

