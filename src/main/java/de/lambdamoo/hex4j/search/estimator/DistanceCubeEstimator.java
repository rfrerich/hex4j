package de.lambdamoo.hex4j.search.estimator;

import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.obj.Cube;

public class DistanceCubeEstimator implements Estimator<Cube> {
	@Override
	public int estimate(Cube current, Cube goal) {
		return HexUtil2.distance(current, goal);
	}
}
