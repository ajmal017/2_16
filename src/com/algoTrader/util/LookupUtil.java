package com.algoTrader.util;

import com.algoTrader.ServiceLocator;
import com.algoTrader.entity.Account;
import com.algoTrader.entity.PortfolioValue;
import com.algoTrader.entity.Security;
import com.algoTrader.entity.StockOption;

public class LookupUtil {

    public static Security[] getSecuritiesInPortfolio() {

        ServiceLocator serviceLocator = ServiceLocator.instance();
        return serviceLocator.getLookupService().getAllSecuritiesInPortfolio();
    }

    public static StockOption[] getStockOptionsOnWatchlist() {

        ServiceLocator serviceLocator = ServiceLocator.instance();
        return serviceLocator.getLookupService().getStockOptionsOnWatchlist();
    }

    public static Account[] getAllAccounts() {
        ServiceLocator serviceLocator = ServiceLocator.instance();
        return serviceLocator.getLookupService().getAllAccounts();
    }

    public static boolean hasOpenPositions() {

        ServiceLocator serviceLocator = ServiceLocator.instance();
        return (serviceLocator.getLookupService().getOpenPositions().length != 0);
    }

    public static PortfolioValue getPortfolioValue() {

        ServiceLocator serviceLocator = ServiceLocator.instance();
        return serviceLocator.getLookupService().getPortfolioValue();
    }
}
