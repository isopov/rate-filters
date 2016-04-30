package com.sopovs.moradanen.ratefilters;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscreteSchedulerFilter implements Filter {

    private final AtomicInteger counter = new AtomicInteger();
    private final int n;
    private final TimeUnit unit;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public DiscreteSchedulerFilter(int n) {
        this(n, TimeUnit.SECONDS);
    }

    public DiscreteSchedulerFilter(int n, TimeUnit unit) {
        this.n = n;
        this.unit = unit;
    }

    @Override
    public boolean isSignalAllowed() {
        boolean res = counter.getAndUpdate(curr -> curr == n ? curr : curr + 1) < n;
        if (res) {
            scheduler.schedule(counter::decrementAndGet, 1, unit);
        }
        return res;
    }

    @Override
    public void shutdown() {
        counter.set(0);
        scheduler.shutdownNow();
    }

    @Override
    public boolean burstsAllowed() {
        return true;
    }

}