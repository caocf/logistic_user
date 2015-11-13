package com.cn.xtouch.fodelforuser.manager;

/**
 * Created by tfl on 2015/7/20.
 */

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadManager {
    private static final Object object = new Object();
    private static ThreadPoolProxy poolProxy;
    public static ThreadPoolProxy getPoolProxy() {
        synchronized (object) {
            if (poolProxy == null) {
                poolProxy = new ThreadPoolProxy(5, 5, 5L);
            }
            return poolProxy;
        }
    }


    public static class ThreadPoolProxy {
        private ThreadPoolExecutor threadPoolExecutor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize,
                               long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable runnable) {
            if (runnable == null) {
                return;
            } else {
                if (threadPoolExecutor == null
                        || threadPoolExecutor.isShutdown()) {
                    threadPoolExecutor = new ThreadPoolExecutor(
                            corePoolSize,
                            maximumPoolSize,
                            keepAliveTime, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>(),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.AbortPolicy());

                }
                threadPoolExecutor.execute(runnable);
            }
        }
    }
}
