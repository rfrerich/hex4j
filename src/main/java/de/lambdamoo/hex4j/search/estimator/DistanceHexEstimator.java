package de.lambdamoo.hex4j.search.estimator;

import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.obj.Hex;

public class DistanceHexEstimator implements Estimator<Hex> {
	@Override
	public int estimate(Hex current, Hex goal) {
		return HexUtil2.distance(current, goal);
	}
}
