package com.orderbook.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.ImmutableMap;
import com.orderbook.core.OrderBookEntry;
import com.orderbook.core.PriceType;

public class OrderBookQueueImpl implements OrderBookQueue<OrderBookEntry> {

	private static final ImmutableMap<PriceType, PriceComparator.Order> PRICE_ORDER_MAP = 
			ImmutableMap.<PriceType, PriceComparator.Order>builder()
			.put(PriceType.ASK, PriceComparator.Order.ASC)
			.put(PriceType.BID, PriceComparator.Order.DESC)
			.build();

	private PriceType priceType;
	private Queue<OrderBookEntry> queue;
	private Map<Integer, OrderBookEntry> entriesByPrice;
	
	public OrderBookQueueImpl(PriceType priceType) {
		this.queue = new PriorityQueue<>(new PriceComparator(PRICE_ORDER_MAP.get(priceType)));
		this.priceType = priceType;		
		this.entriesByPrice = new HashMap<>();
	}
	
	public PriceType getPriceType() {
		return priceType;
	}
	
	@Override
	public OrderBookEntry findByPrice(int price) {
		return entriesByPrice.get(price);
	}
	
	@Override
	public boolean add(OrderBookEntry entry) {
		boolean isAdded = queue.add(entry);
		
		if (isAdded) {
			entriesByPrice.put(entry.getPrice(), entry);
		}
		
		return isAdded; 
	}

	@Override
	public OrderBookEntry peek() {
		List<OrderBookEntry> entries = new ArrayList<>();
		
		OrderBookEntry resultEntry = null;
		while (!queue.isEmpty()) {
			resultEntry = queue.peek();
			
			if (resultEntry.getQuantity() > 0) {
				break;
			}

			entries.add(queue.poll());			
		}
		
		queue.addAll(entries);		
		return resultEntry;
	}
		
	public static class PriceComparator implements Comparator<OrderBookEntry> {

		public enum Order {
			ASC(1),
			DESC(-1);
			
			private int value;
			
			private Order(int value) {
				this.value = value;
			}
			
			public int value() {
				return value;
			}
		}
		
		private Order order;
		
		public PriceComparator(Order order) {
			this.order = order;
		}
		
		@Override
		public int compare(OrderBookEntry entry1, OrderBookEntry entry2) {
			return order.value() * NumberUtils.compare(entry1.getPrice(), entry2.getPrice());
		}
	}
}
