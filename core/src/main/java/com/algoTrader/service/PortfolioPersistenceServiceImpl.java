package com.algoTrader.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.math.util.MathUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.CronSequenceGenerator;

import com.algoTrader.entity.Strategy;
import com.algoTrader.entity.StrategyImpl;
import com.algoTrader.entity.Transaction;
import com.algoTrader.entity.TransactionImpl;
import com.algoTrader.entity.strategy.PortfolioValue;
import com.algoTrader.enumeration.Currency;
import com.algoTrader.enumeration.TransactionType;
import com.algoTrader.util.DateUtil;
import com.algoTrader.util.MyLogger;
import com.algoTrader.util.RoundUtil;

public abstract class PortfolioPersistenceServiceImpl extends PortfolioPersistenceServiceBase {

    private static Logger logger = MyLogger.getLogger(PortfolioPersistenceServiceImpl.class.getName());

    private @Value("#{T(com.algoTrader.enumeration.Currency).fromString('${misc.portfolioBaseCurrency}')}") Currency portfolioBaseCurrency;
    private @Value("${misc.rebalanceMinAmount}") double rebalanceMinAmount;

    @Override
    protected void handleRebalancePortfolio() throws Exception {

        Strategy base = getStrategyDao().findByName(StrategyImpl.BASE);
        Collection<Strategy> strategies = getStrategyDao().loadAll();
        double portfolioNetLiqValue = getPortfolioService().getNetLiqValueDouble();

        double totalAllocation = 0.0;
        double totalRebalanceAmount = 0.0;
        Collection<Transaction> transactions = new ArrayList<Transaction>();
        for (Strategy strategy : strategies) {

            totalAllocation += strategy.getAllocation();

            if (StrategyImpl.BASE.equals(strategy.getName())) {
                continue;
            }

            double actualNetLiqValue = MathUtils.round(getPortfolioService().getNetLiqValueDouble(strategy.getName()), 2);
            double targetNetLiqValue = MathUtils.round(portfolioNetLiqValue * strategy.getAllocation(), 2);
            double rebalanceAmount = targetNetLiqValue - actualNetLiqValue;

            if (Math.abs(rebalanceAmount) >= this.rebalanceMinAmount) {

                totalRebalanceAmount += rebalanceAmount;

                Transaction transaction = new TransactionImpl();
                transaction.setDateTime(DateUtil.getCurrentEPTime());
                transaction.setQuantity(targetNetLiqValue > actualNetLiqValue ? +1 : -1);
                transaction.setPrice(RoundUtil.getBigDecimal(Math.abs(rebalanceAmount)));
                transaction.setCurrency(this.portfolioBaseCurrency);
                transaction.setType(TransactionType.REBALANCE);
                transaction.setStrategy(strategy);

                transactions.add(transaction);
            }
        }

        // check allocations add up to 1.0
        if (MathUtils.round(totalAllocation, 2) != 1.0) {
            throw new IllegalStateException("the total of all allocations is: " + totalAllocation + " where it should be 1.0");
        }

        // add BASE REBALANCE transaction to offset totalRebalanceAmount
        if (transactions.size() != 0) {

            Transaction transaction = new TransactionImpl();
            transaction.setDateTime(DateUtil.getCurrentEPTime());
            transaction.setQuantity((int) Math.signum(-1.0 * totalRebalanceAmount));
            transaction.setPrice(RoundUtil.getBigDecimal(Math.abs(totalRebalanceAmount)));
            transaction.setCurrency(this.portfolioBaseCurrency);
            transaction.setType(TransactionType.REBALANCE);
            transaction.setStrategy(base);

            transactions.add(transaction);

        } else {

            logger.info("no rebalancing is performed because all rebalancing amounts are below min amount " + this.rebalanceMinAmount);
        }

        for (Transaction transaction : transactions) {

            getTransactionService().persistTransaction(transaction);
        }
    }

    @Override
    protected void handleSavePortfolioValues() throws Exception {

        for (Strategy strategy : getStrategyDao().findAutoActivateStrategies()) {

            PortfolioValue portfolioValue = getPortfolioService().getPortfolioValue(strategy.getName());

            // truncate Date to hour
            portfolioValue.setDateTime(DateUtils.truncate(portfolioValue.getDateTime(), Calendar.HOUR));

            getPortfolioValueDao().create(portfolioValue);
        }
    }

    @Override
    protected void handleSavePortfolioValue(Strategy strategy, Transaction transaction) throws Exception {

        // trades do not affect netLiqValue / performance so no portfolioValues are saved
        if (transaction.isTrade()) {
            return;

        // do not save a portfolioValue for BASE when rebalancing
        } else if (TransactionType.REBALANCE.equals(transaction.getType()) && strategy.isBase()) {
            return;
        }

        PortfolioValue portfolioValue = getPortfolioService().getPortfolioValue(strategy.getName());

        portfolioValue.setCashFlow(transaction.getGrossValue());

        getPortfolioValueDao().create(portfolioValue);
    }

    @Override
    protected void handleRestorePortfolioValues(Date fromDate, Date toDate) throws Exception {

        // same cron as SAVE_PORTFOLIO_VALUE
        CronSequenceGenerator cron = new CronSequenceGenerator("0 0 14-23 * * 1-5", TimeZone.getDefault());

        // sort by dateTime and strategy
        Set<PortfolioValue> portfolioValues = new TreeSet<PortfolioValue>(new Comparator<PortfolioValue>() {
            @Override
            public int compare(PortfolioValue p1, PortfolioValue p2) {
                if (!p1.getDateTime().equals(p2.getDateTime())) {
                    return p1.getDateTime().compareTo(p2.getDateTime());
                } else {
                    return p1.getStrategy().compareTo(p2.getStrategy());
                }
            }
        });

        // save values for all autoActiveStrategies
        List<Strategy> strategies = getStrategyDao().findAutoActivateStrategies();

        // create portfolioValues for all cron time slots
        Date date = fromDate;
        while (date.compareTo(toDate) <= 0) {

            date = cron.next(date);
            for (Strategy strategy : strategies) {

                PortfolioValue portfolioValue = getPortfolioService().getPortfolioValue(strategy.getName(), date);
                portfolioValues.add(portfolioValue);

                logger.info("processed portfolioValue for " + strategy.getName() + " " + date);
            }
        }

        // save values for all cashFlows
        List<Transaction> transactions = getTransactionDao().findAllCashflows();
        for (Transaction transaction : transactions) {

            // trades do not affect netLiqValue / performance so no portfolioValues are saved
            if (transaction.isTrade()) {
                continue;

            // do not save a portfolioValue for BASE when rebalancing
            } else if (TransactionType.REBALANCE.equals(transaction.getType()) && transaction.getStrategy().isBase()) {
                continue;
            }

            // don not save before fromDate
            if (transaction.getDateTime().compareTo(fromDate) < 0) {
                continue;
            }

            PortfolioValue portfolioValue = getPortfolioService().getPortfolioValue(transaction.getStrategy().getName(), transaction.getDateTime());
            portfolioValue.setCashFlow(transaction.getGrossValue());
            portfolioValues.add(portfolioValue);

            logger.info("processed portfolioValue for " + transaction.getStrategy().getName() + " " + transaction.getDateTime());
        }

        // netLiqValue might simply be zero because the strategy is not active yet
        for (Iterator<PortfolioValue> it = portfolioValues.iterator(); it.hasNext();) {
            PortfolioValue portfolioValue = it.next();
            if (portfolioValue.getNetLiqValueDouble() == 0) {
                it.remove();
            }
        }

        getPortfolioValueDao().create(portfolioValues);
    }
}
