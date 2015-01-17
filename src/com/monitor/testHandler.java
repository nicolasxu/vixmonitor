package com.monitor;

import com.ib.client.Contract;
import com.ib.client.Order;

import java.io.*;
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

    @Override
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

    @Override
    public int getNextValidOrderId () {
        return m_simNextValidOrderId;
    }

    @Override
    public int placeOrder (Contract contract, Order order) {

        hedge.logger.log("placeOrder() - sim order placed");

        // just place and complete the order, no interaction with server
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


    @Override
    public void startReceivingPrice(Contract contract) {
        Integer p = 10;
        p++;
    }

    @Override
    public double getLastPrice() {

        if (priceIndex < m_prices.size()) {

            return m_prices.get(priceIndex++).price;

        } else {
            this.disConnect();
            return 0;
        }
    }


    @Override
    public void cancelUnfilledOrders() {
        int p = 10;
        p++;
    }

    @Override
    public void disConnect () {

        hedge.logger.log("disConnect() - sim handler disConnected");

        this.hedge.timer.cancel();

        for(OrderRecord record: m_orderRecords) {
            hedge.logger.log("position: " + record.futureLongShort + " price: " + record.price);
        }
        // Write the result file
        String outputFileName = "/Users/nick/documents/output.txt";

        // Calculate P&L
        for(int i = 0; i < m_orderRecords.size(); i ++) {
            if(m_orderRecords.get(i).futureLongShort == 0) {
                m_orderRecords.get(i).profitLoss = (m_orderRecords.get(i-1).futureLongShort - m_orderRecords.get(i).futureLongShort) *
                        (m_orderRecords.get(i).price - m_orderRecords.get(i-1).price);
            }
        }

        try {
            FileWriter fw = new FileWriter(outputFileName);
            BufferedWriter bw = new BufferedWriter(fw);

            for(OrderRecord record: m_orderRecords) {
                bw.write("position: " + record.futureLongShort + ", price: " + record.price +
                        (record.profitLoss != 0 ? " P&L: " + record.profitLoss : "") + "\n");
            }

            bw.close();

        }
        catch (IOException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void getHistory () {
        hedge.logger.log("getHistory() overwride - do nothing");
    }

    //     this.handler.getHedgeRef(this);  // no need to change

    //         handler.getNextValidOrderId()                done
    //         handler.placeOrder(this.contract, buyOrder); done
    //         handler.getSentOrderSize()                   done
    //         handler.startReceivingPrice(contract);       done
    //         lastPrice = handler.getLastPrice();          done

    //         handler.cancelUnfilledOrders();                   done
    //         handler.m_openOrderEnd = false;              done
    //         handler.getContractOpenOrders();             done
    //         this.handler.disConnect();                   done



}
