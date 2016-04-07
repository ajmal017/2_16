/***********************************************************************************
 * AlgoTrader Enterprise Trading Framework
 *
 * Copyright (C) 2015 AlgoTrader GmbH - All rights reserved
 *
 * All information contained herein is, and remains the property of AlgoTrader GmbH.
 * The intellectual and technical concepts contained herein are proprietary to
 * AlgoTrader GmbH. Modification, translation, reverse engineering, decompilation,
 * disassembly or reproduction of this material is strictly forbidden unless prior
 * written permission is obtained from AlgoTrader GmbH
 *
 * Fur detailed terms and conditions consult the file LICENSE.txt or contact
 *
 * AlgoTrader GmbH
 * Aeschstrasse 6
 * 8834 Schindellegi
 ***********************************************************************************/
package ch.algotrader.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.math.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import ch.algotrader.accounting.PositionTracker;
import ch.algotrader.accounting.PositionTrackerImpl;
import ch.algotrader.entity.Position;
import ch.algotrader.entity.Transaction;
import ch.algotrader.entity.security.Security;
import ch.algotrader.entity.security.SecurityFamily;
import ch.algotrader.entity.security.SecurityFamilyImpl;
import ch.algotrader.entity.security.StockImpl;
import ch.algotrader.entity.strategy.Strategy;
import ch.algotrader.entity.strategy.StrategyImpl;
import ch.algotrader.enumeration.Currency;
import ch.algotrader.enumeration.TransactionType;
import ch.algotrader.util.DateTimeLegacy;

/**
 * Cost Accounting Test.
 *
 * Compare with "IT\Cost Accounting\Position Properties.xlsx"
 *
 * @author <a href="mailto:aflury@algotrader.ch">Andy Flury</a>
 */
public class CostAccountingTest {

    @Test
    public void test() throws ParseException {

        PositionTracker positionTracker = PositionTrackerImpl.INSTANCE;

        Strategy strategy = new StrategyImpl();

        SecurityFamily family = new SecurityFamilyImpl();
        family.setContractSize(10.0);
        family.setScale(2);

        Security security = new StockImpl();
        security.setSecurityFamily(family);

        List<Transaction> transactions = new ArrayList<>();

        Transaction transaction;

        transaction = Transaction.Factory.newInstance(UUID.randomUUID().toString(), DateTimeLegacy.parseAsTimeGMT("08:00:00"), 10, new BigDecimal(100), Currency.USD, TransactionType.BUY, strategy);
        transaction.setId(1);
        transaction.setSecurity(security);
        transaction.setExecutionCommission(new BigDecimal("10.0"));
        transactions.add(transaction);

        Position position = positionTracker.processFirstTransaction(transaction);

        assertPosition(position, 100, 10, 10010, 100.1, 10000, -10, 0);
        assertPosition(position, 105, 10, 10010, 100.1, 10500, 490, 0);

        transaction = Transaction.Factory.newInstance(UUID.randomUUID().toString(), DateTimeLegacy.parseAsTimeGMT("09:00:00"), 2, new BigDecimal(110), Currency.USD, TransactionType.BUY, strategy);
        transaction.setId(2);
        transaction.setSecurity(security);
        transaction.setExecutionCommission(new BigDecimal("2.0"));
        transactions.add(transaction);

        positionTracker.processTransaction(position, transaction);

        assertPosition(position, 110, 12, 12212, 101.77, 13200, 988, 0);
        assertPosition(position, 115, 12, 12212, 101.77, 13800, 1588, 0);

        transaction = Transaction.Factory.newInstance(UUID.randomUUID().toString(), DateTimeLegacy.parseAsTimeGMT("10:00:00"), -12, new BigDecimal(120), Currency.USD, TransactionType.SELL, strategy);
        transaction.setId(3);
        transaction.setSecurity(security);
        transaction.setExecutionCommission(new BigDecimal("12.0"));
        transactions.add(transaction);

        positionTracker.processTransaction(position, transaction);

        assertPosition(position, 120, 0, 0, Double.NaN, 0, 0, 2176);
        assertPosition(position, 125, 0, 0, Double.NaN, 0, 0, 2176);

        transaction = Transaction.Factory.newInstance(UUID.randomUUID().toString(), DateTimeLegacy.parseAsTimeGMT("11:00:00"), -20, new BigDecimal(130), Currency.USD, TransactionType.SELL, strategy);
        transaction.setId(4);
        transaction.setSecurity(security);
        transaction.setExecutionCommission(new BigDecimal("20.0"));
        transactions.add(transaction);

        positionTracker.processTransaction(position, transaction);

        assertPosition(position, 130, -20, -25980, 129.9, -26000, -20, 2176);
        assertPosition(position, 135, -20, -25980, 129.9, -27000, -1020, 2176);

        transaction = Transaction.Factory.newInstance(UUID.randomUUID().toString(), DateTimeLegacy.parseAsTimeGMT("12:00:00"), 12, new BigDecimal(140), Currency.USD, TransactionType.BUY, strategy);
        transaction.setId(5);
        transaction.setSecurity(security);
        transaction.setExecutionCommission(new BigDecimal("12.0"));
        transactions.add(transaction);

        positionTracker.processTransaction(position, transaction);

        assertPosition(position, 140, -8, -10392, 129.9, -11200, -808, 952);
    }

    private void assertPosition(Position position, double price, long eQty, double eCost, double eAvgPrice, double eMarketValue, double eUnrealizedPL, double eRealizedPL) {

        long qty = position.getQuantity();
        double cost = MathUtils.round(position.getCost(), 2);
        double marketValue = MathUtils.round(qty * 10 * price, 2);
        double unrealizedPL = MathUtils.round(marketValue - cost, 2);
        double avgPrice = MathUtils.round(position.getAveragePrice(), 2);
        double realizedPL = MathUtils.round(position.getRealizedPL(), 2);

        Assert.assertEquals(eQty, qty);
        Assert.assertEquals(eCost, cost, 0.001);
        Assert.assertEquals(eMarketValue, marketValue, 0.001);
        Assert.assertEquals(eUnrealizedPL, unrealizedPL, 0.001);
        Assert.assertEquals(eAvgPrice, avgPrice, 0.001);
        Assert.assertEquals(eRealizedPL, realizedPL, 0.001);
    }

}