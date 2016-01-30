package com.orderbook.interpreter;

import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

import org.springframework.stereotype.Component;

import com.orderbook.core.OrderType;
import com.orderbook.interpreter.event.OrderEvent;

@Component(value = "orderEventInterpreter")
public class OrderEventInterpreter extends AbstractEventInterpreter<OrderEvent> {

	@Override
	protected OrderEvent interpret(String[] parts) {
		OrderType type = OrderType.valueOf(upperCase(parts[1]));
		int size = toInt(parts[2]);
		
		return new OrderEvent(type, size);
	}
}
