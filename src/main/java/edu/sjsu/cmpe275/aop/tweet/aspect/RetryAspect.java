package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

import java.io.IOException;

@Aspect
@Order(1)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */

	@Around("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.*tweet(..))")
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



	@Around("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public Object retryModule(ProceedingJoinPoint joinPoint) throws Throwable{
		Object res = null;
		try {
			System.out.printf("Attempting the executuion of the metohd %s for 1st time with result \n", joinPoint.getSignature().getName() );
			res = joinPoint.proceed(joinPoint.getArgs());
			System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), res);
		} catch (IOException e) {
			try {
				System.out.printf("Attempting the executuion of the metohd %s for 2nd time with result \n", joinPoint.getSignature().getName() );
				res = joinPoint.proceed(joinPoint.getArgs());
				System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), res);
			} catch (IOException ex) {
				try {
					System.out.printf("Attempting the executuion of the metohd %s for 3rd time with result \n", joinPoint.getSignature().getName() );
					res = joinPoint.proceed(joinPoint.getArgs());
					System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), res);
				} catch (Throwable exc) {
					throw new IOException();
				}
			} catch (Throwable ex) {

				ex.printStackTrace();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.printf("Aborted the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), res);
			throw e;
		} finally{
			return res;
		}
		/*Integer totalAttempts = 4;
		Integer attempt = 0;
		IOException exception = new IOException();
		try {
			while (attempt < totalAttempts) {
				System.out.println("Attempt Number: " + attempt);
				try {
					joinPoint.proceed();
					System.out.printf("Finished the executuion of the method %s\n", joinPoint.getSignature().getName());
					return ;
				} catch (IOException ex) {
					exception = ex;
				}

			}
			throw  exception;
		}
		catch (Throwable ex){

		}*/
	}

}
