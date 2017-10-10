package com.concurrent.test;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest2 {

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(2);// 两个工人的协作
		Worker worker1 = new Worker(latch);
		Worker worker2 = new Worker(latch);
		worker1.start();//
		worker2.start();//
		latch.await();// 等待所有工人完成工作
		System.out.println("all work done");
	}

	static class Worker extends Thread {
		CountDownLatch latch;

		public Worker(CountDownLatch latch) {
			this.latch = latch;
		}

		public void run() {
			System.out.println(latch.getCount() + ":do");
			latch.countDown();// 工人完成工作，计数器减一
		}
	}

}