package com.orderbook.interpreter.event;

import com.orderbook.core.PriceType;
import com.orderbook.event.Event;

public class UpdateEvent implements Event {

	private final int price;
	private final int size;
	private final PriceType type;
	
	public UpdateEvent(final int price, final int size, final PriceType type) {
		this.price = price;
		this.size = size;
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public int getSize() {
		return size;
	}

	public PriceType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("price:%d, size:%d, type:%s", price, size, type.name());
	}
}
