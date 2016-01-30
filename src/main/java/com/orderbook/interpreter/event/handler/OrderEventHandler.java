package com.orderbook.interpreter.event.handler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.orderbook.core.OrderBookEntry;
import com.orderbook.core.OrderType;
import com.orderbook.core.PriceType;
import com.orderbook.event.Event;
import com.orderbook.event.handler.EventHandler;
import com.orderbook.interpreter.event.OrderEvent;
import com.orderbook.interpreter.event.UpdateEvent;
import com.orderbook.repository.OrderBookRepository;

@Component
public class OrderEventHandler implements EventHandler<OrderEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventHandler.class); 
	
	private static final ImmutableMap<OrderType, PriceType> ORDER_PRICE_TYPES_MAPPING = 
			ImmutableMap.<OrderType, PriceType>builder()
			.put(OrderType.SELL, PriceType.BID)
			.put(OrderType.BUY, PriceType.ASK)
			.build();
	
	@Autowired
	private OrderBookRepository orderBookRepository;

	@Autowired
	private EventBus eventBus;
	
	@Override
	@Subscribe
	public void handle(final OrderEvent event) {
		PriceType priceType = ORDER_PRICE_TYPES_MAPPING.get(event.getType());
		OrderBookEntry entry = orderBookRepository.findBestPriceEntry(priceType);
		
		if (entry != null) {
			if (entry.getQuantity() >= event.getSize()) {
				postEvent(new UpdateEvent(entry.getPrice(), entry.getQuantity() - event.getSize(), priceType));
			} else {
				
				// make recursive call to itself in hope that new stocks appears				
				// http://www.aaii.com/journal/article/how-your-buy-and-sell-orders-get-filled.touch
				/*
				 A market order indicates you want the immediate execution of an order for a stated number of shares at the next available price 
				 without any other restrictions. This means your order will seek execution once it is received by the market (as long as the security is trading).
				 */
				// so, no expiration !!!
//				if (!isExpiredEvent(event)) {
				postEvent(new UpdateEvent(entry.getPrice(), 0, priceType));
				event.setSize(event.getSize() - entry.getQuantity());				
//				}
				
				new Thread(() -> postOrderEvent(event)).start();
			}
		} else {
			postOrderEvent(event);
		}
	}
	
	private void postOrderEvent(OrderEvent event) {
		try {
			// hope that somebody make decision and stocks becomes available
			Thread.sleep(200);
		} catch (InterruptedException e) {
			LOGGER.warn("sleeping thread interrupted", e);
		}
		postEvent(event);		
	}	
	
	private void postEvent(final Event event) {
		eventBus.post(event);
	}
	
	@SuppressWarnings("unused")
	private boolean isExpiredEvent(final OrderEvent event) {
		return ChronoUnit.DAYS.between(event.getCreatedTimepoint(), LocalDateTime.now()) < 1;
	}
}
