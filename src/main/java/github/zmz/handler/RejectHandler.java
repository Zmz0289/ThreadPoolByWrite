package github.zmz.handler;

import github.zmz.ThreadPool;

public interface RejectHandler {

    void reject(Runnable rejectTask, ThreadPool threadPool);


}
