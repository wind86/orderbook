package com.orderbook.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.orderbook.event.handler.EventHandler;

@Component
public class EventHandlerRegistration implements BeanPostProcessor, ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof EventHandler) {
			EventBus eventBus = applicationContext.getBean(EventBus.class);
			eventBus.register(bean);
		}		
		return bean;
	}
}
