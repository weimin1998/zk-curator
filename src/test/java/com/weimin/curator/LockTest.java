package com.weimin.curator;

import java.util.ArrayList;
import java.util.List;

public class LockTest {
    public static void main(String[] args) throws InterruptedException {
        Mock12306 mock12306 = new Mock12306();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(mock12306);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
