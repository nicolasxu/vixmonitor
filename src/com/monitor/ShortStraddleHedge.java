package com.monitor;


import com.ib.client.Contract;
import com.ib.client.Order;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nick on 12/25/14.
 */
public class ShortStraddleHedge {
    public ApiHandler handler;
    public double upThreshold;
    public double downThreshold;
    public int futureLongShort = 0; // 1: long, -1 short
    public int interval = 0; // check interval in mikes
    public double upTolerance = 0; // after a order is filled, resulting from reaching up or down threshold,
                                   // the following order will not trigger if the price is within
                                   // upThreshold +   upTolerance and upThreshold -   downTolerance or,
                                   // downThreshold + upTolerance and downThreshold - downTolerance
    public double downTolerance = 0;

    public Contract contract;
    public Timer timer;

    public int hedgePositionSize = 1;

    public ILogger logger;



    public ShortStraddleHedge (double upThreshold, double downThreshold, int interval, int futureLongShort, ApiHandler handler) {
        this.upThreshold = upThreshold;
        this.downThreshold = downThreshold;

        this.interval = interval;
        this.futureLongShort = futureLongShort;

        // Set tolerance value
        this.upTolerance = 0.2;
        this.downTolerance = 0.2;

        if(handler == null) {
            this.handler = new ApiHandler();
        } else {
            this.handler = handler;
        }


        // Initialize the contract internally, 2015 Jan VIX Future
        this.contract = new Contract();

        this.contract.m_symbol = "VIX";
        this.contract.m_secType = "FUT";
        this.contract.m_expiry = "20150121";
        this.contract.m_localSymbol = "VXF5";
        this.contract.m_exchange = "CFE";
        this.contract.m_currency = "USD";
        this.contract.m_multiplier = "1000";
        this.contract.m_tradingClass = "VX";



        // give handler reference to hedge object, so that handler can update member variables in
        // hedge object
        this.handler.getHedgeRef(this);


    }

    public void assignLogger (ILogger logger) {
        this.logger = logger;
    }

    public void startHedge () {
        logger.log("- starting hedging with upThreshold " + this.upThreshold +
                " and downThreshold " + this.downThreshold + " at " +
                        this.interval + " mikes interval");

        logger.log("- Initial future position is: " + futureLongShort);

        this.handler.connect();

        // 1. build the TimerTask
        class DynamicHedgeTask extends TimerTask {
            int counter = 0;
            boolean receivingPriceCalled = false;
            public void run(){


                if(handler.getNextValidOrderId() > 0) {
                    // we are connected

                    handler.updateFutureLongShort();

                    if(receivingPriceCalled == false ) {
                        handler.startReceivingPrice(contract);
                        receivingPriceCalled = true;
                    }

                    double lastPrice = handler.getLastPrice();

                    logger.log(counter++ + " price: " + lastPrice +
                            " ,VIX future position: " + futureLongShort);

                    handler.outPutSentOrderStatus();

                    if(lastPrice > 0) {

                        if(lastPrice > upThreshold) {

                            if(futureLongShort > 0) {
                                // do nothing
                            }

                            if(futureLongShort == 0) {

                                // clear all pending order then buy, no matter they are sell or buy.
                                handler.cancelUnfilledOrders();
                                buy(1,lastPrice);
                            }

                            if(futureLongShort < 0) {

                                // unlikely, but it can happen when price jumps suddenly within interval
                                // from below downThreshold to above upThreshold
                                // TODO: clear all pending order, and then buy
                                handler.cancelUnfilledOrders();
                                buy(2, lastPrice);
                            }
                        }

                        if(lastPrice <= upThreshold && lastPrice >= downThreshold) {

                            if(futureLongShort == 0) {
                                // All good, do nothing
                            }

                            if(futureLongShort > 0 && lastPrice < upThreshold - downTolerance) {

                                handler.cancelUnfilledOrders();
                                sell(1, lastPrice);
                            }

                            if(futureLongShort < 0 && lastPrice > downThreshold + upTolerance) {

                                handler.cancelUnfilledOrders();
                                buy(1, lastPrice);
                            }

                        }

                        if(lastPrice < downThreshold) {

                            if(futureLongShort > 0) {
                                // unlikely

                                handler.cancelUnfilledOrders();
                                sell(2, lastPrice);

                            }

                            if(futureLongShort == 0) {

                                handler.cancelUnfilledOrders();
                                sell(1, lastPrice);


                            }

                            if(futureLongShort < 0) {
                                // do nothing

                            }
                        }
                    }


                } else {

                    logger.log("Not connected");
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

    public void getHistory() {
        this.handler.getHistory();
    }
}
