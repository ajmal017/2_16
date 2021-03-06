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
package ch.algotrader.dao.security;

import java.util.List;

import ch.algotrader.dao.ReadWriteDao;
import ch.algotrader.entity.security.Stock;

/**
 * DAO for {@link ch.algotrader.entity.security.Stock} objects.
 *
 * @see ch.algotrader.entity.security.Stock
 */
public interface StockDao extends ReadWriteDao<Stock> {

    /**
     *
     * @param code
     * @return List<Stock>
     */
    List<Stock> findBySectory(String code);

    /**
     *
     * @param code
     * @return List<Stock>
     */
    List<Stock> findByIndustryGroup(String code);

    /**
     *
     * @param code
     * @return List<Stock>
     */
    List<Stock> findByIndustry(String code);

    /**
     *
     * @param code
     * @return List<Stock>
     */
    List<Stock> findBySubIndustry(String code);

    /**
     * Finds all Stocks of the specified {@link ch.algotrader.entity.security.SecurityFamily SecurityFamily}
     * @param securityFamilyId
     * @return List<Stock>
     */
    List<Stock> findStocksBySecurityFamily(long securityFamilyId);

    // spring-dao merge-point
}