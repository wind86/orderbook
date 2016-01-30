package com.orderbook.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.eventbus.EventBus;
import com.orderbook.config.OrderBookConfiguration;
import com.orderbook.io.OrderBookFileCommandLoader;
import com.orderbook.io.OrderBookInteractiveConsole;
import com.orderbook.io.OrderCommandLoader;

public class OrderBookCLI {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookCLI.class);
	
	private ApplicationContext applicationContext;	
	private Options options;
	
	private OrderCommandLoader loader;
	
	public OrderBookCLI(String[] args) {
		applicationContext = loadApplicationContext();
		options = new Options();
		
		options.addOption("h", "help", false, "show help");
	    options.addOption("c", "console", false, "interactive console to input order commands (used by default)");
	    options.addOption("f", "file", true, "source file with order commands (one time loading, not interactive, useful for testing)");
		
		parseArguments(args);
	}

	public static void main(String[] args) {
		new OrderBookCLI(args).run();
	}

	public void run() {
		new Thread(loader).start();
	}

	private AnnotationConfigApplicationContext loadApplicationContext() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(OrderBookConfiguration.class);

		applicationContext.refresh();
		applicationContext.start();
		
		return applicationContext;
	}
	
	private void parseArguments(String[] args) {
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				help();
			}
			
			loader = applicationContext.getBean(OrderBookInteractiveConsole.class);
			
			if (cmd.hasOption("f")) {
				String fileLocation = cmd.getOptionValue("f");
				
				if (StringUtils.isBlank(fileLocation)) {
					System.err.println("missed order commands file location");
					help();
				}
				
				EventBus eventBus = applicationContext.getBean(EventBus.class);
				loader = new OrderBookFileCommandLoader(fileLocation, eventBus);
			}			
		} catch (ParseException e) {
			LOGGER.error("Failed to parse comand line properties", e);
			help();
		}
	}

	private void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("usage", options);
		System.exit(0);
	}
}
