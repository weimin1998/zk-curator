package com.weimin.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

import static com.weimin.curator.CuratorCreateTest.getConnection;

public class CuratorGetTest {

    @Test
    public void testGet() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 查询节点的数据
        byte[] bytes = connection.getData().forPath("/app");

        System.out.println(new String(bytes));

        connection.close();
    }




    @Test
    public void testLs() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 查询节点的子节点
        // ls
        List<String> list = connection.getChildren().forPath("/");
        System.out.println(list);

        connection.close();
    }

    @Test
    public void testNodeStatus() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 获取节点数据，和节点的状态信息
        // get -s /path
        Stat stat = new Stat();
        byte[] bytes = connection.getData().storingStatIn(stat).forPath("/app");

        System.out.println(new String(bytes));
        System.out.println(stat);

        connection.close();
    }
}
