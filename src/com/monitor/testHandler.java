package com.monitor;

import com.ib.client.Contract;
import com.ib.client.Order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nick on 1/6/15.
 */
public class TestHandler extends ApiHandler{

    int m_simNextValidOrderId;

    String m_fileName;

    ArrayList<OrderRecord> m_orderRecords; // holds executed orders for calculating P&L

    ArrayList<PriceRecord> m_prices; // this variable holds price and time loaded from file

    int priceIndex;

    public class PriceRecord {
        int timeStamp;
        double price;
        public PriceRecord(int timeStamp, double price) {
            this.timeStamp = timeStamp;
            this.price = price;
        }
    }

    public TestHandler() {
        m_simNextValidOrderId = 1;
        m_orderRecords = new ArrayList<OrderRecord>();
        m_prices = new ArrayList<PriceRecord>();
        priceIndex = 0;
        m_fileName = "/Users/nick/documents/testFile.txt";
    }

    public void connect() {

        // TODO: implement file loading
        try(BufferedReader br = new BufferedReader(new FileReader(this.m_fileName))) {
            String sCurrentLine;
            String[] results;
            while((sCurrentLine = br.readLine()) != null ) {

                results = sCurrentLine.split(",");

                m_prices.add(new PriceRecord(Integer.parseInt(results[0]), Double.parseDouble(results[1])));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        hedge.logger.log("connect() -  data loaded from file...");


        for(int i = 0; i < m_prices.size(); i++) {
            hedge.logger.log("[" + i + "] " + m_prices.get(i).timeStamp + ": " + m_prices.get(i).price);

        }

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

        return m_prices.get(priceIndex++).price;
    }



    public void cancelAllOrders () {
        int p = 10;
        p++;
    }
    public void getContractOpenOrders () {

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
