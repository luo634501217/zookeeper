package com.concurrent.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		CyclicBarrier latch = new CyclicBarrier(2);// 两个工人的协作
		Worker worker1 = new Worker(latch);
		Worker worker2 = new Worker(latch);
		worker1.start();
		worker2.start();
	}

	static class Worker extends Thread {
		CyclicBarrier latch;

		public Worker(CyclicBarrier latch) {
			this.latch = latch;
		}

		public void run() {
			System.out.println(latch.getParties() + ":strat");
			try {
				// 等待所有工人完成工作
				latch.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println(latch.getParties() + ":end");
		}
	}

}