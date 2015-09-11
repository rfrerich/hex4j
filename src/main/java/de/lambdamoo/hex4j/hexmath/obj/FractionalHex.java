package de.lambdamoo.hex4j.hexmath.obj;

import java.io.Serializable;

/**
 * Same like Hex necessary for calculations.
 * 
 */
public class FractionalHex implements Serializable {
	public FractionalHex(double q, double r, double s) {
		this.q = q;
		this.r = r;
		this.s = s;
	}

	public FractionalHex(double q, double r) {
		this.q = q;
		this.r = r;
		this.s = -q - r;
	}

	public double q;
	public double r;
	public double s;

	@Override
	public String toString() {
		return "FractionalHex [q=" + q + ", r=" + r + ", s=" + s + "]";
	}

}
