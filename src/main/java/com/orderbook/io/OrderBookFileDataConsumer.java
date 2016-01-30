package com.orderbook.io;

import static org.apache.commons.io.FileUtils.isFileNewer;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.orderbook.io.event.InputEvent;

public class OrderBookFileDataConsumer extends Thread {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookFileDataConsumer.class);
	
	private final String fileName;
	private final EventBus eventBus;
	
	private long readTomepoint;
	private int lastReadedPosition;
	
	public OrderBookFileDataConsumer(final String fileName, final EventBus eventBus) {
		this.fileName = fileName;
		this.eventBus = eventBus;
	}
	
	@Override
	public void run() {
		readOrderBookFile();
	}
	
	protected void readOrderBookFile() {
		File file = new File(fileName);

		if (isFileNewer(file, readTomepoint)) {
			readOrderBookFile(file);
		}		
	}
	
	private void readOrderBookFile(File file) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {		
			String line = null;
			int readedLines = 0;
			while ((line = reader.readLine()) != null ) {
				readedLines++;
		
				if (isNotBlank(line) && readedLines > lastReadedPosition) {
					//LOGGER.info("> {}", line);
					eventBus.post(new InputEvent(line));
				}
				
				readTomepoint = System.currentTimeMillis();
				lastReadedPosition = readedLines;
			}
		} catch (Exception e) {
			LOGGER.error("file reading failed: {}", getMessage(e));
		}
	}
}
