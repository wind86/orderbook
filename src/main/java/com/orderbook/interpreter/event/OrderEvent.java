package com.orderbook.interpreter.event;

import java.time.LocalDateTime;

import com.orderbook.core.OrderType;
import com.orderbook.event.Event;

public class OrderEvent implements Event {

	private final LocalDateTime createdTimepoint;
	private final OrderType type;
	private int size;
	
	public OrderEvent(final OrderType type, final int size) {
		this.type = type; 
		this.size = size; 
		this.createdTimepoint = LocalDateTime.now();
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

	public OrderType getType() {
		return type;
	}
	
	public LocalDateTime getCreatedTimepoint() {
		return createdTimepoint;
	}
	
	@Override
	public String toString() {
		return String.format("size:%d,type:%s,created:%s", size, type.name(), createdTimepoint.toString());
	}
}
