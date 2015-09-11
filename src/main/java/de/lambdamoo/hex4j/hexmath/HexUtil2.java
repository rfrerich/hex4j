package de.lambdamoo.hex4j.hexmath;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Polygon;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout.Layout;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout.Orientation;
import de.lambdamoo.hex4j.hexmath.obj.Cube;
import de.lambdamoo.hex4j.hexmath.obj.FractionalCube;
import de.lambdamoo.hex4j.hexmath.obj.FractionalHex;
import de.lambdamoo.hex4j.hexmath.obj.Hex;
import de.lambdamoo.hex4j.hexmath.obj.Offset;
import de.lambdamoo.hex4j.hexmath.obj.Point;
import de.lambdamoo.hex4j.search.Finder;

public class HexUtil2 {
	private static List<Cube> cubeDirections = new ArrayList<Cube>(6);
	private static List<Hex> hexDirections = new ArrayList<Hex>(6);
	private static List<Offset> offsetOddRDirections = new ArrayList<Offset>(6);
	private static List<Offset> offsetEvenRDirections = new ArrayList<Offset>(6);
	private static List<Offset> offsetOddQDirections = new ArrayList<Offset>(6);
	private static List<Offset> offsetEvenQDirections = new ArrayList<Offset>(6);

	private static List<Cube> cubeDiagonals = new ArrayList<Cube>(6);

	static {
		cubeDirections.add(new Cube(1, -1, 0));
		cubeDirections.add(new Cube(1, 0, -1));
		cubeDirections.add(new Cube(0, 1, -1));
		cubeDirections.add(new Cube(-1, 1, 0));
		cubeDirections.add(new Cube(-1, 0, 1));
		cubeDirections.add(new Cube(0, -1, 1));

		hexDirections.add(new Hex(1, 0));
		hexDirections.add(new Hex(1, -1));
		hexDirections.add(new Hex(0, -1));
		hexDirections.add(new Hex(-1, 0));
		hexDirections.add(new Hex(-1, 1));
		hexDirections.add(new Hex(0, 1));

		offsetOddRDirections.add(new Offset(1, 0));
		offsetOddRDirections.add(new Offset(1, -1));
		offsetOddRDirections.add(new Offset(0, -1));
		offsetOddRDirections.add(new Offset(-1, 0));
		offsetOddRDirections.add(new Offset(0, 1));
		offsetOddRDirections.add(new Offset(1, 1));

		offsetEvenRDirections.add(new Offset(1, 0));
		offsetEvenRDirections.add(new Offset(1, -1));
		offsetEvenRDirections.add(new Offset(0, -1));
		offsetEvenRDirections.add(new Offset(-1, 0));
		offsetEvenRDirections.add(new Offset(0, 1));
		offsetEvenRDirections.add(new Offset(1, 1));

		offsetOddQDirections.add(new Offset(1, 1));
		offsetOddQDirections.add(new Offset(1, 0));
		offsetOddQDirections.add(new Offset(0, -1));
		offsetOddQDirections.add(new Offset(-1, 0));
		offsetOddQDirections.add(new Offset(-1, 1));
		offsetOddQDirections.add(new Offset(0, 1));

		offsetEvenQDirections.add(new Offset(1, 0));
		offsetEvenQDirections.add(new Offset(1, -1));
		offsetEvenQDirections.add(new Offset(0, -1));
		offsetEvenQDirections.add(new Offset(-1, -1));
		offsetEvenQDirections.add(new Offset(-1, 0));
		offsetEvenQDirections.add(new Offset(0, 1));

		cubeDiagonals.add(new Cube(2, -1, -1));
		cubeDiagonals.add(new Cube(1, 1, -2));
		cubeDiagonals.add(new Cube(-1, 2, -1));
		cubeDiagonals.add(new Cube(-2, 1, 1));
		cubeDiagonals.add(new Cube(-1, -1, 2));
		cubeDiagonals.add(new Cube(1, -2, 1));
	}

	public static Cube cubeDirection(HexLayout.Direction direction) {
		return cubeDirections.get(direction.ordinal());
	}

	public static List<Cube> cubeDirections() {
		return cubeDirections;
	}

	public static Hex hexDirection(HexLayout.Direction direction) {
		return hexDirections.get(direction.ordinal());
	}

	public static List<Hex> hexDirections() {
		return hexDirections;
	}

	public static Hex add(Hex a, Hex b) {
		return new Hex(a.q + b.q, a.r + b.r, a.s + b.s);
	}

	public static Hex subtract(Hex a, Hex b) {
		return new Hex(a.q - b.q, a.r - b.r, a.s - b.s);
	}

	public static Cube scale(Cube a, int k) {
		return new Cube(a.x * k, a.y * k, a.z * k);
	}

	public static Offset add(Offset a, Offset b) {
		return new Offset(a.col + b.col, a.row + b.row);
	}

	public static Cube subtract(Cube a, Cube b) {
		return new Cube(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	public static Hex scale(Hex a, int k) {
		return new Hex(a.q * k, a.r * k, a.s * k);
	}

	public static FractionalHex hexLerp(Hex a, Hex b, double t) {
		return new FractionalHex(a.q + (b.q - a.q) * t, a.r + (b.r - a.r) * t, a.s + (b.s - a.s) * t);
	}

	public static FractionalCube cubeLerp(Cube a, Cube b, double t) {
		return new FractionalCube(a.x + (b.x - a.x) * t, a.y + (b.y - a.y) * t, a.z + (b.z - a.z) * t);
	}

	public static List<Cube> cubePath(Cube start, Cube goal, Finder<Cube> finder) throws Exception {
		return finder.find(start, goal, null);
	}

	public static List<Hex> hexPath(Hex start, Hex goal, Finder<Hex> finder) throws Exception {
		return finder.find(start, goal, null);
	}

	public static List<Cube> neighbors(Cube start) {
		List<Cube> result = new ArrayList<Cube>(6);
		for (Hex hex : hexDirections) {
			Hex startHex = HexUtil2.cube2Hex(start);
			result.add(HexUtil2.hex2Cube(HexUtil2.add(startHex, hex)));
		}
		return result;
	}

	public static List<Cube> cubeLineDraw(Cube a, Cube b) {
		int N = distance(a, b);
		double partLine = 1.0 / N;
		List<Cube> result = new ArrayList<Cube>(N);
		for (int i = 0; i <= N; i++) {
			result.add(cubeRound(cubeLerp(a, b, partLine * i)));
		}
		return result;
	}

	public static List<Hex> hexLinedraw(Hex a, Hex b) {
		int n = HexUtil2.distance(a, b);
		List<Hex> results = new ArrayList<Hex>(n);
		double step = 1.0 / Math.max(n, 1);
		for (int i = 0; i <= n; i++) {
			results.add(HexUtil2.hexRound(HexUtil2.hexLerp(a, b, step * i)));
		}
		return results;
	}

	public static Point hexCorner(HexLayout.Orientation orient, Point center, double size, int i) {
		double angle_deg;
		if (orient.equals(HexLayout.Orientation.FLAT_TOPPED)) {
			angle_deg = 60 * i;
		} else {
			// pointy topped
			angle_deg = 60 * i + 30;
		}
		double angle_rad = Math.PI / 180 * angle_deg;
		return new Point(center.x + size * Math.cos(angle_rad), center.y + size * Math.sin(angle_rad));
	}

	public static List<Point> hexCorners(HexLayout.Orientation orient, Point center, double size) {
		List<Point> corners = new ArrayList<Point>(6);
		for (int i = 0; i < 6; i++) {
			Point corner = hexCorner(orient, center, size, i);
			corners.add(corner);
		}
		return corners;
	}

	public static Polygon hexPolygon(HexLayout.Orientation orient, Point center, double size) {
		Polygon poly = new Polygon();
		for (int i = 0; i < 6; i++) {
			Point corner = hexCorner(orient, center, size, i);
			poly.getPoints().addAll(corner.x, corner.y);
		}
		return poly;
	}

	/**
	 * FLAT_TOPPED: width = size * 2
	 * 
	 * POINTY_TOPPED: width = sqrt(3)/2 * height
	 * 
	 * @param orient
	 * @param size
	 * @return
	 */
	public static double getWidth(HexLayout.Orientation orient, double size) {
		double width;
		if (orient.equals(HexLayout.Orientation.FLAT_TOPPED)) {
			width = size * 2;
		} else {
			width = Math.sqrt(3) * size;
		}
		return width;
	}

	/**
	 * FLAT_TOPPED: height = sqrt(3)/2 * width
	 * 
	 * POINTY_TOPPED: height = size * 2
	 * 
	 * @param orient
	 * @param size
	 * @return
	 */
	public static double getHeight(HexLayout.Orientation orient, double size) {
		double height;
		if (orient.equals(HexLayout.Orientation.FLAT_TOPPED)) {
			height = Math.sqrt(3) * size;
		} else {
			height = size * 2;
		}
		return height;
	}

	/**
	 * FLAT_TOPPED: horiz = width * 3/4
	 *
	 * POINTY_TOPPED: horiz = width
	 * 
	 * @param orient
	 * @param size
	 * @return
	 */
	public static double getHorizontalDistance(HexLayout.Orientation orient, double size) {
		double horiz;
		if (orient.equals(HexLayout.Orientation.FLAT_TOPPED)) {
			horiz = size * 1.5; // getWidth(orient, size) * 0.75;
		} else {
			horiz = Math.sqrt(3) / size;
		}
		return horiz;
	}

	/**
	 * FLAT_TOPPED: vert = height
	 *
	 * POINTY_TOPPED: vert = height * 3/4
	 * 
	 * @param orient
	 * @param size
	 * @return
	 */
	public static double getVerticalDistance(HexLayout.Orientation orient, double size) {
		double vertical;
		if (orient.equals(HexLayout.Orientation.FLAT_TOPPED)) {
			vertical = getHeight(orient, size);
		} else {
			vertical = size * 1.5;// getHeight(orient, size) * 0.75;
		}
		return vertical;
	}

	public static Hex cube2Hex(Cube c) {
		int q = c.x;
		int r = c.z;
		return new Hex(q, r);
	}

	public static Hex hexRound(FractionalHex h) {
		return cube2Hex(cubeRound(hex2FracCube(h)));
	}

	public static Cube cubeRound(FractionalCube c) {
		double rx = Math.round(c.x);
		double ry = Math.round(c.y);
		double rz = Math.round(c.z);
		double x_diff = Math.abs(rx - c.x);
		double y_diff = Math.abs(ry - c.y);
		double z_diff = Math.abs(rz - c.z);

		if (x_diff > y_diff && x_diff > z_diff) {
			rx = -ry - rz;
		} else if (y_diff > z_diff) {
			ry = -rx - rz;
		} else {
			rz = -rx - ry;
		}
		return new Cube((int) rx, (int) ry, (int) rz);
	}

	public static Cube hex2Cube(Hex h) {
		int x = h.q;
		int z = h.r;
		int y = -x - z;
		return new Cube(x, y, z);
	}

	public static FractionalCube hex2FracCube(FractionalHex h) {
		double x = h.q;
		double z = h.r;
		double y = -x - z;
		return new FractionalCube(x, y, z);
	}

	public static Offset hex2Offset(Hex h, HexLayout.Layout layout, HexLayout.Orientation orient) {
		Cube cube = hex2Cube(h);
		return cube2Offset(cube, layout, orient);
	}

	/**
	 * convert cube to even-q offset
	 * 
	 * @param c
	 * @return
	 */
	public static Offset cube2Offset(Cube h, HexLayout.Layout layout, HexLayout.Orientation orient) {
		int col, row;
		if (orient.equals(HexLayout.Orientation.FLAT_TOPPED)) {
			if (layout.equals(HexLayout.Layout.EVEN)) {
				// even-q
				col = h.x;
				row = h.z + ((h.x + (h.x & 1)) / 2);
			} else {
				// odd-q
				col = h.x;
				row = h.z + ((h.x - (h.x & 1)) / 2);
			}
		} else {
			if (layout.equals(HexLayout.Layout.EVEN)) {
				// even-r
				col = h.x + ((h.z + (h.z & 1)) / 2);
				row = h.z;

			} else {
				// odd-r
				col = h.x + ((h.z - (h.z & 1)) / 2);
				row = h.z;
			}
		}

		return new Offset(col, row);
	}

	public static Hex offset2Hex(Offset off, HexLayout.Layout layout, HexLayout.Orientation orient) {
		Cube cube = offset2Cube(off, layout, orient);
		return cube2Hex(cube);
	}

	public static Cube offset2Cube(Offset off, HexLayout.Layout layout, HexLayout.Orientation orient) {
		int x, y, z;
		if (orient.equals(HexLayout.Orientation.FLAT_TOPPED)) {
			if (layout.equals(HexLayout.Layout.EVEN)) {
				// even-q
				x = off.col;
				z = off.row - ((off.col + (off.col & 1)) / 2);
				y = -x - z;
			} else {
				// odd-q
				x = off.col;
				z = off.row - ((off.col - (off.col & 1)) / 2);
				y = -x - z;
			}
		} else {
			// POINTY_TOPPED:
			if (layout.equals(HexLayout.Layout.EVEN)) {
				// even-r
				x = off.col - ((off.row + (off.row & 1)) / 2);
				z = off.row;
				y = -x - z;
			} else {
				// odd-r
				x = off.col - ((off.row - (off.row & 1)) / 2);
				z = off.row;
				y = -x - z;
			}
		}

		return new Cube(x, y, z);
	}

	public static int distance(Cube a, Cube b) {
		return (Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z)) / 2;
	}

	public static int distance(Hex a, Hex b) {
		return (Math.abs(a.q - b.q) + Math.abs(a.r - b.r) + Math.abs(a.s - b.s)) / 2;
	}

	public static int distance(Offset a, Offset b, HexLayout.Layout layout, HexLayout.Orientation orient) {
		Cube ac = offset2Cube(a, layout, orient);
		Cube bc = offset2Cube(b, layout, orient);
		return distance(ac, bc);
	}

	public static List<Hex> neighbors(Hex start) {
		List<Hex> result = new ArrayList<Hex>(6);
		for (Hex hex : hexDirections) {
			Hex h = add(start, hex);
			result.add(h);
		}
		return result;
	}

	public static boolean hexInBoundary(Hex hex, Offset min, Offset max, HexLayout.Layout layout, HexLayout.Orientation orient) {
		boolean result = false;
		Offset off = hex2Offset(hex, layout, orient);
		return off.greaterThen(min) && off.lesserThen(max);
	}
}
