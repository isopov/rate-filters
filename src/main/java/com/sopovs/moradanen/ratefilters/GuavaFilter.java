package com.sopovs.moradanen.ratefilters;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

public class GuavaFilter implements Filter {
	private final RateLimiter limiter;

	public GuavaFilter(int n) {
		this(n, TimeUnit.SECONDS);
	}

	public GuavaFilter(int n, TimeUnit timeUnit) {
		limiter = RateLimiter.create(timeUnit.toSeconds(1) * n);
	}

	@Override
	public boolean isSignalAllowed() {
		return limiter.tryAcquire();
	}
}