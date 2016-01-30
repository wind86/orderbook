package com.orderbook.event.handler;

import com.orderbook.event.Event;

public interface EventHandler<T extends Event> {
	void handle(T event);
}
