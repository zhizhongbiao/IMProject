package com.example.alv_chi.improject.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Alv_chi on 2017/2/16.
 */

public class ThreadUtil {

    private static ExecutorService executorService;

    public static void executeThreadTask(Runnable task) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(20);
        }
        executorService.submit(task);
    }
}
