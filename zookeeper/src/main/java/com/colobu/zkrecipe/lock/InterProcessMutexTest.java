package com.colobu.zkrecipe.lock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * InterProcessMutex 
 * @author nike1、
 * 
 * 互斥锁
	实现原理
        n个线程、进程、或者服务，需要并发获取锁时，将请求在ZooKeeper上请求创建结点，zookeeper通过Paxos算法，创建n个临时的顺序结点(base path)/xxx-lock-${no}。${no}最小的成为当前的持锁者，其它进程watch比它自己no小的第一个的进程对应的结点，比如2 watch 1, 3 watch 2。当前持锁者释放锁后，watch no节点的进程就会收到ZooKeeper的通知，它成为新的持锁者。
 * 
 *
 */
public class InterProcessMutexTest {
    private static final int N = 5;
    private static final String PATH = "/examples/lock";

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        ExecutorService service = Executors.newFixedThreadPool(N);
        InterProcessLock lock = new InterProcessMutex(client, PATH);
        for (int i = 0; i < N; ++i) {
            final int index = i;
            Callable<Void> task = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                	lock.acquire();
                    Thread.sleep(5000l);
                    System.out.println("Client #" + index);
                    lock.release();
                    return null;
                }
            };
            service.submit(task);
        }
        service.shutdown();
    }
}