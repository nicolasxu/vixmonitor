package com.monitor;


import com.ib.client.Order;

/**
 * Created by nick on 1/1/15.
 */

public class SentOrder {
    public String     status;
    public int        remaining;
    public Order      order;
    boolean counted = false;

    public SentOrder(Order order, String status, int remaining) {
        this.order = order;
        this.status = status;
        this.remaining = remaining;

    }

    public boolean isCounted () {
        return this.counted;
    }

    public void setCounted () {
        this.counted = true;
    }

    public int getRemaining () {
        return this.remaining;
    }

    public int getTotal () {
        return this.order.m_totalQuantity;
    }


}
