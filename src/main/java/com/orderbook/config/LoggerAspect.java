package com.orderbook.config;

import java.util.Arrays;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggerAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);
	
	@Around("@annotation(com.google.common.eventbus.Subscribe)")
	public void logEventHandleCall(final ProceedingJoinPoint proceedingJoinPoint) {
		final String args =  Arrays.toString(proceedingJoinPoint.getArgs());
		final Class<?> calledClass = proceedingJoinPoint.getSignature().getDeclaringType(); 
		final String methodName = proceedingJoinPoint.getSignature().getName();

		LOGGER.info("start handling event {}.{} with args {}", calledClass, methodName, args);
		
        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            LOGGER.error("{}.{}({}): {}", calledClass, methodName, args, ExceptionUtils.getMessage(e));
        } finally {
			LOGGER.info("finish handling event {}.{} with args {}", calledClass, methodName, args);
		}
	}
	
	@Around("execution(* com.orderbook..*Repository*.*(..))")
	public Object logRepositoryCall(final ProceedingJoinPoint proceedingJoinPoint) {
		final String args =  Arrays.toString(proceedingJoinPoint.getArgs());
		final Class<?> calledClass = proceedingJoinPoint.getSignature().getDeclaringType(); 
		final String methodName = proceedingJoinPoint.getSignature().getName();
		
		LOGGER.info("start handling call {}.{} with args {}", calledClass, methodName, args);
		
		Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            LOGGER.error("{}.{}({}): {}", calledClass, methodName, args, ExceptionUtils.getMessage(e));
        } finally {
			LOGGER.info("finish handling call {}.{} with args {}", calledClass, methodName, args);
		}
        return result;
	}
}
