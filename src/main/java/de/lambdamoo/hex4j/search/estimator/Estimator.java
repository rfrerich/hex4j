package de.lambdamoo.hex4j.search.estimator;

public interface Estimator<T> {

	/**
	 * The estimated cost is used as a priority. The lower the better.
	 */
	int estimate(T current, T goal);

}