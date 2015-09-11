package de.lambdamoo.hex4j.search.producer;

public interface BlockedChecker<T> {
	boolean isBlocked(T object);
}
