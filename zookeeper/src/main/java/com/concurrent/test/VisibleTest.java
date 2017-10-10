package com.concurrent.test;

import java.util.concurrent.atomic.AtomicInteger;

public class VisibleTest {
	private static boolean ready = false;
	private static String readyStr = "不可见ready";

	// private static AtomicInteger count = new AtomicInteger(0);
	private static int count;

	private static class ReaderThread extends Thread {
		int i;

		public ReaderThread(int i) {
			this.i = i;
		}

		public void run() {
			while (!ready) {
				// System.out.println("线程"+i+"，计数"+count.getAndIncrement());
				System.out.println("线程" + i + "，           计数" + count++);
				Thread.yield();
			}
			System.out.println("线程" + i + readyStr);
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new ReaderThread(i).start();
		}

		ready = true;
		readyStr = "可见ready";

	}
}