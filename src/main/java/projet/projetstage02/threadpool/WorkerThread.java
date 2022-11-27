package projet.projetstage02.threadpool;

public class WorkerThread extends Thread {

    ThreadPool parent;
    private boolean isRunning = false;
    private Runnable task = null;

    public WorkerThread(ThreadPool parent, String id) {
        super(id);
        this.parent = parent;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            if (this.task != null) {
                this.task.run();
                this.task = null;
                parent.onWorkerFree(this);
            }

            synchronized (this) {
                if (isRunning) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    synchronized public void setTask(Runnable task) {
        if (this.task != null) {
            throw new RuntimeException(Thread.currentThread().getName() + " - Invalid state - already running a task");
        }

        this.task = task;
        notify();
    }

    synchronized public void terminate() {
        isRunning = false;
        this.interrupt();
    }

}
