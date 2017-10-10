package com.concurrent.test;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

class Task implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		Random rand = new Random();
		TimeUnit.SECONDS.sleep(rand.nextInt(7));
		return rand.nextInt();
	}

}