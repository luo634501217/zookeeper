package com.colobu.zkrecipe.barrier;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.bigmouth.common.zookeeper.config.ZooKeeperFactory;

public class DistributedBarrierExample {
    private static final int QTY = 5;
    private static final String PATH = "/examples/barrier";

    public static void main(String[] args){
        try (TestingServer server = new TestingServer()) {
            //CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
            CuratorFramework client = ZooKeeperFactory.get();
            //client.start();

            ExecutorService service = Executors.newFixedThreadPool(QTY);
            DistributedBarrier controlBarrier = new DistributedBarrier(client, PATH);
            controlBarrier.setBarrier();

            for (int i = 0; i < QTY; ++i) {
                final DistributedBarrier barrier = new DistributedBarrier(client, PATH);
                final int index = i;
                Callable<Void> task = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {

                        Thread.sleep((long) (3 * Math.random()));
                        System.out.println("Client #" + index + " waits on Barrier");
                        barrier.waitOnBarrier();
                        System.out.println("Client #" + index + " begins");
                        return null;
                    }
                };
                service.submit(task);
            }

            Thread.sleep(10000);
            System.out.println("all Barrier instances should wait the condition");


            controlBarrier.removeBarrier();


            service.shutdown();
            service.awaitTermination(10, TimeUnit.MINUTES);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}