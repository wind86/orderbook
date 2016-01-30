package com.orderbook.repository;

public interface OrderBookQueue<T> {
	T peek();
	boolean add(T item);
	T findByPrice(int price);
}
