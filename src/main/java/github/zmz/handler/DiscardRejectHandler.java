package github.zmz.handler;

import github.zmz.ThreadPool;

import java.util.concurrent.BlockingQueue;

public class DiscardRejectHandler implements RejectHandler{
    @Override
    public void reject(Runnable rejectTask, ThreadPool threadPool) {
        BlockingQueue<Runnable> taskList = threadPool.taskList;
        taskList.poll();

        threadPool.execute(rejectTask);
    }
}
