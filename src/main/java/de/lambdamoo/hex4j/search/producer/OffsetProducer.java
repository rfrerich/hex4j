package de.lambdamoo.hex4j.search.producer;

import java.util.Iterator;
import java.util.List;

import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.layout.HexMap;
import de.lambdamoo.hex4j.hexmath.obj.Hex;
import de.lambdamoo.hex4j.hexmath.obj.Offset;

public class OffsetProducer implements SuccessorProducer<Hex> {

	private BlockedChecker<Hex> checker = null;

	public BlockedChecker<Hex> getChecker() {
		return checker;
	}

	private Offset min;

	private Offset max;

	public void setChecker(BlockedChecker<Hex> checker) {
		this.checker = checker;
	}

	public void setSize(Offset min, Offset max) {
		this.min = min;
		this.max = max;
	}

	private HexMap map;

	public OffsetProducer(HexMap map) {
		super();
		this.map = map;
	}

	@Override
	public List<Hex> getSuccessors(Hex object) {
		List<Hex> result = HexUtil2.neighbors(object);
		Iterator<Hex> iter = result.iterator();
		while (iter.hasNext()) {
			Hex hex = (Hex) iter.next();
			boolean blocked = checker != null && checker.isBlocked(hex);
			boolean boundary = HexUtil2.hexInBoundary(hex, min, max, map.getHexLayout().getLayout(), map.getHexLayout().getOrientation());
			if (blocked | !boundary) {
				iter.remove();
			}
		}

		return result;
	}

}
