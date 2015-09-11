package de.lambdamoo.hex4j.hexmath.obj;

import java.io.Serializable;

/**
 * Same like cube with double values, necessary for calculations.
 *
 */
public class FractionalCube implements Serializable {
	public double x;
	public double y;
	public double z;

	public FractionalCube(double x, double y, double z) {
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
