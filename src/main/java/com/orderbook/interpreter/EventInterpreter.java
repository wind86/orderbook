package com.orderbook.interpreter;

import com.orderbook.event.Event;

public interface EventInterpreter<S, T extends Event> extends Interpreter<S, T> {

}
