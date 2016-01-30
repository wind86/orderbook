package com.orderbook.io.event;

import com.orderbook.event.Event;

public class InputEvent implements Event {
	
	private final String text;
	
	public InputEvent(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return String.format("text:%s", text);
	}
}
