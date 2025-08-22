package github.zmz;

import github.zmz.handler.DiscardRejectHandler;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        ThreadPool threadPool = new ThreadPool(3, 5, 1, TimeUnit.SECONDS, new DiscardRejectHandler());

        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("线程池执行");
            });
        }

        System.out.println("主线程执行完毕");

        threadPool.shutdown();

        System.out.println("线程池关闭了");
    }

}
