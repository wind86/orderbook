package com.orderbook.interpreter;

import org.apache.commons.lang3.StringUtils;

import com.orderbook.event.Event;

public abstract class AbstractEventInterpreter<T extends Event> implements EventInterpreter<String,T> {

	private static final String SEPARATOR = ",";
	
	@Override
	public T interpret(String input) {
		return interpret(StringUtils.split(input, SEPARATOR));
	}
	
	protected abstract T interpret(String[] parts);
}
