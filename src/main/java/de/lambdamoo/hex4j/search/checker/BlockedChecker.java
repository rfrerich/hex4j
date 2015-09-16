package de.lambdamoo.hex4j.search.checker;

public interface BlockedChecker<T> {
	boolean isBlocked(T object);
}
