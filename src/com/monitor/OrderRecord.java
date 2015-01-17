package com.monitor;


// For simulation only
/**
 * Created by nick on 1/7/15.
 */
public class OrderRecord {
    int futureLongShort;
    double price;
    double profitLoss;

    public OrderRecord(int futureLongShort, double price) {
        this.futureLongShort = futureLongShort;
        this.price = price;
        this.profitLoss = 0;

    }
}
