package de.lambdamoo.hex4j.hexmath.layout;

import java.io.Serializable;
import java.util.ArrayList;

import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.obj.Cube;
import de.lambdamoo.hex4j.hexmath.obj.FractionalHex;
import de.lambdamoo.hex4j.hexmath.obj.Hex;
import de.lambdamoo.hex4j.hexmath.obj.Offset;
import de.lambdamoo.hex4j.hexmath.obj.Point;

/**
 * This is a storage class for the attributes of the layout. Orientation (FLAT
 * or POINTY), Layout (EVEN or ODD column layout), etc.
 * 
 * Additionally there are few calculation methods to convert pixel to hex and
 * vice versa.
 * 
 */
public class HexLayout implements Serializable {
	public enum Direction {
		A_ONE, B_TWO, C_THREE, D_FOUR, E_FIVE, F_SIX
	}

	public enum Layout {
		EVEN, ODD
	}

	public enum Orientation {
		FLAT_TOPPED, POINTY_TOPPED
	}

	public static OrientationCorners flat = new OrientationCorners(3.0 / 2.0, 0.0, Math.sqrt(3.0) / 2.0, Math.sqrt(3.0), 2.0 / 3.0, 0.0,
			-1.0 / 3.0, Math.sqrt(3.0) / 3.0, 0.0);

	public static OrientationCorners pointy = new OrientationCorners(Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0,
			Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);

	private double edgeSize = 0;

	private double hexHeight = 0;

	private double hexHorizontalDistance = 0;

	private double hexVerticalDistance = 0;

	private double hexWidth = 0;

	private Layout layout = Layout.EVEN;

	public Orientation orientation;

	public OrientationCorners orientCorners;

	public Point origin;

	public HexLayout() {
		super();
	}

	public HexLayout(Orientation orient, Point origin) {
		this.orientation = orient;
		if (orient.equals(Orientation.FLAT_TOPPED)) {
			this.orientCorners = flat;
		} else {
			this.orientCorners = pointy;
		}
		this.origin = origin;
	}

	public double getEdgeSize() {
		return edgeSize;
	}

	public double getHexHeight() {
		return hexHeight;
	}

	public double getHexHorizontalDistance() {
		return hexHorizontalDistance;
	}

	public double getHexVerticalDistance() {
		return hexVerticalDistance;
	}

	public double getHexWidth() {
		return hexWidth;
	}

	public Layout getLayout() {
		return layout;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public Point getOrigin() {
		return origin;
	}

	public Point hex2Pixel(Hex h) {
		double x, y;
		Point origin = this.origin;
		if (this.getOrientation().equals(Orientation.FLAT_TOPPED)) {
			x = this.getEdgeSize() * 3 / 2 * h.q;
			y = this.getEdgeSize() * Math.sqrt(3) * (h.r + h.q / 2);
			if (h.q < 0 && h.q % 2 == -1) {
				y -= this.getHexVerticalDistance() / 2;
			} else if (h.q > 0 && h.q % 2 == 1) {
				y += this.getHexVerticalDistance() / 2;
			} else if (h.q % 2 == 0) {
				// y -= this.getHexVerticalDistance() / 2;
			}
		} else {
			x = this.getEdgeSize() * Math.sqrt(3) * (h.q + h.r / 2);
			y = this.getEdgeSize() * 3 / 2 * h.r;
		}
		return new Point(x + origin.x, y + origin.y);
	}

	public Point hexCornerOffset(int corner) {
		OrientationCorners M = orientCorners;
		double angle = 2.0 * Math.PI * (corner + M.start_angle) / 6;
		return new Point(getHexWidth() * Math.cos(angle), getHexHeight() * Math.sin(angle));
	}

	public Point offset2Pixel(Offset off) {
		Cube cube = HexUtil2.offset2Cube(off, this.layout, this.orientation);
		Hex hex = HexUtil2.cube2Hex(cube);
		return hex2Pixel(hex);
	}

	public FractionalHex pixel2Hex(Point offsetP) {
		Point pt = new Point((offsetP.x - origin.x), (offsetP.y - origin.y));
		double q, r;
		if (this.orientation.equals(Orientation.FLAT_TOPPED)) {
			q = pt.x * 2 / 3 / this.edgeSize;
			r = (-pt.x / 3 + Math.sqrt(3) / 3 * pt.y) / this.edgeSize;
		} else {
			q = (pt.x * Math.sqrt(3) / 3 - pt.y / 3) / this.edgeSize;
			r = pt.y * 2 / 3 / this.edgeSize;

		}

		return new FractionalHex(q, r, -q - r);
	}

	public ArrayList<Point> polygonCorners(Hex h) {
		ArrayList<Point> corners = new ArrayList<Point>(6);
		Point center = hex2Pixel(h);
		for (int i = 0; i < 6; i++) {
			Point offset = hexCornerOffset(i);
			corners.add(new Point(center.x + offset.x, center.y + offset.y));
		}
		return corners;
	}

	public void setEdgeSize(double size) {
		this.edgeSize = size;
	}

	public void setHexHeight(double hexHeight) {
		this.hexHeight = hexHeight;
	}

	public void setHexHorizontalDistance(double hexHorizontalDistance) {
		this.hexHorizontalDistance = hexHorizontalDistance;
	}

	public void setHexVerticalDistance(double hexVerticalDistance) {
		this.hexVerticalDistance = hexVerticalDistance;
	}

	public void setHexWidth(double hexWidth) {
		this.hexWidth = hexWidth;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}
}
