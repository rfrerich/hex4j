package de.lambdamoo.hex4j.hexmath.obj;

/**
 * Point class to represent a pixel points on the screen or canvas.
 *
 */
public class Point {
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public final double x;
	public final double y;

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

}
