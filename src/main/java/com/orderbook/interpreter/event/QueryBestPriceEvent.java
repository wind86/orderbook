package com.orderbook.interpreter.event;

import com.orderbook.core.PriceType;

public class QueryBestPriceEvent extends QueryEvent {

	private final PriceType type;

	public QueryBestPriceEvent(final PriceType type) {
		this.type = type;
	}
	
	public PriceType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("type:%s", type.name());
	}
}
