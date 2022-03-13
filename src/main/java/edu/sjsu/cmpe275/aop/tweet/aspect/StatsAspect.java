package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

import java.util.UUID;

@Aspect
@Order(2)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Autowired TweetStatsServiceImpl stats;
	
	@After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void dummyAfterAdvice(JoinPoint joinPoint) {
		System.out.printf("After the execution of the method %s\n", joinPoint.getSignature().getName());
		//stats.resetStats();
	}
	
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) {
		System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
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



	@AfterReturning(pointcut="execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..)) && args(user,message)",returning="retVal")
	public void afterTweet(JoinPoint jp, String user, String message, UUID retVal) {
		stats.addTweet(user, message, retVal);

	}

	@AfterReturning(pointcut="execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.reply(..)) && args(user,originalMessage,message)",returning="newMessage")
	public void afterReply(JoinPoint jp, String user, UUID originalMessage, String message, UUID newMessage) {
		System.out.println("in After Returning for reply");
		stats.reply(user, originalMessage, message, newMessage);
	}

	@AfterReturning(pointcut="execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..)) && args(follower,followee)")
	public void afterFollow(JoinPoint jp, String follower, String followee) {
		stats.follow(follower, followee);
	}

	@AfterReturning(pointcut="execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..)) && args(user,follower)")
	public void afterBlock(JoinPoint jp, String user, String follower) {
		stats.block(user, follower);
	}

	@AfterReturning(pointcut="execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.like(..)) && args(user,messageId)")
	public void afterLike(JoinPoint jp, String user, UUID messageId) {
		stats.like(user, messageId);
	}
	
}
