package com.monitor;

import com.ib.client.Contract;

/**
 * Created by nick on 12/30/14.
 */
public class Position {

    Contract m_contract;
    int m_amount;
    double m_marketPrice;
    double m_marketValue;
    double m_averageCost;
    double m_unrealizedPNL;
    double m_realizedPNL;
    String m_accountName;

    public Position(Contract c, double marketPrice, double marketValue,
                         double averageCost, double uPNL, double rPNL, String accountName) {

        this.m_contract = c;
        this.m_marketPrice = marketPrice;
        this.m_marketValue = marketValue;
        this.m_averageCost = averageCost;
        this.m_averageCost = averageCost;
        this.m_unrealizedPNL = uPNL;
        this.m_realizedPNL = rPNL;
        this.m_accountName = accountName;
    }
}
