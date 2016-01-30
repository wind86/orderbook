package com.orderbook.core;

public enum PriceType {
	ASK,
	BID,
	SPREAD;
		
	@Override
	public String toString() {
		return String.valueOf(name().charAt(0));
	}
}