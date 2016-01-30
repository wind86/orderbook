package com.orderbook.repository;

import com.orderbook.core.OrderBookEntry;
import com.orderbook.core.PriceType;

public interface OrderBookRepository {
	OrderBookEntry findEntry(int price, PriceType priceType);
	
	void put(OrderBookEntry entry);
	
	OrderBookEntry findBestPriceEntry(PriceType priceType);
	
	OrderBookEntry findEntry(int price);
}
