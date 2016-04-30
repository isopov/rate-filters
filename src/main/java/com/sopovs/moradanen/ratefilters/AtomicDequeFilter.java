package com.sopovs.moradanen.ratefilters;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicDequeFilter implements Filter {
    private final long time;
    private final long n;
    private final ConcurrentSkipListMap<Long, Long> acquisitions = new ConcurrentSkipListMap<>();
    private final AtomicLong counter = new AtomicLong();

    public AtomicDequeFilter(int n) {
        this(n, TimeUnit.SECONDS);
    }

    public AtomicDequeFilter(int n, TimeUnit timeUnit) {
        this.n = n;
        this.time = timeUnit.toNanos(1);
    }

    @Override
    public boolean isSignalAllowed() {
        long index = counter.incrementAndGet();
        if (index <= n) {
            acquisitions.put(index, System.nanoTime());
            return true;
        } else {
            long current = System.nanoTime();
            Entry<Long, Long> replaceEntry = acquisitions.firstEntry();
            while (current - replaceEntry.getValue() >= time) {
                acquisitions.put(index, current);
                if (acquisitions.remove(replaceEntry.getKey()) != null) {
                    return true;
                } else {
                    acquisitions.remove(index);
                    replaceEntry = acquisitions.firstEntry();
                }

            }
            return false;
        }
    }

    @Override
    public void shutdown() {
        counter.set(0L);
        acquisitions.clear();
    }

}