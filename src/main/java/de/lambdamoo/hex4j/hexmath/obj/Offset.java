package de.lambdamoo.hex4j.hexmath.obj;

import java.io.Serializable;

/**
 * A class to store offset coordinates: col and row.
 *
 */
public class Offset implements Serializable {
	public Offset(int col, int row) {
		this.col = col;
		this.row = row;
	}

	public boolean greaterThen(Offset h2) {
		return this.col >= h2.col && this.row >= h2.row;
	}

	public boolean lesserThen(Offset h2) {
		return this.col <= h2.col && this.row <= h2.row;
	}

	public final int col;
	public final int row;

	@Override
	public String toString() {
		return "OffsetCoord [col=" + col + ", row=" + row + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
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
		Offset other = (Offset) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
