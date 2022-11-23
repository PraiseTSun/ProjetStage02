package projet.projetstage02.threadpool;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

//Fait par Guillaume Courtemanche et modifié par Massi
public class ThreadPool {

    private Queue<Runnable> tasksToRun = new ArrayDeque<>();
    private Queue<WorkerThread> freeThreads = new ArrayDeque<>();
    private List<WorkerThread> allThreads = new ArrayList<>();
    private boolean terminated = false;

    public void start() {
        Thread thread = new Thread(() -> {
            while (!terminated) {
                if (tasksToRun.size() > 0 && freeThreads.size() > 0) {
                    WorkerThread workerThread = freeThreads.poll();
                    Runnable task = tasksToRun.poll();
                    workerThread.setTask(task);
                }
            }
        });
        thread.start();
    }

    public ThreadPool() {
        this(3);
    }

    public ThreadPool(int nbThreads) {
        int thisId = System.identityHashCode(this);
        for (int i = 0; i < nbThreads; i++) {
            String id = String.format("0x%08X-0x%04X", thisId, i);

            WorkerThread worker = new WorkerThread(this, id);
            allThreads.add(worker);
            freeThreads.add(worker);
            worker.start();
        }
    }

    synchronized public void addTask(Runnable task) {
        if (terminated) throw new RuntimeException("adding tasks to terminated thread pool");
        if (freeThreads.isEmpty()) {
            tasksToRun.add(task);
            return;
        }
        WorkerThread next = freeThreads.poll();
        next.setTask(task);
    }

    synchronized public void onWorkerFree(WorkerThread worker) {
        freeThreads.add(worker);
        notify();
    }

    synchronized public void terminate() {
        for (WorkerThread w : allThreads) {
            w.terminate();
        }
        notify();
    }

}
