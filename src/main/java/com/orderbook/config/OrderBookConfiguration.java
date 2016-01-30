package com.orderbook.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.google.common.eventbus.EventBus;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "com.orderbook")
public class OrderBookConfiguration {

	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocations(new Resource[] { 
				new ClassPathResource("log4j.properties"),
				new ClassPathResource("app.properties"),
				});
		return ppc;
	}
	
	@Bean
	public EventBus eventBus() {
		return new EventBus();
	}
	
	@Bean
	public LoggerAspect eventHandlerLoggerAspect() {
		return new LoggerAspect();
	}
}
