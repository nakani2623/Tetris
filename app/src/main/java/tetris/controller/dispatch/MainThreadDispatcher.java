package tetris.controller.dispatch;

public interface MainThreadDispatcher {
    /**
     * allow worker threads dispatch task on Main/Graphics Thread
     */
    void dispatch(Runnable runnable);
}
