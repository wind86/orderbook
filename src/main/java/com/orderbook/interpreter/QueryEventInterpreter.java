package com.orderbook.interpreter;

import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

import org.springframework.stereotype.Component;

import com.orderbook.core.PriceType;
import com.orderbook.interpreter.event.QueryBestPriceEvent;
import com.orderbook.interpreter.event.QueryEvent;
import com.orderbook.interpreter.event.QuerySizeEvent;

@Component(value = "queryEventInterpreter")
public class QueryEventInterpreter extends AbstractEventInterpreter<QueryEvent> {

	private enum Type {
		BEST_BID,
		BEST_ASK,
		SIZE
	}
	
	@Override
	protected QueryEvent interpret(String[] parts) {
		Type type = Type.valueOf(upperCase(parts[1]));		
		switch (type) {
		case BEST_ASK:
			return new QueryBestPriceEvent(PriceType.ASK);
		case BEST_BID:
			return new QueryBestPriceEvent(PriceType.BID);
		case SIZE:
			return new QuerySizeEvent(toInt(parts[2]));
		}
		
		return null;
	}
}
