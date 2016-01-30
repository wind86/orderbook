package com.orderbook.interpreter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import com.orderbook.core.PriceType;
import com.orderbook.interpreter.event.UpdateEvent;

@Component(value = "updateEventInterpreter")
public class UpdateEventInterpreter extends AbstractEventInterpreter<UpdateEvent> {

	@Override
	protected UpdateEvent interpret(String[] parts) {
		int price = NumberUtils.toInt(parts[1]);
		int size = NumberUtils.toInt(parts[2]);
		PriceType priceType = PriceType.valueOf(StringUtils.upperCase(parts[3])); 

		return new UpdateEvent(price, size, priceType);
	}
}
