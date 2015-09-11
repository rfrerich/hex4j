package de.lambdamoo.hex4j.search.producer;

import java.util.List;

import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.obj.Hex;

public class HexProducer implements SuccessorProducer<Hex> {

	private BlockedChecker<Hex> checker = null;

	public BlockedChecker<Hex> getChecker() {
		return checker;
	}

	public void setChecker(BlockedChecker<Hex> checker) {
		this.checker = checker;
	}

	@Override
	public List<Hex> getSuccessors(Hex object) {
		List<Hex> result = HexUtil2.neighbors(object);
		for (Hex hex : result) {
			if (checker.isBlocked(hex)) {
				result.remove(hex);
			}
		}

		return result;
	}

}
