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
package ch.algotrader.dao.strategy;

import java.util.Date;
import java.util.List;

import ch.algotrader.dao.EntityConverter;
import ch.algotrader.dao.ReadWriteDao;
import ch.algotrader.entity.strategy.PortfolioValue;

/**
 * DAO for {@link ch.algotrader.entity.strategy.PortfolioValue} objects.
 *
 * @see ch.algotrader.entity.strategy.PortfolioValue
 */
public interface PortfolioValueDao extends ReadWriteDao<PortfolioValue> {

    /**
     *
     * @param strategyName
     * @param minDate
     * @return List<PortfolioValue>
     */
    List<PortfolioValue> findByStrategyAndMinDate(String strategyName, Date minDate);

    /**
     * <p>
     * Does the same thing as {@link #findByStrategyAndMinDate(String, Date)} with an
     * additional conversion of entities to value objects.
     * @param strategyName
     * @param minDate
     * @param converter
     * @return List<V>
     * </p>
     */
    <V> List<V> findByStrategyAndMinDate(String strategyName, Date minDate, EntityConverter<PortfolioValue, V> converter);

    // spring-dao merge-point
}