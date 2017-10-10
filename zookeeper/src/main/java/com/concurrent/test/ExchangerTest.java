package com.concurrent.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Exchanger;

public class ExchangerTest {

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		Exchanger<Integer> exchange = new Exchanger<>();
		Worker worker1 = new Worker(1, 11, exchange);
		Worker worker2 = new Worker(2, 22, exchange);
		Worker worker3 = new Worker(3, 33, exchange);
		Worker worker4 = new Worker(4, 44, exchange);
		worker1.start();
		worker2.start();
		worker3.start();
		worker4.start();
	}

	static class Worker extends Thread {
		int i, j;
		Exchanger<Integer> exchange;

		public Worker(int i, int j, Exchanger<Integer> exchange) {
			this.i = i;
			this.j = j;
			this.exchange = exchange;
		}

		public void run() {
			try {
				System.out.println("i:" + i + ",j:" + j);
				int k = exchange.exchange(j);
				System.out.println("i:" + i + ",j:" + j + ",k:" + k);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}