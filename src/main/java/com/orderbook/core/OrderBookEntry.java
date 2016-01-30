package com.orderbook.core;

public class OrderBookEntry {
	
	private final int price;	
	private final PriceType type;
	private int quantity;
	
	public OrderBookEntry(final int price, int quantity, final PriceType type) {
		this.price = price;
		this.quantity = quantity;
		this.type = type;
	}
	
	public int getPrice() {
		return price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public PriceType getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return String.format("price: %d, quantity: %d, type: %s", price, quantity, type.name());
	}
}
