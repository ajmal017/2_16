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
package ch.algotrader.adapter.fix;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import quickfix.fix44.MarketDataRequest;
import ch.algotrader.entity.marketData.Tick;
import ch.algotrader.entity.marketData.TickDao;
import ch.algotrader.entity.security.Forex;
import ch.algotrader.entity.security.ForexImpl;
import ch.algotrader.entity.security.Security;
import ch.algotrader.entity.security.SecurityDao;
import ch.algotrader.entity.security.SecurityFamily;
import ch.algotrader.entity.security.SecurityFamilyImpl;
import ch.algotrader.enumeration.Currency;
import ch.algotrader.enumeration.FeedType;
import ch.algotrader.esper.Engine;
import ch.algotrader.esper.EngineLocator;
import ch.algotrader.service.fix.FixMarketDataServiceException;
import ch.algotrader.service.fix.fix44.Fix44MarketDataService;
import ch.algotrader.vo.SubscribeTickVO;

public class FIXMarketDataServiceTest {

    @Mock
    private SecurityDao securityDao;
    @Mock
    private TickDao tickDao;
    @Mock
    private FixAdapter fixAdapter;
    @Mock
    private FixSessionLifecycle sessionLifecycle;
    @Mock
    private Engine engine;

    private Fix44MarketDataService impl;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);

        FakeFix44MarketDataService fakeFix44MarketDataService = new FakeFix44MarketDataService(sessionLifecycle, fixAdapter, securityDao );

        impl = Mockito.spy(fakeFix44MarketDataService);

        EngineLocator.instance().setEngine("BASE", engine);
    }

    private static Forex createForex(final Currency base, final Currency counter) {
        SecurityFamily family = new SecurityFamilyImpl();
        family.setCurrency(counter);

        Forex forex = new ForexImpl();
        forex.setSymbol(base.getValue() + "." + counter.getValue());
        forex.setBaseCurrency(base);
        forex.setSecurityFamily(family);
        return forex;
    }

    @Test
    public void testInitialSubscriptions() throws Exception {


        Mockito.when(sessionLifecycle.isLoggedOn()).thenReturn(Boolean.TRUE);
        Mockito.when(sessionLifecycle.isSubscribed()).thenReturn(Boolean.FALSE);
        Mockito.when(sessionLifecycle.subscribe()).thenReturn(Boolean.TRUE);

        Forex forex = createForex(Currency.EUR, Currency.USD);

        Mockito.when(securityDao.findSubscribedByFeedTypeForAutoActivateStrategiesInclFamily(FeedType.SIM))
                .thenReturn(Collections.singletonList((Security) forex));

        // do initSubscriptions
        impl.initSubscriptions();

        // verify externalMarketDataService.subscribe
        Mockito.verify(impl).subscribe(forex);

        // verify engine.sendEvent
        ArgumentCaptor<Object> argumentCaptor1 = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(engine).sendEvent(argumentCaptor1.capture());

        List<Object> allEvents = argumentCaptor1.getAllValues();

        Assert.assertNotNull(allEvents);
        Assert.assertEquals(1, allEvents.size());
        Object event = allEvents.get(0);

        Assert.assertTrue(event instanceof SubscribeTickVO);
        SubscribeTickVO subscribeTick = (SubscribeTickVO) event;
        Tick tick = subscribeTick.getTick();
        Assert.assertNotNull(tick);
        Assert.assertSame(forex, tick.getSecurity());

        // verify fixAdapter.sendMessage
        Mockito.verify(fixAdapter, Mockito.times(1)).sendMessage(Mockito.<MarketDataRequest>any(), Mockito.anyString());

        // verify no event has been sent to the engine
        Mockito.verify(engine, Mockito.never()).executeQuery(Mockito.anyString());
    }

    @Test
    public void testInitialSubscriptionsAlreadySubscribed() throws Exception {

        Mockito.when(sessionLifecycle.isLoggedOn()).thenReturn(Boolean.TRUE);
        Mockito.when(sessionLifecycle.isSubscribed()).thenReturn(Boolean.TRUE);
        Mockito.when(sessionLifecycle.subscribe()).thenReturn(Boolean.FALSE);

        Forex forex = createForex(Currency.EUR, Currency.USD);
        Mockito.when(securityDao.findSubscribedByFeedTypeForAutoActivateStrategiesInclFamily(FeedType.SIM))
                .thenReturn(Collections.singletonList((Security) forex));

        // do initSubscriptions
        impl.initSubscriptions();

        // verify externalMarketDataService.subscribe
        Mockito.verify(impl, Mockito.never()).subscribe(Mockito.<Security>any());
    }

    @Test
    public void testSubscribe() throws Exception {

        Mockito.when(sessionLifecycle.isLoggedOn()).thenReturn(Boolean.TRUE);

        Forex forex = createForex(Currency.EUR, Currency.USD);

        // Do subscribe
        impl.subscribe(forex);

        Mockito.verify(impl).getTickerId(forex);

        // verify engine.sendEvent
        ArgumentCaptor<Object> argumentCaptor1 = ArgumentCaptor.forClass(Object.class);
        Mockito.verify(engine).sendEvent(argumentCaptor1.capture());

        List<Object> allEvents = argumentCaptor1.getAllValues();

        Assert.assertNotNull(allEvents);
        Assert.assertEquals(1, allEvents.size());
        Object event = allEvents.get(0);

        Assert.assertTrue(event instanceof SubscribeTickVO);
        SubscribeTickVO subscribeTick = (SubscribeTickVO) event;
        Tick tick = subscribeTick.getTick();
        Assert.assertNotNull(tick);
        Assert.assertSame(forex, tick.getSecurity());

        // verify fixSessionFactory.sendMessage
        Mockito.verify(fixAdapter, Mockito.times(1)).sendMessage(Mockito.<MarketDataRequest>any(), Mockito.anyString());

        // verify engine.executeQuery does not get called
        Mockito.verify(engine, Mockito.never()).executeQuery(Mockito.anyString());
    }

    @Test(expected = FixMarketDataServiceException.class)
    public void testSubscribeNotLoggedOn() throws Exception {

        Mockito.when(sessionLifecycle.isLoggedOn()).thenReturn(Boolean.FALSE);

        Forex forex = createForex(Currency.EUR, Currency.USD);

        // Do subscribe
        impl.subscribe(forex);
    }

    @Test
    public void testUnsubscribe() throws Exception {

        Mockito.when(sessionLifecycle.isLoggedOn()).thenReturn(Boolean.TRUE);
        Mockito.when(sessionLifecycle.isSubscribed()).thenReturn(Boolean.TRUE);

        Forex forex = createForex(Currency.EUR, Currency.USD);
        forex.setId(123);

        // Do unsubscribe
        impl.unsubscribe(forex);

        // verify no event has been sent to the engine
        Mockito.verify(engine, Mockito.never()).sendEvent(Mockito.any());

        // verify fixSessionFactory.sendMessage
        // verify fixSessionFactory.sendMessage
        Mockito.verify(fixAdapter, Mockito.times(1)).sendMessage(Mockito.<MarketDataRequest>any(), Mockito.anyString());

        // verify the esper delete statement has been executed
        Mockito.verify(engine).executeQuery("delete from TickWindow where security.id = 123");
    }

    @Test(expected = FixMarketDataServiceException.class)
    public void testUnsubscribeNotSubscribed() throws Exception {

        Mockito.when(sessionLifecycle.isLoggedOn()).thenReturn(Boolean.TRUE);
        Mockito.when(sessionLifecycle.isSubscribed()).thenReturn(Boolean.FALSE);

        Forex forex = createForex(Currency.EUR, Currency.USD);

        // Do unsubscribe
        impl.unsubscribe(forex);
    }

}