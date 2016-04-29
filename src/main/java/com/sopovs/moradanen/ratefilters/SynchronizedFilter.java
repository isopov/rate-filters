package com.sopovs.moradanen.ratefilters;

import static com.sopovs.moradanen.ratefilters.Utils.nanosForSignal;

import java.util.concurrent.TimeUnit;

public class SynchronizedFilter implements Filter {
	private final long nsForSignal;
	private long last;

	public SynchronizedFilter(int n) {
		this(n, TimeUnit.SECONDS);
	}

	public SynchronizedFilter(int n, TimeUnit timeUnit) {
		nsForSignal = nanosForSignal(n, timeUnit);
		last = System.nanoTime() - nsForSignal;
	}

	@Override
	public synchronized boolean isSignalAllowed() {
		long nanoTime = System.nanoTime();
		if (nanoTime - last >= nsForSignal) {
			last = nanoTime;
			return true;
		}
		return false;
	}
}