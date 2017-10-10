package com.concurrent.test;

import java.util.HashMap;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CompletionServiceTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newCachedThreadPool();
		CompletionService<Integer> comp = new ExecutorCompletionService<>(executor);
		for (int i = 0; i < 5; i++) {
			comp.submit(new Task());
		}
		executor.shutdown();
		int count = 0, index = 1;
		while (count < 5) {
			Future<Integer> f = comp.poll();
			if (f == null) {
				System.out.println(index + " 没发现有完成的任务");
			} else {
				System.out.println(index + "产生了一个随机数: " + f.get());
				count++;
			}
			index++;
			TimeUnit.MILLISECONDS.sleep(500);
		}
	}
}
