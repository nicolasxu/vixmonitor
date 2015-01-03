package com.monitor;


import com.ib.client.Contract;
import com.ib.client.Order;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nick on 12/25/14.
 */
public class ShortStraddleHedge {
    public ApiHandler handler;
    public double upThreshold;
    public double downThreshold;
    public double strikePrice;
    public int futureLongShort = 0; // 1: long, -1 short
    public int interval = 0; // check interval in mikes

    public Contract contract;
    public Timer timer;

    public JTextArea logTextArea;



    public ShortStraddleHedge (double upThreshold, double downThreshold, int interval, int futureLongShort) {
        this.upThreshold = upThreshold;
        this.downThreshold = downThreshold;

        this.interval = interval;
        this.futureLongShort = futureLongShort;
        this.handler = new ApiHandler();

        // Initialize the contract internally, 2015 Jan VIX Future
        this.contract = new Contract();
//        this.contract.m_symbol = "VXF5"; // 2015 JAN VIX FUTURE, EXPIRED AT JAN 15
        this.contract.m_symbol = "VIX";
        this.contract.m_secType = "FUT";
        this.contract.m_expiry = "20150121";
        this.contract.m_localSymbol = "VXF5";
        this.contract.m_exchange = "CFE";
        this.contract.m_currency = "USD";
        this.contract.m_multiplier = "1000";
        this.contract.m_tradingClass = "VX";
        this.handler.getHedgeRef(this);


    }

    public void assignLogger (JTextArea ta) {
        this.logTextArea = ta;
    }

    public void startHedge () {

//        System.out.println("starting hedging...");
//
//        System.out.println("upThreshold is: " + this.upThreshold);
//        System.out.println("downThreshold is: " + this.downThreshold);
//        System.out.println("interval is: " + this.interval);

        logTextArea.append("- starting hedging with upThreshold " + this.upThreshold + " and downThreshold " + this.downThreshold + " at " +
        this.interval + " mikes interval \n");

        logTextArea.append("- Initial future position is: " + futureLongShort);



        this.handler.connect();

        // 1. buid the TimerTask
        class DynamicHedgeTask extends TimerTask {
            int counter = 0;
            boolean receivingPriceFlag = false;
            public void run(){


                if(handler.getNextValidOrderId() > 0) {
                    // we are connected
                    System.out.println("Checking price... " + counter++);

                    System.out.println("handler.m_openOrders.size(): " + handler.m_openOrders.size());

                    if(receivingPriceFlag == false ) {
                        handler.startReceivingPrice(contract);
                        receivingPriceFlag = true;
                    }

                    double lastPrice = handler.getlastPrice();
//                    System.out.println("The current price is: " + lastPrice);
                    logTextArea.append("The current price is: " + lastPrice + "\n");
                    if(lastPrice > 0) {
                        // last price is ready
                        if (lastPrice > upThreshold) {
                            // buy
                            if(futureLongShort == 0) {
                                // buy
                            }

                            if(futureLongShort < 0 ) {
                                // close short position
                                // buy
                            }

                            if(futureLongShort > 0) {
                                // do nothing
                            }
                        }

                        if (lastPrice < downThreshold) {
                            // sell
                            if(futureLongShort == 0) {
                                // sell
                            }

                            if(futureLongShort > 0) {
                                // close long position
                                // sell
                            }

                            if(futureLongShort < 0) {
                                // do nothing
                            }
                        }

                        // handler.sendOrder(contract, order);
                    }


                } else {

//                    System.out.println("Not connected");
                    logTextArea.append("Not connected \n");
                }


            }
        }
        TimerTask task = new DynamicHedgeTask();
        this.timer = new Timer();

        // 2. start the timer
        this.timer.schedule(task,1000, 6000);

    }

    public int buy(int quantity, double price) {

        Order buyOrder = new Order();
        buyOrder.m_orderType = "LMT";
        buyOrder.m_totalQuantity = quantity;
        buyOrder.m_lmtPrice = price;
        buyOrder.m_action = "BUY";

        return this.handler.placeOrder(this.contract, buyOrder);

    }

    public int sell(int quantity, double price) {

        Order sellOrder = new Order();
        sellOrder.m_orderType = "LMT";
        sellOrder.m_totalQuantity = quantity;
        sellOrder.m_lmtPrice = price;
        sellOrder.m_action = "SELL";

        return this.handler.placeOrder(this.contract, sellOrder);
    }

    public void stopHedge () {

        System.out.println("stop hedging");
        // 1. Cancel the timer
        this.timer.cancel();

        this.handler.disConnect();
    }
}
