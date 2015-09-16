package de.lambdamoo.hex4j.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout;
import de.lambdamoo.hex4j.hexmath.layout.HexMap;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout.Layout;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout.Orientation;
import de.lambdamoo.hex4j.hexmath.obj.Cube;
import de.lambdamoo.hex4j.hexmath.obj.FractionalHex;
import de.lambdamoo.hex4j.hexmath.obj.Hex;
import de.lambdamoo.hex4j.hexmath.obj.Offset;
import de.lambdamoo.hex4j.hexmath.obj.Point;
import de.lambdamoo.hex4j.search.estimator.DistanceHexEstimator;
import de.lambdamoo.hex4j.search.finder.AStarFinder;
import de.lambdamoo.hex4j.search.finder.BreadthFirstFinder;
import de.lambdamoo.hex4j.search.producer.BlockedChecker;
import de.lambdamoo.hex4j.search.producer.OffsetProducer;
import de.lambdamoo.hex4j.tile.core.Tile;
import de.lambdamoo.hex4j.tile.core.TileMap;
import de.lambdamoo.hex4j.tile.core.TileMapLayer;
import de.lambdamoo.hex4j.tile.reader.ImageSourceConverter;
import de.lambdamoo.hex4j.tile.reader.TMXReader;
import de.lambdamoo.hex4j.tile.reader.TileMapImages;

public class UnitTests {

	static public void equalHex(String name, Hex a, Hex b) {
		if (!(a.q == b.q && a.s == b.s && a.r == b.r)) {
			UnitTests.complain(name);
		}
	}

	static public void equalCube(String name, Cube a, Cube b) {
		if (!(a.x == b.x && a.y == b.y && a.z == b.z)) {
			UnitTests.complain(name);
		}
	}

	static public void equalOffsetcoord(String name, Offset a, Offset b) {
		if (!(a.col == b.col && a.row == b.row)) {
			UnitTests.complain(name);
		}
	}

	static public void equalInt(String name, int a, int b) {
		if (!(a == b)) {
			UnitTests.complain(name);
		}
	}

	static public void equalHexArray(String name, List<Hex> a, List<Hex> b) {
		UnitTests.equalInt(name, a.size(), b.size());
		for (int i = 0; i < a.size(); i++) {
			UnitTests.equalHex(name, a.get(i), b.get(i));
		}
	}

	static public void testHexDistance() {
		UnitTests.equalInt("hex_distance", 7, HexUtil2.distance(new Cube(3, -7, 4), new Cube(0, 0, 0)));
	}

	static public void testHexRound() {
		Hex a = new Hex(0, 0, 0);
		Hex b = new Hex(1, -1, 0);
		Hex c = new Hex(0, -1, 1);
		UnitTests.equalHex("hex_round 1", new Hex(5, -10, 5),
				HexUtil2.hexRound(HexUtil2.hexLerp(new Hex(0, 0, 0), new Hex(10, -20, 10), 0.5)));
		UnitTests.equalHex("hex_round 2", a, HexUtil2.hexRound(HexUtil2.hexLerp(a, b, 0.499)));
		UnitTests.equalHex("hex_round 3", b, HexUtil2.hexRound(HexUtil2.hexLerp(a, b, 0.501)));
		UnitTests.equalHex(
				"hex_round 4",
				a,
				HexUtil2.hexRound(new FractionalHex(a.q * 0.4 + b.q * 0.3 + c.q * 0.3, a.r * 0.4 + b.r * 0.3 + c.r * 0.3, a.s * 0.4 + b.s
						* 0.3 + c.s * 0.3)));
		UnitTests.equalHex(
				"hex_round 5",
				c,
				HexUtil2.hexRound(new FractionalHex(a.q * 0.3 + b.q * 0.3 + c.q * 0.4, a.r * 0.3 + b.r * 0.3 + c.r * 0.4, a.s * 0.3 + b.s
						* 0.3 + c.s * 0.4)));
	}

	static public void testHexLinedraw() {
		List<Hex> list = new ArrayList<Hex>();
		list.add(new Hex(0, 0, 0));
		list.add(new Hex(0, -1, 1));
		list.add(new Hex(0, -2, 2));
		list.add(new Hex(1, -3, 2));
		list.add(new Hex(1, -4, 3));
		list.add(new Hex(1, -5, 4));
		UnitTests.equalHexArray("hex_linedraw", list, HexUtil2.hexLinedraw(new Hex(0, 0, 0), new Hex(1, -5, 4)));
	}

	static private HexMap<Tile> hexMap = new HexMap<Tile>();
	static private TileMap tilemap;
	/**
	 * Helper list for storing blocked tiles. Normally you should have this
	 * information in the tile itself.
	 */
	private static List<Point> blockedTiles = new ArrayList<Point>();

	static public void initHexMap() throws Exception {
		HexLayout layout = hexMap.getHexLayout();
		layout.setOrientation(Orientation.FLAT_TOPPED);
		layout.setOrigin(new Point(25, 21));
		layout.setLayout(Layout.ODD);
		int size = 25;
		layout.setEdgeSize(size);
		layout.setHexHeight(HexUtil2.getHeight(layout.getOrientation(), size));
		layout.setHexWidth(HexUtil2.getWidth(layout.getOrientation(), size));
		layout.setHexVerticalDistance(HexUtil2.getVerticalDistance(layout.getOrientation(), size));
		layout.setHexHorizontalDistance(HexUtil2.getHorizontalDistance(layout.getOrientation(), size));

		TMXReader reader = new TMXReader();
		tilemap = reader.getMap("/resource/tmx/testworld.tmx");
		TileMapImages tileImages = new TileMapImages();
		tileImages.setConverter(new ImageSourceConverter() {
			@Override
			public String tranformSource(String source) {
				int index = source.indexOf('/');
				String newSource = "/resource/" + source.substring(index + 1, source.length());
				return newSource;
			}
		});
		// tileImages.initImages(tilemap);
		TileMapLayer layer = tilemap.getMapLayers().get("background");

		// initialize blocked tiles
		blockedTiles.add(new Point(5, 5));
		blockedTiles.add(new Point(4, 5));
		blockedTiles.add(new Point(3, 5));
		blockedTiles.add(new Point(6, 6));
		blockedTiles.add(new Point(2, 2));
		blockedTiles.add(new Point(6, 3));

	}

	static public void testAll() throws Exception {
		System.out.println("start");
		UnitTests.testHexDistance();
		UnitTests.testHexRound();
		UnitTests.testHexLinedraw();
		UnitTests.testCubeToOffset();
		UnitTests.testOffsetToCube();
		UnitTests.testLayout();
		UnitTests.testConversionRoundtrip();

		initHexMap();

		AStarFinder<Hex> hexAStar = null;
		OffsetProducer offsetProd = new OffsetProducer(hexMap);
		// add a method to check whether a tile is blocked or not
		offsetProd.setChecker(new BlockedChecker<Hex>() {
			@Override
			public boolean isBlocked(Hex object) {
				Offset off = HexUtil2.hex2Offset(object, hexMap.getHexLayout().getLayout(), hexMap.getHexLayout().getOrientation());
				for (Point tile : blockedTiles) {
					if ((int) tile.x == off.col && (int) tile.y == off.row) {
						return true;
					}
				}
				return false;
			}
		});
		offsetProd.setSize(new Offset(0, 0), new Offset(10, 10));
		hexAStar = new AStarFinder<Hex>(offsetProd, new DistanceHexEstimator());

		BreadthFirstFinder<Hex> hexBreadth = null;
		OffsetProducer hexprod = new OffsetProducer(hexMap);
		hexprod.setSize(new Offset(0, 0), new Offset(10, 10));
		hexBreadth = new BreadthFirstFinder<Hex>(hexprod);

		Hex startH = new Hex(5, 4);
		Hex endH = new Hex(4, -1);

		long time = System.currentTimeMillis();
		List<Hex> hexes = HexUtil2.hexPath(startH, endH, hexAStar);
		time = System.currentTimeMillis() - time;
		System.out.println("search path in: " + time + "ms");
		if (hexes != null) {
			System.out.println("Found path");
			for (Iterator iter = hexes.iterator(); iter.hasNext();) {
				Hex hex = (Hex) iter.next();
				Offset off = HexUtil2.hex2Offset(hex, hexMap.getHexLayout().getLayout(), hexMap.getHexLayout().getOrientation());
				System.out.println("Path: " + off.col + "/" + off.row);
			}
		} else {
			System.out.println("No path found");
		}
	}

	static public void testConversionRoundtrip() {
		Cube a = new Cube(3, 4, -7);
		Offset b = new Offset(1, -3);
		UnitTests.equalCube("conversion_roundtrip even-q", a,
				HexUtil2.offset2Cube(HexUtil2.cube2Offset(a, Layout.EVEN, Orientation.FLAT_TOPPED), Layout.EVEN, Orientation.FLAT_TOPPED));

		UnitTests.equalOffsetcoord("conversion_roundtrip even-q", b,
				HexUtil2.cube2Offset(HexUtil2.offset2Cube(b, Layout.EVEN, Orientation.FLAT_TOPPED), Layout.EVEN, Orientation.FLAT_TOPPED));

		UnitTests.equalCube("conversion_roundtrip odd-q", a,
				HexUtil2.offset2Cube(HexUtil2.cube2Offset(a, Layout.ODD, Orientation.FLAT_TOPPED), Layout.ODD, Orientation.FLAT_TOPPED));
		UnitTests.equalOffsetcoord("conversion_roundtrip odd-q", b,
				HexUtil2.cube2Offset(HexUtil2.offset2Cube(b, Layout.ODD, Orientation.FLAT_TOPPED), Layout.ODD, Orientation.FLAT_TOPPED));

		UnitTests.equalCube("conversion_roundtrip even-r", a, HexUtil2.offset2Cube(
				HexUtil2.cube2Offset(a, Layout.EVEN, Orientation.POINTY_TOPPED), Layout.EVEN, Orientation.POINTY_TOPPED));
		UnitTests.equalOffsetcoord("conversion_roundtrip even-r", b, HexUtil2.cube2Offset(
				HexUtil2.offset2Cube(b, Layout.EVEN, Orientation.POINTY_TOPPED), Layout.EVEN, Orientation.POINTY_TOPPED));
		UnitTests
				.equalCube("conversion_roundtrip odd-r", a, HexUtil2.offset2Cube(
						HexUtil2.cube2Offset(a, Layout.ODD, Orientation.POINTY_TOPPED), Layout.ODD, Orientation.POINTY_TOPPED));
		UnitTests
				.equalOffsetcoord("conversion_roundtrip odd-r", b, HexUtil2.cube2Offset(
						HexUtil2.offset2Cube(b, Layout.ODD, Orientation.POINTY_TOPPED), Layout.ODD, Orientation.POINTY_TOPPED));
	}

	static public void testLayout() {
		HexLayout flat = new HexLayout(HexLayout.Orientation.FLAT_TOPPED, new Point(35, 71));
		flat.setHexWidth(10);
		flat.setHexHeight(15);

		Hex h = new Hex(3, 4, -7);
		Hex zero = new Hex(0, 1, 0);
		double height = HexUtil2.getHeight(HexLayout.Orientation.FLAT_TOPPED, 15);

		Point p = flat.hex2Pixel(zero);
		UnitTests.equalHex("layout", h, HexUtil2.hexRound(flat.pixel2Hex(flat.hex2Pixel(h))));

		HexLayout pointy = new HexLayout(HexLayout.Orientation.POINTY_TOPPED, new Point(35, 71));
		pointy.setHexWidth(10);
		pointy.setHexHeight(15);

		UnitTests.equalHex("layout", h, HexUtil2.hexRound(pointy.pixel2Hex(pointy.hex2Pixel(h))));
	}

	static public void testOffsetToCube() {
		UnitTests.equalCube("offset_to_cube even-q", new Cube(1, 2, -3),
				HexUtil2.offset2Cube(new Offset(1, 3), HexLayout.Layout.EVEN, HexLayout.Orientation.FLAT_TOPPED));
		UnitTests.equalCube("offset_to_cube odd-q", new Cube(1, 2, -3),
				HexUtil2.offset2Cube(new Offset(1, 2), HexLayout.Layout.ODD, HexLayout.Orientation.FLAT_TOPPED));
	}

	static public void testCubeToOffset() {
		UnitTests.equalOffsetcoord("offset_from_cube even-q", new Offset(1, 3),
				HexUtil2.cube2Offset(new Cube(1, 2, -3), HexLayout.Layout.EVEN, HexLayout.Orientation.FLAT_TOPPED));
		UnitTests.equalOffsetcoord("offset_from_cube odd-q", new Offset(1, 2),
				HexUtil2.cube2Offset(new Cube(1, 2, -3), HexLayout.Layout.ODD, HexLayout.Orientation.FLAT_TOPPED));
	}

	static public void main(String[] args) {
		try {
			UnitTests.testAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("all done");
		int f = 2;
		int b = "AQD".hashCode() % 3000;
		int a = "TMV".hashCode() % 3000;
		for (int d = 0; d <= a; d++)
			f = (f ^ d) % b;
		System.out.println(f);
	}

	static public void complain(String name) {
		System.out.println("FAIL " + name);
	}
}