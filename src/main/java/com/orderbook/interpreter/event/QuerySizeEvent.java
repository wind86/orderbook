package com.orderbook.interpreter.event;

public class QuerySizeEvent extends QueryEvent {

	private final int price;
	
	public QuerySizeEvent(final int price) {
		this.price = price;
	}
	
	public int getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return String.format("price:%d", price);
	}
}
