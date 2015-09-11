package de.lambdamoo.hex4j.hexmath.obj;

import java.io.Serializable;

/**
 * This is the class for a cube that represents the diagonal axis: x, y, z of a
 * hexagon
 *
 */
public class Cube implements Serializable {
	public int x;
	public int y;
	public int z;

	public Cube(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return "Cube [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

}
