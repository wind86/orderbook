package com.orderbook.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.orderbook.core.OrderBookEntry;
import com.orderbook.core.PriceType;

@Component
public class OrderBookRepositoryImpl implements OrderBookRepository {

	//private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookRepositoryImpl.class);
	
	private static final OrderBookQueueImpl SELL_QUEUE = new OrderBookQueueImpl(PriceType.ASK);
	private static final OrderBookQueueImpl BUY_QUEUE = new OrderBookQueueImpl(PriceType.BID);
	
	private static final Map<PriceType, OrderBookQueueImpl> STORAGE = new HashMap<>(2);
	
	public OrderBookRepositoryImpl() {
		STORAGE.put(BUY_QUEUE.getPriceType(), BUY_QUEUE);
		STORAGE.put(SELL_QUEUE.getPriceType(), SELL_QUEUE);
	} 	
	
	@Override
	public OrderBookEntry findEntry(int price, PriceType priceType) {
		return getStorage(priceType).findByPrice(price);
	}

	@Override
	public void put(OrderBookEntry entry) {
		getStorage(entry.getType()).add(entry);
	}

	@Override
	public OrderBookEntry findBestPriceEntry(PriceType priceType) {
		return getStorage(priceType).peek();
	}

	@Override
	public OrderBookEntry findEntry(int price) {
		for (PriceType priceType : STORAGE.keySet()) {
			OrderBookEntry entry = findEntry(price, priceType);
			if (entry != null) {
				return entry;
			}
		}
		return null;
	}
	
	private OrderBookQueueImpl getStorage(PriceType priceType) {
		return STORAGE.get(priceType);
	}
}
