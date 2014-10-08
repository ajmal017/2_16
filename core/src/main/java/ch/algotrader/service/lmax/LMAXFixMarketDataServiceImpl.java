/***********************************************************************************
 * AlgoTrader Enterprise Trading Framework
 *
 * Copyright (C) 2014 AlgoTrader GmbH - All rights reserved
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
 * Badenerstrasse 16
 * 8004 Zurich
 ***********************************************************************************/
package ch.algotrader.service.lmax;

import org.apache.commons.lang.Validate;

import quickfix.field.SubscriptionRequestType;
import quickfix.fix44.MarketDataRequest;
import ch.algotrader.adapter.fix.FixAdapter;
import ch.algotrader.adapter.fix.FixSessionLifecycle;
import ch.algotrader.adapter.lmax.LMAXFixMarketDataRequestFactory;
import ch.algotrader.entity.security.Security;
import ch.algotrader.entity.security.SecurityDao;
import ch.algotrader.enumeration.FeedType;
import ch.algotrader.service.fix.fix44.Fix44MarketDataServiceImpl;

/**
 * @author <a href="mailto:aflury@algotrader.ch">Andy Flury</a>
 *
 * @version $Revision$ $Date$
 */
public class LMAXFixMarketDataServiceImpl extends Fix44MarketDataServiceImpl implements LMAXFixMarketDataService {

    private static final long serialVersionUID = 1144501885597028244L;

    private final LMAXFixMarketDataRequestFactory requestFactory;

    public LMAXFixMarketDataServiceImpl(final FixSessionLifecycle lifeCycle,
            final FixAdapter fixAdapter,
            final SecurityDao securityDao) {

        super(lifeCycle, fixAdapter, securityDao);

        Validate.notNull(fixAdapter, "FixAdapter is null");

        this.requestFactory = new LMAXFixMarketDataRequestFactory();
    }

    @Override
    public FeedType getFeedType() {

        return FeedType.LMAX;
    }

    @Override
    public String getSessionQualifier() {

        return "LMAXMD";
    }

    @Override
    public void sendSubscribeRequest(Security security) {

        Validate.notNull(security, "Security is null");

        try {
            MarketDataRequest request = this.requestFactory.create(security, new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES));

            getFixAdapter().sendMessage(request, getSessionQualifier());
        } catch (Exception ex) {
            throw new LMAXFixMarketDataServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void sendUnsubscribeRequest(Security security) {

        Validate.notNull(security, "Security is null");

        try {
            MarketDataRequest request = this.requestFactory.create(security, new SubscriptionRequestType(SubscriptionRequestType.DISABLE_PREVIOUS_SNAPSHOT_PLUS_UPDATE_REQUEST));

            getFixAdapter().sendMessage(request, getSessionQualifier());
        } catch (Exception ex) {
            throw new LMAXFixMarketDataServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public String getTickerId(Security security) {

        Validate.notNull(security, "Security is null");

        try {
            return security.getLmaxid();
        } catch (Exception ex) {
            throw new LMAXFixMarketDataServiceException(ex.getMessage(), ex);
        }
    }
}