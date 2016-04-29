package com.sopovs.moradanen.ratefilters;

public interface Filter {
	boolean isSignalAllowed();

	default void shutdown() {
		// no code
	}
}
