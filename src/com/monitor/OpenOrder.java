package com.monitor;



/**
 * Created by nick on 1/1/15.
 */
//public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {

public class OpenOrder {
    public int        orderId;
    public String     status;
    public int        remaining;

    public OpenOrder (int orderId, String status, int remaining) {
        this.orderId = orderId;
        this.status = status;
        this.remaining = remaining;
    }
}
