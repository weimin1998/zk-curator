package com.weimin.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import static com.weimin.curator.CuratorCreateTest.getConnection;

public class CuratorSetTest {

    @Test
    public void testSet() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();


        byte[] bytes = connection.getData().forPath("/app");
        System.out.println(new String(bytes));

        connection.setData().forPath("/app","after set".getBytes());

        byte[] bytes1 = connection.getData().forPath("/app");
        System.out.println(new String(bytes1));

        connection.close();
    }

    @Test
    public void testSetByDataVersion() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        Stat stat = new Stat();
        byte[] bytes = connection.getData().storingStatIn(stat).forPath("/app");
        System.out.println("version: "+stat.getVersion());
        System.out.println("old data: "+new String(bytes));

        // 根据节点的数据版本号，修改数据
        // 每次修改，数据版本会+1；

        connection.setData().withVersion(stat.getVersion()).forPath("/app","bbbbb".getBytes());

        byte[] bytes1 = connection.getData().forPath("/app");
        System.out.println("new data: "+new String(bytes1));

        connection.close();
    }

    @Test
    public void testSetByDataVersionFail() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        Stat stat = new Stat();
        byte[] bytes = connection.getData().storingStatIn(stat).forPath("/app");
        System.out.println("version: "+stat.getVersion());
        System.out.println("old data: "+new String(bytes));

        // 根据节点的数据版本号，修改数据
        // 每次修改，数据版本会+1；
        // 修改的时候如果版本号不对，修改失败；

        int badVersion = 0;
        try {
            connection.setData().withVersion(badVersion).forPath("/app","bbbbb".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            assert e.getMessage().contains("KeeperErrorCode = BadVersion for /app");
            connection.close();
        }


    }
}
