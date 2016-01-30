package com.orderbook.interpreter.event.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.orderbook.core.OrderBookEntry;
import com.orderbook.event.handler.EventHandler;
import com.orderbook.interpreter.event.UpdateEvent;
import com.orderbook.repository.OrderBookRepository;

@Component
public class UpdateEventHandler implements EventHandler<UpdateEvent> {

	@Autowired
	private OrderBookRepository orderBookRepository;
	
	@Override
	@Subscribe
	public void handle(final UpdateEvent event) {
		// cross orders out of scope
		OrderBookEntry entry = orderBookRepository.findEntry(event.getPrice(), event.getType());
		
		if (entry != null) {
			entry.setQuantity(event.getSize());
		} else {
			entry = new OrderBookEntry(event.getPrice(), event.getSize(), event.getType());
		}
		
		orderBookRepository.put(entry);
	}
}
