package de.lambdamoo.hex4j.search.estimator;

import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.layout.HexMap;
import de.lambdamoo.hex4j.hexmath.obj.Offset;

public class DistanceOffsetEstimator implements Estimator<Offset> {
	private HexMap map = null;

	public DistanceOffsetEstimator(HexMap map) {
		super();
		this.map = map;
	}

	@Override
	public int estimate(Offset current, Offset goal) {
		return HexUtil2.distance(current, goal, map.getHexLayout().getLayout(), map.getHexLayout().getOrientation());
	}
}
