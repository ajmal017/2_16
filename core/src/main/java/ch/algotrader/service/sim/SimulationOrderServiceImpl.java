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
package ch.algotrader.service.sim;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.Validate;

import ch.algotrader.entity.Account;
import ch.algotrader.entity.marketData.MarketDataEventVO;
import ch.algotrader.entity.security.Security;
import ch.algotrader.entity.trade.Fill;
import ch.algotrader.entity.trade.LimitOrderI;
import ch.algotrader.entity.trade.OrderStatus;
import ch.algotrader.entity.trade.SimpleOrder;
import ch.algotrader.enumeration.Direction;
import ch.algotrader.enumeration.OrderServiceType;
import ch.algotrader.enumeration.Side;
import ch.algotrader.enumeration.Status;
import ch.algotrader.esper.EngineManager;
import ch.algotrader.ordermgmt.OrderBook;
import ch.algotrader.service.MarketDataCacheService;
import ch.algotrader.service.OrderExecutionService;

/**
 * @author <a href="mailto:aflury@algotrader.ch">Andy Flury</a>
 */
public class SimulationOrderServiceImpl implements SimulationOrderService {

    private final MarketDataCacheService marketDataCacheService;
    private final OrderBook orderBook;
    private final OrderExecutionService orderExecutionService;
    private final EngineManager engineManager;
    private final AtomicLong counter;
    private final AtomicLong seqnum;

    public SimulationOrderServiceImpl(
            final OrderBook orderBook,
            final OrderExecutionService orderExecutionService,
            final MarketDataCacheService marketDataCacheService,
            final EngineManager engineManager) {

        Validate.notNull(orderBook, "OpenOrderRegistry is null");
        Validate.notNull(orderExecutionService, "OrderExecutionService is null");
        Validate.notNull(marketDataCacheService, "MarketDataCacheService is null");
        Validate.notNull(engineManager, "EngineManager is null");

        this.orderBook = orderBook;
        this.orderExecutionService = orderExecutionService;
        this.engineManager = engineManager;
        this.marketDataCacheService = marketDataCacheService;
        this.counter = new AtomicLong(0);
        this.seqnum = new AtomicLong(0);
    }

    @Override
    public String sendOrder(SimpleOrder order) {

        Validate.notNull(order, "Order is null");

        String intId = order.getIntId();
        if (intId == null) {

            intId = getNextOrderId(order.getAccount());
            order.setIntId(intId);
        }

        this.orderBook.add(order);

        Date d = this.engineManager.getCurrentEPTime();

        // send ack
        OrderStatus ack = OrderStatus.Factory.newInstance();
        ack.setStatus(Status.SUBMITTED);
        ack.setIntId(order.getIntId());
        ack.setFilledQuantity(0);
        ack.setRemainingQuantity(order.getQuantity());
        ack.setExtDateTime(d);
        ack.setDateTime(d);
        ack.setOrder(order);
        ack.setSequenceNumber(this.seqnum.incrementAndGet());

        // send the orderStatus to the AlgoTrader Server
        this.orderExecutionService.handleOrderStatus(ack);

        // send full execution
        OrderStatus orderStatus = OrderStatus.Factory.newInstance();
        orderStatus.setStatus(Status.EXECUTED);
        orderStatus.setIntId(order.getIntId());
        orderStatus.setFilledQuantity(order.getQuantity());
        orderStatus.setRemainingQuantity(0);
        orderStatus.setExtDateTime(d);
        orderStatus.setDateTime(d);
        orderStatus.setOrder(order);
        orderStatus.setSequenceNumber(this.seqnum.incrementAndGet());

        // send the orderStatus to the AlgoTrader Server
        this.orderExecutionService.handleOrderStatus(orderStatus);

        // create one fill per order
        Fill fill = new Fill();
        fill.setDateTime(d);
        fill.setExtDateTime(d);
        fill.setSide(order.getSide());
        fill.setQuantity(order.getQuantity());
        fill.setPrice(getPrice(order));
        fill.setOrder(order);
        fill.setExecutionCommission(getExecutionCommission(order));
        fill.setClearingCommission(getClearingCommission(order));
        fill.setFee(getFee(order));

        this.orderExecutionService.handleFill(fill);

        return intId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getPrice(final SimpleOrder order) {

        Validate.notNull(order, "Order is null");

        if (order instanceof LimitOrderI) {

            // limit orders are executed at their limit price
            return ((LimitOrderI) order).getLimit();

        } else {

            Security security = order.getSecurity();

            // all other orders are executed the the market
            MarketDataEventVO marketDataEvent = this.marketDataCacheService.getCurrentMarketDataEvent(security.getId());
            return marketDataEvent.getMarketValue(Side.BUY.equals(order.getSide()) ? Direction.SHORT : Direction.LONG)
                    .setScale(security.getSecurityFamily().getScale(), BigDecimal.ROUND_HALF_UP);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getExecutionCommission(final SimpleOrder order) {

        Validate.notNull(order, "Order is null");

        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getClearingCommission(final SimpleOrder order) {

        Validate.notNull(order, "Order is null");

        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getFee(final SimpleOrder order) {

        Validate.notNull(order, "Order is null");

        return null;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNextOrderId(final Account account) {

        long n = counter.incrementAndGet();
        return "sim" + n + ".0";
    }

    @Override
    public void validateOrder(SimpleOrder order) {

        // do nothing
    }

    @Override
    public String cancelOrder(SimpleOrder order) {

        throw new UnsupportedOperationException("cancel order not supported in simulation");
    }

    @Override
    public String modifyOrder(SimpleOrder order) {

        throw new UnsupportedOperationException("modify order not supported in simulation");
    }

    @Override
    public String getOrderServiceType() {

        return OrderServiceType.SIMULATION.name();
    }

}
