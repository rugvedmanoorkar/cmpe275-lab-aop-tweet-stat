package edu.sjsu.cmpe275.aop.tweet.aspect;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.security.AccessControlException;
import java.util.UUID;

@Aspect
@Order(0)
public class AccessControlAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */
	@Autowired
	TweetStatsServiceImpl stats;

	@Around("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public int dummyAdviceOne(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Integer result = null;
		try {
			result = (Integer) joinPoint.proceed();
			System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), result);
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.printf("Aborted the executuion of the metohd %s\n", joinPoint.getSignature().getName());
			throw e;
		}
		return result.intValue();
	}

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.reply(..))")
	public void beforeReply(JoinPoint joinPoint) throws AccessControlException {
		System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		boolean validationCheck =  stats.validateReply((String)args[0],(UUID)args[1],(String)args[2]);
		if (!validationCheck){
			throw new AccessControlException("Cannot reply to User");
		}
		System.out.printf("Access Control check Completed successfully for user %s\n", (String)args[0]);
	}

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.like(..))")
	public void beforeLike(JoinPoint joinPoint) throws AccessControlException {
		System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		boolean validationCheck =  stats.validateLike((String)args[0],(UUID)args[1]);
		if (!validationCheck){
			throw new AccessControlException("Cannot like the message. Validation failed");
		}
		System.out.printf("Access Control check Completed successfully for user %s\n", (String)args[0]);
	}



}
