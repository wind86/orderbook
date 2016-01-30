package com.orderbook.interpreter.event.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.orderbook.core.OrderBookEntry;
import com.orderbook.core.PriceType;
import com.orderbook.event.handler.EventHandler;
import com.orderbook.interpreter.event.QueryBestPriceEvent;
import com.orderbook.io.event.OutputEvent;
import com.orderbook.repository.OrderBookRepository;

@Component
public class QueryBestPriceEventHandler implements EventHandler<QueryBestPriceEvent> {

	@Autowired
	private OrderBookRepository orderBookRepository;
	
	@Autowired
	private EventBus eventBus;
	
	@Override
	@Subscribe
	public void handle(final QueryBestPriceEvent event) {
		OrderBookEntry entry = orderBookRepository.findBestPriceEntry(event.getType());
		
		if (entry == null) {
			entry = new OrderBookEntry(0, 0, PriceType.SPREAD);
		}
		
		eventBus.post(new OutputEvent(String.format("%d,%d", entry.getPrice(), entry.getQuantity())));
	}
}
