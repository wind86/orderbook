package com.orderbook.io;

import com.google.common.eventbus.EventBus;

public class OrderBookFileCommandLoader implements OrderCommandLoader {

	private final String fileName;
	private final EventBus eventBus;
	
	public OrderBookFileCommandLoader(final String fileName, final EventBus eventBus) {
		this.fileName = fileName;
		this.eventBus = eventBus;
	}
	
	@Override
	public void run() {
		new OrderBookFileDataConsumer(fileName, eventBus).start();
		System.out.println("DONE: check result file");
	}
}
