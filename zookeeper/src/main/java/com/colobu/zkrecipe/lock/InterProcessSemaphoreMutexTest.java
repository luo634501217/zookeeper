package com.colobu.zkrecipe.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 不可重入锁
 * @author nike
 *
 */
public class InterProcessSemaphoreMutexTest {

    private static final String PATH = "/examples/lock";

    public static void main(String[] args) throws Exception {
    	
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();

            InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(client, PATH);
            Runnable task1 = new Runnable() {
                @Override
                public void run() {
                	try {
                		System.out.println("start acquire 1");
						lock.acquire();
						System.out.println("end acquire 1");
						
						System.out.println("start acquire 2");
						lock.acquire();
						System.out.println("end acquire 2");
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            };
            Runnable task2 = new Runnable() {
                @Override
                public void run() {
                	try {
                		Thread.sleep(3000l);
                		System.out.println("start release 1");
						lock.release();
						System.out.println("end release 1");
						Thread.sleep(2000l);
						System.out.println("start release 2");
						lock.release();
						System.out.println("end release 2");
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            };
            ExecutorService service = Executors.newFixedThreadPool(2);
            service.submit(task1);
            service.submit(task2);
            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        }
      
        
		finally {
			
		}
        

    }

}
