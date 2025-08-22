package github.zmz;

import github.zmz.handler.RejectHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    public BlockingQueue<Runnable> taskList = new ArrayBlockingQueue<>(3);

    List<Thread> coreList = new ArrayList<>();

    List<Thread> supportList = new ArrayList<>();

    final int coreSize;
    final int maxSize;
    final long timeout;
    final TimeUnit timeUnit;
    final RejectHandler rejectHandler;

    public ThreadPool(int coreSize, int maxSize, long timeout, TimeUnit timeUnit, RejectHandler rejectHandler) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.rejectHandler = rejectHandler;
    }

    public void execute(Runnable runnable) {
        if (coreList.size() < coreSize) {
            Thread coreThread = new CoreThread();
            coreThread.start();
            coreList.add(coreThread);
        }

        if (!taskList.offer(runnable)) {
            if (coreList.size() + supportList.size() < maxSize) {
                Thread supportThread = new SupportThread();
                supportThread.start();
                supportList.add(supportThread);
            }

            if (!taskList.offer(runnable)) {
                rejectHandler.reject(runnable, this);
            }
        }

    }

    public void shutdown() throws InterruptedException{
//        if (!taskList.isEmpty()) {
//
//        }

        for (Thread thread : coreList) {
            if(!thread.isInterrupted()) {
                thread.interrupt();
            }

        }
    }

    class CoreThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable runnable = taskList.take();
                    runnable.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class SupportThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable runnable = taskList.poll(timeout, timeUnit);
                    if (runnable == null) {
                        break;
                    }
                    runnable.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
