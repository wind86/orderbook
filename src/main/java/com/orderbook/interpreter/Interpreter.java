package com.orderbook.interpreter;

public interface Interpreter<IN,OUT> {
	OUT interpret(IN input);
}
