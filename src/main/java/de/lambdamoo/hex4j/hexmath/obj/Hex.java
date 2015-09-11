package de.lambdamoo.hex4j.hexmath.obj;

import java.io.Serializable;

/**
 * This is the data class for representing the hex tiles. The representation is
 * a cube system that can be translated to other coordinate systems.
 *
 * pick this: q = x and r = z. You can think of q as “column” and r as “row”.
 */
public class Hex implements Serializable {
	public int q; // = x = column
	public int r; // = z = row
	public int s; // = y = -x-z

	public Hex(int q, int r) {
		super();
		this.q = q;
		this.r = r;
		this.s = -q - r;
	}

	public Hex(int q, int r, int s) {
		super();
		this.q = q;
		this.r = r;
		this.s = s;
	}

	public boolean greaterThen(Hex h2) {
		return this.q >= h2.q && this.r >= h2.r;
	}

	public boolean lesserThen(Hex h2) {
		return this.q <= h2.q && this.r <= h2.r;
	}

	/**
	 * This method checks whether the coordinates defined by q, r, s are valid
	 * 
	 * @return
	 */
	public boolean validate() {
		return q + r + s == 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + q;
		result = prime * result + r;
		result = prime * result + s;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hex other = (Hex) obj;
		if (q != other.q)
			return false;
		if (r != other.r)
			return false;
		if (s != other.s)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Hex [q=" + q + ", r=" + r + ", s=" + s + "]";
	}

	public Hex copy() {
		return new Hex(q, r);
	}

}
