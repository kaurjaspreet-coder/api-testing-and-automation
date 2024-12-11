package com.restAssured.project;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListeners implements ITestListener{

	ThreadLocal<String> threadData = new ThreadLocal<>();
	
	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		threadData.set("Test started for: " + result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println(threadData.get() + " - Test passed.");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		System.out.println("Test Failed: " + threadData.get());
		  //System.out.println("Test Failed: " + result.getMethod().getMethodName());
	        Throwable throwable = result.getThrowable();
	        if (throwable != null) {
	            System.err.println("Error Message: " + throwable.getMessage());
	            throwable.printStackTrace();
	        }
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestSkipped(result);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		ITestListener.super.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		threadData.remove();
	}
	

}

