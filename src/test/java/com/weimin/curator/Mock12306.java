package com.weimin.curator;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Mock12306 implements Runnable{

    private int count = 100;

    private final InterProcessMutex lock;

    public Mock12306(){
        String connectString = "192.168.61.105:2181";
        int sessionTimeoutMs = 60*1000;
        int connectionTimeoutMs = 15*1000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);

        CuratorFramework client = CuratorFrameworkFactory.newClient(
                connectString,
                sessionTimeoutMs,
                connectionTimeoutMs,
                retryPolicy
        );
        client.start();
        lock = new InterProcessMutex(client,"/lock");
    }

    @Override
    public void run() {
        try {
            lock.acquire();
            if(count>0){
                System.out.println(Thread.currentThread().getName()+": " +count);
                count--;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
