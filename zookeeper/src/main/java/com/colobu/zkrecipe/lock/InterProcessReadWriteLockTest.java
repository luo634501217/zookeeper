package com.colobu.zkrecipe.lock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * InterProcessMutex 
 * @author nike
 *
 */
public class InterProcessReadWriteLockTest {
    private static final int N = 10;
    private static final String PATH = "/examples/locks";

    public static void main(String[] args) throws Exception {
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();

            ExecutorService service = Executors.newFixedThreadPool(N);
            InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, PATH);

            for (int i = 0; i < N; ++i) {
                final AtomicInteger index = new AtomicInteger(i);
                Callable<Void> task = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                    	if(index.get()/5==0){
	                    	InterProcessMutex readLock = lock.readLock();
	                    	readLock.acquire();
	                    	System.out.println(readLock.getParticipantNodes());
	                        readLock.release();
	                        System.out.println("Client #" + index + " readLock");
                    	}
                    	else{
                    		InterProcessMutex readLock = lock.writeLock();
	                    	readLock.acquire();
	                    	System.out.println(readLock.getParticipantNodes());
	                        readLock.release();
	                        System.out.println("Client #" + index + " writeLock");
                    	}
                        return null;
                    }
                };
                service.submit(task);
            }
            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);
        }
		finally {
			
		}

    }

}