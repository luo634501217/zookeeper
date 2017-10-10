package com.forkJoin.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * 继承RecursiveTask 则每个子任务带返回值 继承RecursiveAction 则每个子任务不带返回值
 */
@SuppressWarnings("serial")
public class ForkJoinTest extends RecursiveTask<Long> {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		
		long l = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool(); // 类似线程池，也实现了AbstractExecutorService
		ForkJoinTest task = new ForkJoinTest(1l, 100000000000l); // 新建任务
		Future<Long> result = pool.submit(task); // 将任务提交
		System.out.println("result1 is" + result.get().toString()); // 获取结果
		System.out.println(System.currentTimeMillis()-l);
		
		long l2 = System.currentTimeMillis();
		long sum = add(1l,     100000000000l);
		System.out.println("result2 is" + sum); // 获取结果
		System.out.println(System.currentTimeMillis()-l2);
	}

	private final long index = 1000000000l; // 分割任务的基数
	private long left;
	private long right;

	public ForkJoinTest(long left, long right) {
		this.left = left;
		this.right = right;
	}

	@Override
	protected Long compute() {
		long sum = 0l;
		if (right - left < index) { // 如果任务 小于基数，则直接执行；类似递归的出口
			for (long i = left; i <= right; i++) {
				sum += i;
			}
		} else { // 任务 大于基数，则分割，类似与二分法，也可以更多
			long middle = (right + left) >> 1;
			ForkJoinTest myf1 = new ForkJoinTest(left, middle); // 二分法左边
			ForkJoinTest myf2 = new ForkJoinTest(middle + 1, right); // 二分法右边
			myf1.fork(); // 继续执行，类似递归
			myf2.fork(); // 继续执行，类似递归
			long long1 = myf1.join(); // 等待
			long long2 = myf2.join();
			sum = long1 + long2; // 结果合并
		}
		return sum;
	}
	
	
	public static long add(long left, long right) {
		long sum = 0l;
		for (long i = left; i <= right; i++) {
			sum += i;
		}
		return sum;
	}
}