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
package ch.algotrader.service.fxcm;

import ch.algotrader.adapter.fix.FixAdapter;
import ch.algotrader.adapter.fxcm.FXCMFixOrderMessageFactory;
import ch.algotrader.entity.trade.SimpleOrder;
import ch.algotrader.enumeration.OrderServiceType;
import ch.algotrader.service.OrderService;
import ch.algotrader.service.fix.fix44.Fix44OrderServiceImpl;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReplaceRequest;
import quickfix.fix44.OrderCancelRequest;

/**
 * @author <a href="mailto:aflury@algotrader.ch">Andy Flury</a>
 *
 * @version $Revision$ $Date$
 */
public class FXCMFixOrderServiceImpl extends Fix44OrderServiceImpl implements FXCMFixOrderService {

    private static final long serialVersionUID = -3160564098018866170L;

    public FXCMFixOrderServiceImpl(final FixAdapter fixAdapter,
            final OrderService orderService) {

        super(fixAdapter, orderService, new FXCMFixOrderMessageFactory());
    }

    @Override
    public void init() {

        try {
            getFixAdapter().openSession(getOrderServiceType());
        } catch (Exception ex) {
            throw new FXCMFixOrderServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void sendOrder(SimpleOrder order, NewOrderSingle newOrder) {
    }

    @Override
    public void modifyOrder(SimpleOrder order, OrderCancelReplaceRequest replaceRequest) {
    }

    @Override
    public void cancelOrder(SimpleOrder order, OrderCancelRequest cancelRequest) {
    }

    @Override
    public OrderServiceType getOrderServiceType() {

        return OrderServiceType.FXCM_FIX;
    }
}