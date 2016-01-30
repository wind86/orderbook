package com.orderbook.interpreter.event.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.orderbook.core.OrderBookEntry;
import com.orderbook.core.PriceType;
import com.orderbook.event.handler.EventHandler;
import com.orderbook.interpreter.event.QuerySizeEvent;
import com.orderbook.io.event.OutputEvent;
import com.orderbook.repository.OrderBookRepository;

@Component
public class QuerySizeEventHandler implements EventHandler<QuerySizeEvent> {

	@Autowired
	private OrderBookRepository orderBookRepository;
	
	@Autowired
	private EventBus eventBus;
	
	@Override
	@Subscribe
	public void handle(final QuerySizeEvent event) {
		OrderBookEntry entry = orderBookRepository.findEntry(event.getPrice());
		
		if (entry == null) {
			entry = new OrderBookEntry(event.getPrice(), 0, PriceType.SPREAD);
		}
		
		eventBus.post(new OutputEvent(String.format("%d", entry.getQuantity())));
	}
}
