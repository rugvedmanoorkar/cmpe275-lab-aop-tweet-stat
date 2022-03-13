package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

@Aspect
@Order(3)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) {
		System.out.printf("Permission check before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
	}

	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void beforeVoid(JoinPoint joinPoint) throws IllegalArgumentException{
		System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		if(args[0] == null || args[1] == null || args[0].toString() == "" || args[1].toString() == "")
			throw new IllegalArgumentException("Thers is a null Argument. Please validate");
		if(args[0] == args[1])
			throw new IllegalArgumentException("Users cannot be same while following");
	}

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void beforeTweet(JoinPoint joinPoint) throws IllegalArgumentException{
		System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		if(args[0] == null || args[1] == null || (String)args[0] == "" || (String)args[1] == "")
			throw new IllegalArgumentException("Thers is a null Argument. Please validate");
		if(((String) args[1]).length() > 140)
			throw new IllegalArgumentException("The length of Message is more than 140");
	}

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.reply(..))")
	public void beforeReply(JoinPoint joinPoint) throws IllegalArgumentException{
		System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		if(args[0] == null || args[0].toString() == "" || args[1] == null || args[1].toString() == "" || args[2] == null || args[2].toString() == "")
			throw new IllegalArgumentException("Thers is a null Argument. Please validate");
	}


	
}
