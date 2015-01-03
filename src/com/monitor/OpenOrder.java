package com.monitor;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderState;

/**
 * Created by nick on 1/1/15.
 */
public class OpenOrder {
    public int        orderId;
    public Contract   contract;
    public Order      order;
    public OrderState state;

    public OpenOrder (int orderId, Contract contract, Order order, OrderState state) {
        this.orderId = orderId;
        this.contract = contract;
        this.state = state;
        this.order = order;

    }

}
