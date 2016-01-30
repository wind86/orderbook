package com.orderbook.interpreter;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.orderbook.event.Event;

@Component(value = "orderBookEventInterpreter")
public class OrderBookEventInterpreter implements EventInterpreter<String, Event> {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookEventInterpreter.class); 
	
	private ImmutableMap<String, EventInterpreter<String, ? extends Event>> eventInterpreters; 
	
	@Autowired
	@Qualifier(value = "orderEventInterpreter")
	private EventInterpreter<String, ? extends Event> orderEventInterpreter; 
	
	@Autowired
	@Qualifier(value = "queryEventInterpreter")
	private EventInterpreter<String, ? extends Event> queryEventInterpreter;
	
	@Autowired
	@Qualifier(value = "updateEventInterpreter")
	private EventInterpreter<String, ? extends Event> updateEventInterpreter;
	
	@PostConstruct
	public void init() {
		eventInterpreters = ImmutableMap.<String, EventInterpreter<String, ? extends Event>>builder()
				.put("u", updateEventInterpreter)
				.put("o", orderEventInterpreter)
				.put("q", queryEventInterpreter)
				.build();
	}
	
	@Override
	public Event interpret(String input) {
		String eventCode = StringUtils.lowerCase(String.valueOf(input.charAt(0)));
		EventInterpreter<String, ? extends Event> interpreter = eventInterpreters.get(eventCode);
		
		if (interpreter != null) {
			return interpreter.interpret(input);
		}
		
		LOGGER.error("Unknown input command: {}", input);		
		return null;
	}
}
