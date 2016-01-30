package com.orderbook.io.event.handler;

import java.io.File;
import java.io.IOException;
import static java.util.Arrays.*;

import static org.apache.commons.io.FileUtils.*;
import static org.apache.commons.lang3.exception.ExceptionUtils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.orderbook.event.handler.EventHandler;
import com.orderbook.io.event.OutputEvent;

@Component
public class OutputEventHandler implements EventHandler<OutputEvent> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OutputEventHandler.class);

	@Value("${order.book.output.file.name}")
	private String fileName;

	@Override
	@Subscribe
	public void handle(final OutputEvent event) {
		try {
			writeLines(new File(fileName), asList(String.valueOf(event.getResult())), true);
		} catch (IOException e) {
			LOGGER.error("error while writting result: {}", getMessage(e));
		}
	}
}
