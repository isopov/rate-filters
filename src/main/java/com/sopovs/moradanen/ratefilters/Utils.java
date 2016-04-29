package com.sopovs.moradanen.ratefilters;

import java.nio.channels.IllegalSelectorException;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Preconditions;

public final class Utils {
	private Utils() {
		throw new IllegalSelectorException();
	}

	public static long nanosForSignal(int n, TimeUnit timeUnit) {
		long result = timeUnit.toNanos(1) / n;
		Preconditions.checkState(result * n == timeUnit.toNanos(1));
		return result;
	}

	public static Filter createFilter(String filterType) {
		switch (filterType) {
		case "SynchronizedFilter":
			return new SynchronizedFilter(10);
		case "GuavaFilter":
			return new GuavaFilter(10);
		case "AtomicFilter":
			return new AtomicFilter(10);
		case "SynchronizedDequeFilter":
			return new SynchronizedDequeFilter(10);
		case "SingleSchedulerFilter":
			return new SingleSchedulerFilter(10);
		default:
			throw new IllegalStateException();
		}
	}

}
