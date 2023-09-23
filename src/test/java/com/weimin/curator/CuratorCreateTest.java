package com.weimin.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class CuratorCreateTest {

    @Test
    public void newConnect(){

        /**
         * Create a new client
         *
         * @param connectString       list of servers to connect to; 连接字符串，ip+port，集群的话，用逗号隔开；
         * @param sessionTimeoutMs    session timeout; 会话过期时间；
         * @param connectionTimeoutMs connection timeout; 连接超时时间
         * @param retryPolicy         retry policy to use;
         * @return client
         */

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

        // do something

        client.close();
    }

    @Test
    public void connectBuilder(){

        /**
         * Create a new client
         *
         * @param connectString       list of servers to connect to; 连接字符串，ip+port，集群的话，用逗号隔开；
         * @param sessionTimeoutMs    session timeout; 会话过期时间；
         * @param connectionTimeoutMs connection timeout; 连接超时时间
         * @param retryPolicy         retry policy to use;
         * @return client
         */

        String connectString = "192.168.61.105:2181";
        int sessionTimeoutMs = 60*1000;
        int connectionTimeoutMs = 15*1000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(retryPolicy)
                .build();

        client.start();

        // do something

        client.close();
    }

    @Test
    public void namespace(){
        String connectString = "192.168.61.105:2181";
        int sessionTimeoutMs = 60*1000;
        int connectionTimeoutMs = 15*1000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);
        String ns = "weimin";


        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(retryPolicy)
                .namespace(ns) // 默认以ns为根目录
                .build();

        client.start();

        // do something

        client.close();
    }

    public static CuratorFramework getConnection(){
        String connectString = "192.168.61.105:2181";
        int sessionTimeoutMs = 60*1000;
        int connectionTimeoutMs = 15*1000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);

        return CuratorFrameworkFactory.newClient(
                connectString,
                sessionTimeoutMs,
                connectionTimeoutMs,
                retryPolicy
        );
    }

    @Test
    public void createNode() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 创建节点的时候如果不指定数据，默认存放的是客户端的ip地址
        String path = connection.create().forPath("/app");

        System.out.println(path);

        connection.close();
    }

    @Test
    public void createNodeWithData() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        String path = connection.create().forPath("/app1","hello weimin".getBytes());

        System.out.println(path);

        connection.close();
    }

    @Test
    public void createNodeWithNodeType() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 临时节点
        String path = connection.create().withMode(CreateMode.EPHEMERAL).forPath("/app2","hello weimin".getBytes());

        System.out.println(path);

        connection.close();
    }

    @Test
    public void createNodeWithMultiPath() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 多级目录
        // creatingParentsIfNeeded
        String path = connection.create().creatingParentsIfNeeded().forPath("/app3/p1","hello weimin".getBytes());

        System.out.println(path);

        connection.close();
    }
}
