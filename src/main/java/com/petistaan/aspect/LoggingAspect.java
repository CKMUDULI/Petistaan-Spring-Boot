package com.petistaan.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

	@Pointcut("execution(* com.petistaan.service.impl.*.*(..))")
	public void serviceMethodExpression() {
	}

	@Pointcut("execution(* com.petistaan.repository..*.*(..))")
	public void repositoryMethodExpression() {
	}

	@Before("serviceMethodExpression() || repositoryMethodExpression()")
	public void logBeforeAdvice(JoinPoint joinPoint) {
		log.info("Calling {} with arguments as {}", joinPoint.getSignature(), joinPoint.getArgs());
	}

	@AfterReturning(pointcut = "serviceMethodExpression() || repositoryMethodExpression()", returning = "result")
	public void logAfterReturningAdvice(JoinPoint joinPoint, Object result) {
		result = ((MethodSignature) joinPoint.getSignature()).getReturnType() == void.class ? "void" : result;
		log.info("Returning from {} with result as {}", joinPoint.getSignature(), result);
	}

	@AfterThrowing(pointcut = "serviceMethodExpression() || repositoryMethodExpression()", throwing = "exception")
	public void logAfterThrowingAdvice(JoinPoint joinPoint, Exception exception) {
		log.error("An error occurred in {} with exception as {}", joinPoint.getSignature(), exception.getMessage());
	}

}
