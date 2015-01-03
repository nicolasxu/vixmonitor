/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitor;

import com.ib.client.*;

import java.util.*;


/**
 *
 * @author nick
 */
public class ApiHandler implements EWrapper{

    public String m_accountString;

    public int m_clientId;

    int m_nextValidOrderId;

    int m_reqId;

    boolean m_orderCompleted;

    int m_isFrozenData;

    double m_lastPrice;

    double m_accountValue;

    ArrayList<Position> m_positions;

    ArrayList<OpenOrder> m_openOrders;

    public EClientSocket m_socket;

    public ShortStraddleHedge hedge;

    public ApiHandler() {

        this.m_clientId = 123;
        this.m_socket = new EClientSocket(this);
        this.m_orderCompleted = true; // If submitted order is completed or not. Default is true.
        this.m_reqId = 20034;
        this.m_lastPrice = 0; // if m_lastPrice > 0, then we can use the price for calculation.
        this.m_accountValue = 0;
        this.m_positions = new ArrayList<Position>();
        this.m_openOrders = new ArrayList<OpenOrder>();


    }
    public void getHedgeRef (ShortStraddleHedge hedge) {
        this.hedge = hedge;
    }

    public void connect() {

        this.m_socket.eConnect(null, 7496, this.m_clientId);
    }

    public void disConnect () {

        this.m_socket.eDisconnect();
        this.hedge.logTextArea.append("disConnected... \n");
    }


    public void startReceivingPrice(Contract contract) {

        this.m_socket.reqMarketDataType(2); // receive frozen data

        this.m_socket.reqMktData(this.m_reqId++, contract, "" , false, Collections.<TagValue>emptyList());

    }

    public void getPositions() {

        // test
        System.out.println("this.hedge.futureLongShort in getlastPrice " + this.hedge.futureLongShort);

        this.m_positions.clear();
        this.m_socket.reqAccountUpdates(true, this.m_accountString);

    }

    public void getContractOpenOrders () {
        this.m_openOrders.clear();
        this.m_socket.reqOpenOrders();
    }

    public int getNextValidOrderId () {
        return this.m_nextValidOrderId;
    }

    public double getlastPrice () {

        return this.m_lastPrice;
    }

    public int placeOrder (Contract contract, Order order) {

//        System.out.println("ApiHandler - testOrder()");

        this.m_socket.placeOrder(this.m_nextValidOrderId, contract, order);

        Calendar c = new GregorianCalendar();

        this.hedge.logTextArea.append(String.format("%tT", c.getInstance()) + " order placed: " + order.m_action + " " + order.m_totalQuantity + " " + contract.m_localSymbol + " " + contract.m_expiry + " \n");

        return this.m_nextValidOrderId++;

    }

    // -------------- Connection and Server ------------------- //

    @Override
    // This method is called when TWS closes the sockets connection, or when TWS is shut down.
    public void connectionClosed() {
        System.out.println("connectionClosed");

    }

    @Override
    // This method receives the current system time on the server side.
    public void currentTime(long time) {
        throw new UnsupportedOperationException("currentTime - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    //This method is called when there is an error with the communication or when TWS wants to send a message to the client.    
    public void error(Exception e) {
        System.out.print(e);
        // The exception that occurred

    }

    @Override
    public void error(String str) {
        // This is the textual description of the error
        throw new UnsupportedOperationException("error - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void error(int id, int errorCode, String errorMsg) {
        //id            int 	This is the orderId or tickerId of the request that generated the error.
        //errorCode 	int 	For information on error codes, see Error Codes.
        //errorString 	String 	The textual description of the error.    
        System.out.println("error in connection: " + id + " | errorCode: " + errorCode + " | errorMsg: " + errorMsg);

    }







    // -------------- Market and Data ------------------- //


    @Override
    // This method is called when the market data changes. Prices are updated immediately with no delay.
    public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {




        //        1 = bid
        //        2 = ask
        //        4 = last
        //        6 = high
        //        7 = low
        //        9 = close

//        TickType.getField(field).equals("last")
        if(field == 4) {

            this.m_lastPrice = price;

            this.hedge.logTextArea.append("current Price is: " + price + "\n");

            System.out.println("we got last price: " + field);
            System.out.println("tickPrice() - last price is: " + price);



        }



    }

    @Override
    // This method is called when the market data changes. Sizes are updated immediately with no delay.
    public void tickSize(int tickerId, int field, int size) {
        //tickerId 	int 	The ticker Id that was specified previously in the call to reqMktData()
        //field 	int 	
        //
        //                        Specifies the type of price.
        //
        //                        Pass the field value into TickType.getField(int tickType) to retrieve the field description.
        //                        For example, a field value of 0 will map to bidSize, a field value of 3 will map to askSize, etc.
        //                    
        //                            0 = bid size
        //                            3 = ask size
        //                            5 = last size
        //                            8 = volume
        //
        //size 	int 	Specifies the size for the specified field
        //       System.out.println("tickSize() - Type: "  + TickType.getField(field) );
        //        System.out.println("tickSize() - tick size is: " + size);

        // Totally discard tickSize value for now.

    }

    @Override
    // This method is called when the market in an option or its underlier moves. TWS’s option model volatilities, prices, and deltas, along with the present value of dividends expected on that options underlier are received.
    public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        // field    int     
        //          Specifies the type of option computation.
        //          Pass the field value into TickType.getField(int tickType) to retrieve the field description. For example, a field value of 13 will map to modelOptComp, etc.

        // 10 = Bid
        // 11 = Ask
        // 12 = Last

        throw new UnsupportedOperationException("tickOptionComputation - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // This method is called when the market data changes. Values are updated immediately with no delay.
    public void tickGeneric(int tickerId, int tickType, double value) {
        // tickerId     int     The ticker Id that was specified previously in the call to reqMktData()
        // tickType     int     Specifies the type of price.
        //                      Pass the field value into TickType.getField(int tickType) to retrieve the field description.  For example, a field value of 46 will map to shortable, etc.
        // value        double  The value of the specified field
        System.out.println("tickGeneric() - typs is: " + TickType.getField(tickType));
        System.out.println("tickGeneric() - value is: " + value);
    }

    @Override
    // This method is called when the market data changes. Values are updated immediately with no delay.
    public void tickString(int tickerId, int tickType, String value) {
        // tickerId    int     The ticker Id that was specified previously in the call to reqMktData()
        // field       int     Specifies the type of price.
        //                     Pass the field value into TickType.getField(int tickType) to retrieve the field description.  For example, a field value of 45 will map to lastTimestamp, etc.
        // value       String  The value of the specified field

        System.out.println("tickString() - type: " + TickType.getField(tickType));

        System.out.println("tickString() - value: " + value);

    }

    @Override
    // This method is called when the market data changes. Values are updated immediately with no delay.
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureExpiry, double dividendImpact, double dividendsToExpiry) {
        // basisPoints     double  Annualized basis points, which is representative of the financing rate that can be directly compared to broker rates
        // impliedFuture   double  Implied futures price
        // holdDays        int     The number of hold days until the expiry of the EFP
        throw new UnsupportedOperationException("tickEFP - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // This is called when a snapshot market data subscription has been fully handled and there is nothing more to wait for. This also covers the timeout case.
    public void tickSnapshotEnd(int reqId) {
        throw new UnsupportedOperationException("tickSnapshotEnd - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    // TWS sends a marketDataType(type) callback to the API, where type is set to Frozen or RealTime, to announce that market data has been switched 
    // between frozen and real-time. This notification occurs only when market data switches between real-time and frozen. The marketDataType( ) callback 
    // accepts a reqId parameter and is sent per every subscription because different contracts can generally trade on a different schedule.
    public void marketDataType(int reqId, int marketDataType) {
        // int reqId        int     Id of the data request
        // marketDataType   int     1 for real-time streaming market data or 2 for frozen market data..

        this.m_isFrozenData = marketDataType;

        if(marketDataType == 2) {
            System.out.println("Receiving frozen data");
        } else {
            System.out.println("Receiving live data");

        }


    }











    // -------------- Orders ------------------- //


    @Override
    // This method is called whenever the status of an order changes. It is also fired after reconnecting to TWS if the client has any open orders.
    public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
        // Note:  It is possible that orderStatus() may return duplicate messages. It is essential that you filter the message accordingly.
        // 
        // orderId     int      The order Id that was specified previously in the call to placeOrder()
        // status      String   NOTE: This order status is not sent by TWS and should be explicitly set by the API developer when an order is submitted.
        //                      PendingSubmit
        //                      PendingCancel
        //                      PreSubmitted
        //                      Submitted
        //                      Cancelled
        //                      Filled
        //                      Inactive
        // filled       int     Specifies the number of shares that have been executed.
        // remaining    int     Specifies the number of shares still outstanding.
        // avgFillPrice     double  The average price of the shares that have been executed. This parameter is valid only if the filled 
        //                      parameter value is greater than zero. Otherwise, the price parameter will be zero.
        // permId   int     The TWS id used to identify orders. Remains the same over TWS sessions.
        // parentId     int     The order ID of the parent order, used for bracket and auto trailing stop orders.
        // lastFilledPrice  double  The last price of the shares that have been executed. This parameter is valid only if 
        //                      the filled parameter value is greater than zero. Otherwise, the price parameter will be zero.
        // clientId     int     The ID of the client (or TWS) that placed the order. Note that TWS orders have a fixed clientId and 
        //                      orderId of 0 that distinguishes them from API orders.
        // whyHeld  String      This field is used to identify an order held when TWS is trying to locate shares for a short sell. The value used to indicate this is 'locate'.

        OpenOrder orderToRemove = null;
        for(OpenOrder oo: this.m_openOrders ) {

            if (oo.orderId == orderId) {

                if(status.equals("Filled")) {
                    // complete order processing is done at execute order detail handler

                }

                if(status.equals("Cancelled")) {

//                    m_openOrders.remove(oo);
                    orderToRemove = oo;
//                    m_openOrders.iterator().remove();
                    System.out.println("orderStatus() - order cancelled due to user");

                    System.out.println("m_openOrders.size() is: " + m_openOrders.size());

                }
            }
        }

        if(orderToRemove != null) {
            m_openOrders.remove(orderToRemove);
        }



    }

    @Override
    // This method is called to feed in open orders.
    // 
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        // orderId     int     The order Id assigned by TWS. Used to cancel or update the order.
        // contract    Contract    The Contract class attributes describe the contract.
        // order   Order   The Order class attributes define the details of the order.
        // orderState  OrderState  The orderState attributes include margin and commissions fields for both pre and post trade data.



        // if same contract, if same if clientId same, then put it into m_openOrders

        if(hedge.contract.m_secType.equals(contract.m_secType) &&
                hedge.contract.m_symbol.equals(contract.m_symbol) &&
                (order.m_clientId == this.m_clientId) ) {

            boolean exist = false;
            for(OpenOrder oo: m_openOrders) {
                if(oo.orderId == orderId) {

                    exist = true;
                    System.out.println("order " + orderId + " exists");



                }
            }

            if(!exist && orderState.m_status.equals("Submitted")) {

                OpenOrder oo2 = new OpenOrder(orderId, contract, order, orderState);

                this.m_openOrders.add(oo2);

                System.out.println("openOrder() - Open order is added: " + oo2.orderId);

            }
        }
    }

    @Override
    // This is called at the end of a given request for open orders.
    public void openOrderEnd() {

        System.out.println("openOrderEnd() - triggered");

    }



    @Override
    //  This method is called after a successful connection to TWS.
    public void nextValidId(int orderId) {

        // Starter point of the program
        System.out.println("nextValidId() - next available orderId is: " + orderId);
        this.m_nextValidOrderId = orderId;
        this.getPositions();
        this.getContractOpenOrders();



        // orderId     int     The next available order Id received from TWS upon connection. Increment all successive orders by one based on this Id.        
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // Upon accepting a Delta-Neutral RFQ(request for quote), the server sends a deltaNeutralValidation() message with the UnderComp structure. If the 
    // delta and price fields are empty in the original request, the confirmation will contain the current values from the server. These values are 
    // locked when the RFQ is processed and remain locked until the RFQ is canceled.
    public void deltaNeutralValidation(int reqId, UnderComp underComp) {
        // reqID       int         The Id of the data request.
        // underComp   UnderComp   Underlying component.        
        throw new UnsupportedOperationException("deltaNeutralValidation - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }















    // -------------- Account and Portfolio ------------------- //



    @Override
    // This method is called only when reqAccountUpdates() method on the EClientSocket object has been called.
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        // key      String  
        //                A string that indicates one type of account value. There is a long list of possible keys that can be sent, here are just a few examples:
        //                CashBalance - account cash balance
        //                DayTradesRemaining - number of day trades left
        //                EquityWithLoanValue - equity with Loan Value
        //                InitMarginReq - current initial margin requirement
        //                MaintMarginReq - current maintenance margin
        //                NetLiquidation - net liquidation value
        // value          String  The value associated with the key.
        // currency     String  Defines the currency type, in case the value is a currency type.
        // account  String  States the account the message applies to. Useful for Financial Advisor sub-account messages.

        if(key.equals("NetLiquidation")) {
            this.m_accountValue = Double.parseDouble(value);
            System.out.println("updateAccountValue() - account NetLiquidation value is: " + value);
        }
    }

    @Override
    // This method is called only when reqAccountUpdates() method on the EClientSocket object has been called.
    public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
        System.out.println("updatePortfolio() - contract is: ");
        System.out.println(contract.m_secType);
        System.out.println(contract.m_symbol);
        System.out.println(contract.m_localSymbol);
        System.out.println(contract.m_strike);
        System.out.println("updatePortfolio() - position is: " + position);
        System.out.println("updatePortfolio() - marketPrice is: " + marketPrice);

//        Position(Contract c, double marketPrice, double marketValue,
//        double averageCost, double uPNL, double rPNL, String accountName)

        Position pos = new Position(contract, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName);

        this.m_positions.add(pos);





    }

    @Override
    // This method is called only when reqAccountUpdates() method on the EClientSocket object has been called.
    public void updateAccountTime(String timeStamp) {
        // timeStamp    String  This indicates the last update time of the account information
        System.out.println("updateAccountTime: ");
        System.out.print(timeStamp);
        System.out.print("\n");
//        throw new UnsupportedOperationException("updateAccountTime - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // This event is called after a batch updateAccountValue() and updatePortfolio() is sent.
    public void accountDownloadEnd(String accountName) {
        // accountName     String  The name of the account.
        System.out.println("UpdateAccount ends: " + accountName);
        //throw new UnsupportedOperationException("accountDownloadEnd - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // Returns the data from the TWS Account Window Summary tab in response to reqAccountSummary(). 
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {


        // tag     String  The tag from the data request.
        //                 Available tags are:

        // AccountType
        // TotalCashValue — Total cash including futures pnl
        // SettledCash — For cash accounts, this is the same as TotalCashValue
        // AccruedCash — Net accrued interest
        // BuyingPower — The maximum amount of marginable US stocks the account can buy
        // EquityWithLoanValue — Cash + stocks + bonds + mutual funds
        // PreviousEquityWithLoanValue
        // GrossPositionValue — The sum of the absolute value of all stock and equity option positions
        // RegTEquity
        // RegTMargin
        // SMA — Special Memorandum Account
        // InitMarginReq
        // MaintMarginReq
        // AvailableFunds
        // ExcessLiquidity
        // Cushion — Excess liquidity as a percentage of net liquidation value
        // FullInitMarginReq
        // FullMaintMarginReq
        // FullAvailableFunds
        // FullExcessLiquidity
        // LookAheadNextChange — Time when look-ahead values take effect
        // LookAheadInitMarginReq
        // LookAheadMaintMarginReq
        // LookAheadAvailableFunds
        // LookAheadExcessLiquidity
        // HighestSeverity — A measure of how close the account is to liquidation
        // DayTradesRemaining — The Number of Open/Close trades a user could put on before Pattern Day Trading is detected. A value of "-1" means that the user can put on unlimited day trades.
        // Leverage — GrossPositionValue / NetLiquidation


        throw new UnsupportedOperationException("accountSummary - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // This method is called once all account summary data for a given request are received.
    public void accountSummaryEnd(int reqId) {
        // reqId    int     The ID of the data request.
        throw new UnsupportedOperationException("accountSummaryEnd - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void position(String account, Contract contract, int pos, double avgCost) {

        // account     String      The account.
        // contract    Contract    This structure contains a full description of the contract that was executed.
        // pos         double      The position.        
        throw new UnsupportedOperationException("position - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // This is called once all position data for a given request are received and functions as an end marker for the position() data.
    public void positionEnd() {
        throw new UnsupportedOperationException("positionEnd - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
















    // -------------- Contract Details ------------------- //


    @Override
    // This method is called only when reqContractDetails method on the EClientSocket object has been called.
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        // reqID               int                 The ID of the data request. Ensures that responses are matched to requests if several requests are in process.
        // contractDetails     ContractDetails     This structure contains a full description of the contract being looked up.        
        throw new UnsupportedOperationException("contractDetails - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // This method is called only when reqContractDetails method on the EClientSocket object has been called for bonds.
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
        // reqId               int                 The ID of the data request.
        // contractDetails     ContractDetails     This structure contains a full description of the bond contract being looked up.        
        throw new UnsupportedOperationException("bondContractDetails - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    // This method is called once all contract details for a given request are received. This helps to define the end of an option chain.    
    public void contractDetailsEnd(int reqId) {
        // reqID    int     The Id of the data request.
        throw new UnsupportedOperationException("contractDetailsEnd - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
























    // -------------- Contract Details ------------------- //



    @Override
    // This method is called when the reqExecutions() method is invoked, or when an order is filled.
    public void execDetails(int reqId, Contract contract, Execution execution) {

        // reqId       int          The reqID that was specified previously in the call to reqExecution().
        // contract    Contract    
        //             This structure contains a full description of the contract that was executed.
        // execution   Execution   This structure contains addition order execution details.


        OpenOrder orderToRemove = null;
        for(OpenOrder oo: m_openOrders) {

            if(execution.m_orderId == oo.orderId) {


                if(execution.m_side.equals("BOT") ) {
                    this.hedge.futureLongShort =  this.hedge.futureLongShort + execution.m_shares;

                    Calendar c = new GregorianCalendar();

                    hedge.logTextArea.append(  String.format("%tT", c.getInstance()) + " execDetails() - buy Order Filled");




                }

                if(execution.m_side.equals("SLD")) {

                    this.hedge.futureLongShort =  this.hedge.futureLongShort - execution.m_shares;

                    Calendar c = new GregorianCalendar();

                    hedge.logTextArea.append(  String.format("%tT", c.getInstance()) + " execDetails() - sell Order Filled");


                }
            }

            oo.order.m_totalQuantity = oo.order.m_totalQuantity - execution.m_shares;

            if(oo.order.m_totalQuantity == 0) {

                orderToRemove = oo;



            }
        }

        if(orderToRemove != null) {
            m_openOrders.remove(orderToRemove);

            Calendar c = new GregorianCalendar();

            hedge.logTextArea.append(  String.format("%tT", c.getInstance()) + " execDetails() - Open Order Closed");

        }

    }

    @Override
    // This method is called once all executions have been sent to a client in response to reqExecutions().
    public void execDetailsEnd(int reqId) {
        // reqID    int     The Id of the data request.
        throw new UnsupportedOperationException("execDetailsEnd - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    // The commissionReport() callback is triggered as follows:
    // Immediately after a trade execution
    // By calling reqExecutions().

    public void commissionReport(CommissionReport commissionReport) {
        // commissionReport     CommissionReport    The structure that contains commission details.

        System.out.println("commissionReport() - commssion paid: " + commissionReport.m_commission);
//        throw new UnsupportedOperationException("commissionReport - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


















    // -------------- Historical Data ------------------- //

    @Override
    // This method receives the requested historical data results.
    public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {

        // reqId        int         The ticker Id of the request to which this bar is responding.
        // date         String      The date-time stamp of the start of the bar. The format is determined by the reqHistoricalData() formatDate parameter.
        // open         double      The bar opening price.
        // high         double      The high price during the time covered by the bar.
        // low          double      The low price during the time covered by the bar.
        // close        double      The bar closing price.
        // volume       int         The volume during the time covered by the bar.
        // count        int         When TRADES historical data is returned, represents the number of trades that occurred during the time period the bar covers
        // WAP          double      The weighted average price during the time covered by the bar.
        // hasGaps      boolean     Whether or not there are gaps in the data.


        throw new UnsupportedOperationException("historicalData - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
        throw new UnsupportedOperationException("updateMktDepth - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
        throw new UnsupportedOperationException("updateMktDepthL2 - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    //    This method is called when a successful connection is made to an account. It is also called when the reqManagedAccts() method is invoked.
    public void managedAccounts(String accountsList) {
        this.m_accountString = accountsList;
        System.out.print("acccount is: " + this.m_accountString + "\n");
        //throw new UnsupportedOperationException("managedAccounts - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveFA(int faDataType, String xml) {
        throw new UnsupportedOperationException("receiveFA - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    @Override
    public void scannerParameters(String xml) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
        throw new UnsupportedOperationException("scannerData - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void scannerDataEnd(int reqId) {
        throw new UnsupportedOperationException("scannerDataEnd - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
        throw new UnsupportedOperationException("realtimeBar - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



    @Override
    public void fundamentalData(int reqId, String data) {
        throw new UnsupportedOperationException("fundamentalData - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

















    @Override
    public void verifyMessageAPI(String apiData) {
        throw new UnsupportedOperationException("verifyMessageAPI - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
        throw new UnsupportedOperationException("verifyCompleted - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displayGroupList(int reqId, String groups) {
        throw new UnsupportedOperationException("displayGroupList - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
        throw new UnsupportedOperationException("displayGroupUpdated - Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }





}
