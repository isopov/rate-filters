package com.sopovs.moradanen.ratefilters;

import static com.sopovs.moradanen.ratefilters.Utils.nanosForSignal;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicFilter implements Filter {
	private final long nanosForSignal;
	private final AtomicLong last;

	public AtomicFilter(int n) {
		this(n, TimeUnit.SECONDS);
	}

	public AtomicFilter(int n, TimeUnit timeUnit) {
		nanosForSignal = nanosForSignal(n, timeUnit);
		last = new AtomicLong(System.nanoTime() - nanosForSignal);
	}

	@Override
	public boolean isSignalAllowed() {
		long lastTime = last.get();
		long currentTime = System.nanoTime();
		if (currentTime - lastTime >= nanosForSignal) {
			return last.compareAndSet(lastTime, currentTime);
		}
		return false;
	}

}