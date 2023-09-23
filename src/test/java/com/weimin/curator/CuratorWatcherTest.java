package com.weimin.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.junit.Test;

import static com.weimin.curator.CuratorCreateTest.getConnection;

public class CuratorWatcherTest {

    // 监听单个节点的变化
    @Test
    public void nodeCache() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        final NodeCache nodeCache = new NodeCache(connection, "/app", false);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("node has been changed..");
                byte[] data = nodeCache.getCurrentData().getData();
                System.out.println("node has been changed, new data: " + new String(data));
            }
        });

        //
        nodeCache.start(true);

        while (true) {
        }
    }

    // 监听节点的所有子节点，不包括当前节点，也不包括子节点的子节点
    // 代码一启动，默认会收到 children node has been changed, event: PathChildrenCacheEvent{type=CONNECTION_RECONNECTED, data=null}，这个事件不必关心
    @Test
    public void nodePathChildrenCache() throws Exception {
        CuratorFramework connection = getConnection();
        connection.start();

        // 第三个参数cacheData，如果设置为true，则这个监听器在启动的时候会把所有的子节点当成是新添加的节点，并且以处理add节点事件的方式，获取所有子节点的数据；
        // 如果设置成false，则不会获取数据；
        PathChildrenCache pathChildrenCache = new PathChildrenCache(connection, "/app2", true);
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println("children node has been changed, event: " + pathChildrenCacheEvent);

                // 创建一个新的子节点
                // children node has been changed, event: PathChildrenCacheEvent{type=CHILD_ADDED, data=ChildData{path='/app2/p1', stat=112,112,1681789208374,1681789208374,0,0,0,0,2,0,112, data=[112, 49]}}

                // 修改子节点的数据
                // children node has been changed, event: PathChildrenCacheEvent{type=CHILD_UPDATED, data=ChildData{path='/app2/p1', stat=112,113,1681789208374,1681789333632,1,0,0,0,4,0,112
                //, data=[112, 49, 45, 49]}}

                // 删除子节点
                // children node has been changed, event: PathChildrenCacheEvent{type=CHILD_REMOVED, data=ChildData{path='/app2/p1', stat=112,113,1681789208374,1681789333632,1,0,0,0,4,0,112
                //, data=[112, 49, 45, 49]}}

                PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
                if (type.equals(PathChildrenCacheEvent.Type.CHILD_ADDED) || type.equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
                }


            }
        });

        pathChildrenCache.start();

        while (true) {
        }
    }

    // 监听节点和所有后代节点
    @Test
    public void treeCache(){
        CuratorFramework connection = getConnection();
        connection.start();

        TreeCache treeCache = new TreeCache(connection,"/app3");
        treeCache.getListenable().addListener(new TreeCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {

            }
        });

        while (true) {
        }
    }
}
