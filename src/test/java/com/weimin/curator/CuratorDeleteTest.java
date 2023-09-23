package com.weimin.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.junit.Test;

import static com.weimin.curator.CuratorCreateTest.getConnection;

public class CuratorDeleteTest {

    @Test
    public void testDeleteNode() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 删除单个节点
        connection.delete().forPath("/app1");

        connection.close();
    }


    @Test
    public void testDeleteMultiNode() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 删除多级节点
        connection.delete().deletingChildrenIfNeeded().forPath("/app3/p1");

        connection.close();
    }

    @Test
    public void testDeleteMustSuccess() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // guaranteed
        // 防止网络波动
        connection.delete().guaranteed().forPath("/app3");

        connection.close();
    }

    @Test
    public void testDeleteWithCallback() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        connection.delete().guaranteed().inBackground(new BackgroundCallback() {
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println("/app deleted..");
                System.out.println(event);
                // CuratorEventImpl{type=DELETE, resultCode=0, path='/app', name='null', children=null, context=null, stat=null, data=null, watchedEvent=null, aclList=null, opResults=null}
            }
        }).forPath("/app");

        connection.close();
    }


}
