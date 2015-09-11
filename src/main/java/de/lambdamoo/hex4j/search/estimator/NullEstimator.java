package de.lambdamoo.hex4j.search.estimator;

public class NullEstimator<T> implements Estimator<T> {

	@Override
	public int estimate(T current, T goal) {
		return 1;
	}

}
