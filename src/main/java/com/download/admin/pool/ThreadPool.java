package com.download.admin.pool;

import com.download.admin.utils.FileUtil;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author dengchao
 * @date 2019/12/4 23:25
 */
public class ThreadPool {
    private static Map<String, String> savedFiles = FileUtil.getFiles();

    private static ExecutorService pool;

    public static void main(String[] args) throws InterruptedException {
        //maximumPoolSize设置为2 ，拒绝策略为AbortPolic策略，直接抛出异常
        pool = new ThreadPoolExecutor(1,
                2,
                1000,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        Integer head = savedFiles
                .keySet()
                .stream()
                .filter(index->index.startsWith("1"))
                .map(Integer::parseInt).min(Comparator.comparingInt(a -> a))
                .get();
        for (int index = head; index > 1; index--) {
            if (savedFiles.containsKey(String.valueOf(index))) {
                System.out.println("已存在:" + savedFiles.get(String.valueOf(index)));
                continue;
            }
            Thread.sleep(20);
            new ThreadTask(index).run();
        }
    }
}