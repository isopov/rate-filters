package com.sopovs.moradanen.ratefilters;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class SynchronizedDequeFilter implements Filter {
    private final int n;
    private final long time;
    private final Deque<Long> acquisitions = new ArrayDeque<>();

    public SynchronizedDequeFilter(int n) {
        this(n, TimeUnit.SECONDS);
    }

    public SynchronizedDequeFilter(int n, TimeUnit timeUnit) {
        this.n = n;
        this.time = timeUnit.toNanos(1);
    }

    @Override
    public synchronized boolean isSignalAllowed() {
        long currentTime = System.nanoTime();
        if (acquisitions.size() < n) {
            acquisitions.addLast(currentTime);
            return true;
        }
        if (currentTime - acquisitions.getFirst() >= time) {
            acquisitions.removeFirst();
            acquisitions.addLast(currentTime);
            return true;
        }
        return false;
    }

    @Override
    public void shutdown() {
        acquisitions.clear();
    }

    @Override
    public boolean burstsAllowed() {
        return true;
    }

}