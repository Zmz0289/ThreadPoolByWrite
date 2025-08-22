package github.zmz.handler;

import github.zmz.ThreadPool;

public class ExceptionRejectHandler implements RejectHandler{
    @Override
    public void reject(Runnable rejectTask, ThreadPool threadPool) {
        throw new RuntimeException("线程池满了");
    }
}
