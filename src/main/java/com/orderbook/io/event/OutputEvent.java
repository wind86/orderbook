package com.orderbook.io.event;

import com.orderbook.event.Event;

public class OutputEvent implements Event {

	private final Object result;
	
	public OutputEvent(final Object result) {
		this.result = result;
	}
	
	public Object getResult() {
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("result:%s", String.valueOf(result));
	}
}
