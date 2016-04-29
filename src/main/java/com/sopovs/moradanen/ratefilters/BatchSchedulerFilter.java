package com.sopovs.moradanen.ratefilters;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchSchedulerFilter implements Filter {
	private final int n;
	private final AtomicInteger count = new AtomicInteger(0);
	private final ScheduledExecutorService scheduler;

	public BatchSchedulerFilter(int n) {
		this(n, TimeUnit.SECONDS);
	}

	public BatchSchedulerFilter(int n, TimeUnit timeUnit) {
		this.n = n;
		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> count.set(0), 1, 1, timeUnit);

	}

	@Override
	public boolean isSignalAllowed() {
		return count.getAndUpdate(curr -> curr == n ? curr : curr + 1) < n;
	}

	@Override
	public void shutdown() {
		scheduler.shutdownNow();
	}

}