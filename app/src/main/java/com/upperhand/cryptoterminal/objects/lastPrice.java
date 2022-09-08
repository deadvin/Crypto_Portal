package com.upperhand.cryptoterminal.objects;

public class lastPrice {

    private final String symbol;
    private final float price;

    public lastPrice(String symbol, float price) {
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

