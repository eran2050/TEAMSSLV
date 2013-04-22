package util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.addesc.AdDescDAO;
import dao.ads.AdsDAO;
import dao.users.UsersDAO;

@Aspect
public class LoggingAspect {

	private Logger logger;

	@Before("execution(* dao.UsersDAO.getUserById(..))")
	public void logBeforeGetUserById(JoinPoint joinPoint) {

		logger = LoggerFactory.getLogger(UsersDAO.class);
		logger.info("Authenticating action by: "
				+ joinPoint.getSignature().getName());

	}

	@Before("execution(* dao.AdsDAO.deleteById(..))")
	public void logAroundAdsDaoDeleteById(JoinPoint joinPoint) {

		logger = LoggerFactory.getLogger(AdsDAO.class);
		logger.info("Running Delete Ad task: "
				+ joinPoint.getSignature().getName());

	}

	@AfterReturning(pointcut = "execution(* dao.AdDescDAO.deleteById(..))", returning = "result")
	public void logAfterReturningAdsDaoDeleteById(JoinPoint joinPoint,
			boolean result) {

		logger = LoggerFactory.getLogger(AdDescDAO.class);
		if (result) {
			logger.info("Success deleting entry: "
					+ joinPoint.getSignature().getName());
		} else {
			logger.info("Failure deleting entry: "
					+ joinPoint.getSignature().getName());
		}
	}
}
