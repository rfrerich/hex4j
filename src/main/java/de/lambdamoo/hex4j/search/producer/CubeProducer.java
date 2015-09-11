package de.lambdamoo.hex4j.search.producer;

import java.util.List;

import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.obj.Cube;

public class CubeProducer implements SuccessorProducer<Cube> {

	@Override
	public List<Cube> getSuccessors(Cube object) {
		return HexUtil2.neighbors(object);
	}

}
