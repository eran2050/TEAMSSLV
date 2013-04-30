package net.voaideahost.sslv.common;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {

	private Logger	logger	= LoggerFactory.getLogger(getClass());

	@Before ("execution(* dao.UsersDAO.getUserById(..))")
	public void logBeforeGetUserById(JoinPoint joinPoint) {

		logger.info("Authenticating action by: "
				+ joinPoint.getSignature().getName());

	}

	@Before ("execution(* dao.AdsDAO.deleteById(..))")
	public void logAroundAdsDaoDeleteById(JoinPoint joinPoint) {

		logger.info("Running Delete Ad task: "
				+ joinPoint.getSignature().getName());

	}

	@AfterReturning (pointcut = "execution(* dao.AdDescDAO.deleteById(..))", returning = "result")
	public void logAfterReturningAdsDaoDeleteById(JoinPoint joinPoint,
			boolean result) {

		if (result)
			logger.info("Success deleting entry: "
					+ joinPoint.getSignature().getName());
	}

	@AfterThrowing (pointcut = "execution(* (dao, util).*", throwing = "e")
	public void logAfterAnyMethodThrownException(JoinPoint joinPoint,
			Throwable e) {

		Signature signature = joinPoint.getSignature();
		String methodName = signature.getName();
		String stuff = signature.toString();
		String arguments = Arrays.toString(joinPoint.getArgs());

		logger.error("Method: " + methodName + ", Argument: " + arguments
				+ ", " + ", Signature: " + stuff + ", Exception: "
				+ e.getMessage());
	}
}
