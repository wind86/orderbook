package com.orderbook.io.event.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.orderbook.event.Event;
import com.orderbook.event.handler.EventHandler;
import com.orderbook.interpreter.OrderBookEventInterpreter;
import com.orderbook.io.event.InputEvent;

@Component
public class InputEventHandler implements EventHandler<InputEvent> {

	@Autowired
	private OrderBookEventInterpreter orderBookEventInterpreter;
	
	@Autowired
	private EventBus eventBus;
	
	@Override
	@Subscribe
	public void handle(final InputEvent inputEvent) {
		Event event = orderBookEventInterpreter.interpret(inputEvent.getText());
		if (event != null) {
			eventBus.post(event);
		}
	}
}
