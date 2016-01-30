package com.orderbook.io;

import static org.apache.commons.lang3.StringUtils.LF;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.apache.commons.lang3.StringUtils.lowerCase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

@Component
public class OrderBookInteractiveConsole implements OrderCommandLoader {

	//private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookInteractiveConsole.class);
	
	private static final Semaphore SEMAPHORE = new Semaphore(0);
	private static final Semaphore MUTEX = new Semaphore(1);
	
	@Value("${order.book.input.file.name}")
	private String fileName;
	
	@Autowired
	private EventBus eventBus;
	
	@Override
	public void run() {
		System.out.println("type order book command and hit enter");
		new OrderBookConsoleDataProducer(fileName).start();
		new OrderBookConsoleDataConsumer(fileName, eventBus).start();
	}
	
	private static class OrderBookConsoleDataProducer extends Thread {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookConsoleDataProducer.class);
		
		private static final Pattern[] ACCEPTABLE_PATTERNS = new Pattern[] {
			Pattern.compile("u,(\\d){1,},(\\d){1,},(bid|ask){1}"),
			Pattern.compile("q,(best_bid|best_ask){1}"),
			Pattern.compile("q,(size){1},(\\d){1,}"),
			Pattern.compile("o,(buy|sell){1},(\\d){1,}")
		};
		
		private final String fileName;
		private final BufferedReader consoleReader;
		
		public OrderBookConsoleDataProducer(final String fileName) {
			this.fileName = fileName;			
			this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
		} 
		
		@Override
		public void run() {
			try {
				while (true) {
					MUTEX.acquire();
					
					processUserInput();
					
					MUTEX.release();
					SEMAPHORE.release();

					Thread.sleep(50);
				}				
			} catch (final Exception e) {
				LOGGER.error("error while processing console input", ExceptionUtils.getMessage(e));
			}
		}
		
		private void processUserInput() throws IOException {
			try (Writer fileWriter = new BufferedWriter(new FileWriter(fileName, true))) {
				System.out.print("> ");
				
				String line = null;
				while ((line = consoleReader.readLine()) != null) {
					line = lowerCase(deleteWhitespace(line));
					
					if (containsIgnoreCase(line, "exit")) {
						System.exit(0);
					} 
					
					if (!isAcceptableLine(line)) {
						String message = String.format("not acceptable line: '%s'", line);
						System.err.println(message);
						//LOGGER.warn(message);
						return;
					}
					
					fileWriter.write(LF + line);
					return;
				}
			} catch (final IOException e) {
				LOGGER.error("error while processing user input: {}", ExceptionUtils.getMessage(e));
			}			
		}
		
		private boolean isAcceptableLine(String line) {
			for (Pattern pattern : ACCEPTABLE_PATTERNS) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					return true;
				}
			}
			return false;
		}
	}
	
	private static class OrderBookConsoleDataConsumer extends OrderBookFileDataConsumer {

		private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookConsoleDataConsumer.class);
		
		public OrderBookConsoleDataConsumer(final String fileName, final EventBus eventBus) {
			super(fileName, eventBus);
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					SEMAPHORE.acquire();
					MUTEX.acquire();
			
					readOrderBookFile();
					
					MUTEX.release();
				}
			} catch (final Exception e) {
				LOGGER.error("error during console data consumig: {}", ExceptionUtils.getMessage(e));
			}
		}		
	}
}
