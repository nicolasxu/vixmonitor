package com.monitor;

import com.ib.client.Contract;
import com.ib.client.Order;

import java.util.ArrayList;

/**
 * Created by nick on 1/6/15.
 */
public class TestHandler extends ApiHandler{

    int m_simNextValidOrderId;

    ArrayList<OrderRecord> m_orderRecords;

    ArrayList<Integer> m_prices; // this variable holds price loaded from file

    int priceIndex;

    public TestHandler() {
        m_simNextValidOrderId = 1;
        m_orderRecords = new ArrayList<OrderRecord>();
        m_prices = new ArrayList<Integer>();
        priceIndex = 0;
    }

    public void connect() {

        // TODO: implement file loading

        hedge.logger.log("connect() -  loading data from file...");

    }



    public int getNextValidOrderId () {
        return m_simNextValidOrderId;
    }

    public int placeOrder (Contract contract, Order order) {

        hedge.logger.log("placeOrder() - sim order placed");

        // just complete the order
        if(order.m_action.equals("BUY")) {

            hedge.futureLongShort++;

            m_orderRecords.add(new OrderRecord(hedge.futureLongShort, order.m_lmtPrice));

        }

        if(order.m_action.equals("SELL")) {

            hedge.futureLongShort--;

            m_orderRecords.add(new OrderRecord(hedge.futureLongShort, order.m_lmtPrice));

        }

        return this.m_simNextValidOrderId++;
    }

    public int getOpenOrderSize () {
        return 0;
    }

    public void startReceivingPrice(Contract contract) {
        Integer p = 10;
        p++;
    }

    public double getLastPrice() {

        return m_prices.get(priceIndex++);
    }


    public void cancelAllOrders () {
        int p = 10;
        p++;
    }
    public void getContractOpenOrders () {

        m_openOrderEnd = true;
    }

    public void disConnect () {
        hedge.logger.log("disConnect() - sim handler disConnected");
    }






        //     this.handler.getHedgeRef(this);  // no need to change

    //         handler.getNextValidOrderId()                done
    //         handler.placeOrder(this.contract, buyOrder); done
    //         handler.getOpenOrderSize()                   done
    //         handler.startReceivingPrice(contract);       done
    //         lastPrice = handler.getLastPrice();          done

    //         handler.cancelAllOrders();                   done
    //         handler.m_openOrderEnd = false;              done
    //         handler.getContractOpenOrders();             done
    //         this.handler.disConnect();                   done



}
