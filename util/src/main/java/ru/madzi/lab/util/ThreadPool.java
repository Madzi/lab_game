package ru.madzi.lab.util;

import java.util.LinkedList;

/**
 * Пул потоков.
 */
public class ThreadPool extends ThreadGroup {

    private boolean isAlive;

    private LinkedList<Runnable> taskQueue;

    private int threadID;

    private static int threadPoolID;

    /**
     * Создание нового пула потоков.
     *
     * @param numThreads количество потоков в пуле
     */
    public ThreadPool(int numThreads) {
        super("ThreadPool-" + (threadPoolID++));
        setDaemon(true);
        isAlive = true;
        taskQueue = new LinkedList<Runnable>();
        for (int i = 0; i < numThreads; ++i) {
            new PooledThread().start();
        }
    }

    /**
     * Запускает задачу на исполнение в одном из потоков пула.
     *
     * @param task задача
     * @throws IllegalStateException если пул был завершён до запуска
     */
    public synchronized void runTask(Runnable task) throws IllegalStateException {
        if (!isAlive) {
            throw new IllegalStateException();
        }
        if (task != null) {
            taskQueue.add(task);
            notify();
        }
    }

    /**
     * Выбирает первую задачу из очереди.
     *
     * @return задача из очереди или <b>null</b>, если очередь пуста
     * @throws InterruptedException если ожидание было прервано
     */
    protected synchronized Runnable getTask() throws InterruptedException {
        while (taskQueue.isEmpty()) {
            if (!isAlive) {
                return null;
            }
            wait();
        }
        return taskQueue.removeFirst();
    }

    /**
     * Закрывает пул потоков. Все потоки останавливаются.
     * Задачи из очереди не выполняются. Закрытый пул нельзя использовать снова.
     */
    public synchronized void close() {
        if (isAlive) {
            isAlive = false;
            taskQueue.clear();
            interrupt();
        }
    }

    /**
     * Закрывает пул потоков. Все потоки останавливаются.
     * Задачи из очереди выполняются. Закрытй пул нельзя использовать вновь.
     */
    public void join() {
        synchronized (this) {
            isAlive = false;
            notifyAll();
        }
        Thread[] threads = new Thread[activeCount()];
        int count = enumerate(threads);
        for (int i = 0; i < count; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {}
        }
    }

    /**
     * Класс потока, исполняющийся в пуле.
     */
    private class PooledThread extends Thread {

        /**
         * Инициализация потока в пуле.
         */
        public PooledThread() {
            super(ThreadPool.this, "PooledThread-" + (threadID++));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            while (!isInterrupted()) {
                Runnable task = null;
                try {
                    task = getTask();
                } catch (InterruptedException e) {}
                if (task == null) {
                    return;
                }
                try {
                    task.run();
                } catch (Throwable t) {
                    uncaughtException(this, t);
                }
            }
        }

    }

}
