package de.lambdamoo.hex4j.test;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import de.lambdamoo.hex4j.hexmath.HexUtil2;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout.Layout;
import de.lambdamoo.hex4j.hexmath.layout.HexLayout.Orientation;
import de.lambdamoo.hex4j.hexmath.layout.HexMap;
import de.lambdamoo.hex4j.hexmath.obj.Cube;
import de.lambdamoo.hex4j.hexmath.obj.FractionalHex;
import de.lambdamoo.hex4j.hexmath.obj.Hex;
import de.lambdamoo.hex4j.hexmath.obj.Offset;
import de.lambdamoo.hex4j.hexmath.obj.Point;
import de.lambdamoo.hex4j.search.estimator.DistanceHexEstimator;
import de.lambdamoo.hex4j.search.finder.AStarFinder;
import de.lambdamoo.hex4j.search.producer.BlockedChecker;
import de.lambdamoo.hex4j.search.producer.OffsetProducer;
import de.lambdamoo.hex4j.tile.core.Tile;
import de.lambdamoo.hex4j.tile.core.TileMap;
import de.lambdamoo.hex4j.tile.core.TileMapLayer;
import de.lambdamoo.hex4j.tile.reader.ImageSourceConverter;
import de.lambdamoo.hex4j.tile.reader.TMXReader;
import de.lambdamoo.hex4j.tile.reader.TileMapImages;
import de.lambdamoo.hex4j.ui.CanvasPane;

public class GuiTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private long timeLastPulse = 0;

	public void pulse(long timeNanoNow, long timeNanoDelta) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					myScene.pulse();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private MyScene myScene = new MyScene();

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setWidth(800);
			primaryStage.setHeight(600);

			myScene.init(primaryStage);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					System.exit(0);
				}
			});

			new AnimationTimer() {
				@Override
				public void handle(long now) {
					long timeNanoNow = System.nanoTime();
					long timeNanoDelta = timeNanoNow - timeLastPulse;
					pulse(timeNanoNow, timeNanoDelta);
					timeLastPulse = timeNanoNow;
				}
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
		primaryStage.show();
	}
}

class MyScene {
	private CanvasPane canvasPane = new CanvasPane(new Canvas(1200, 960));

	private AStarFinder<Hex> offAStar = null;

	private HexMap<Tile> hexMap = new HexMap<Tile>();

	private Hex mouseHex = null;

	/**
	 * Don not confuse the hex coordinates with the offset coordinates which are
	 * painted on the map
	 */
	private Hex startHex = new Hex(5, 4);

	private Label statusLabel = new Label("");

	/**
	 * This method draws the path found by the pathfinding algorithm. To improve
	 * performance you can only calculate it, when the last mouse position has
	 * changed.
	 * 
	 * @param gc
	 * @param start
	 * @param end
	 * @throws Exception
	 */
	private void drawPath(GraphicsContext gc, Hex start, Hex end) throws Exception {
		// System.out.println("Start draw path " + start.toString() + " " +
		// end.toString());
		List<Hex> hexes = HexUtil2.hexPath(start, end, offAStar);
		if (hexes != null) {
			for (Hex hex : hexes) {
				Point linePoint = hexMap.getHexLayout().hex2Pixel(hex);
				List<Point> points = HexUtil2.hexCorners(hexMap.getHexLayout().getOrientation(), linePoint, hexMap.getHexLayout()
						.getEdgeSize());
				fillPolygon(gc, points, 0, 0);
			}
		}
		// System.out.println("End draw path");
	}

	public void pulse() throws Exception {
		renderMap(canvasPane.getCanvas().getGraphicsContext2D(), tilemap);
	}

	public void init(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setCenter(canvasPane);
		root.setBottom(statusLabel);
		Scene scene = new Scene(root);

		primaryStage.setScene(scene);
		canvasPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent eve) {
				Hex tempHex = HexUtil2.hexRound(hexMap.getHexLayout().pixel2Hex(new Point(eve.getX(), eve.getY())));
				mouseHex = tempHex;
				statusLabel.setText(showHexStatusOnMouseMove(eve));
			}

		});
		canvasPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent eve) {
				String text = showHexStatusOnClick(eve);
				statusLabel.setText(text);
			}
		});

		initHexMap();

	}

	/**
	 * This helper method shows how to fill a polygon.
	 * 
	 * @param gc
	 * @param p
	 * @param offsetX
	 * @param offsetY
	 */
	private void fillPolygon(GraphicsContext gc, List<Point> p, double offsetX, double offsetY) {
		double[] x = new double[6];
		double[] y = new double[6];

		for (int i = 0; i < 6; i++) {
			x[i] = p.get(i).x + offsetX;
			y[i] = p.get(i).y + offsetY;
		}
		gc.fillPolygon(x, y, 6);
	}

	/**
	 * In this method the tile map is loaded from the TMX-file. Additionally the
	 * referenced images can be loaded and added to the class TileMapImages
	 * (actually commented out).
	 * 
	 * @throws Exception
	 */
	private void initHexMap() throws Exception {
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

		// initialize the search algorithm
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
		offsetProd.setSize(new Offset(0, 0), new Offset(tilemap.getWidth(), tilemap.getHeight()));
		offAStar = new AStarFinder<Hex>(offsetProd, new DistanceHexEstimator());

		// initialize blocked tiles
		blockedTiles.add(new Point(5, 5));
		blockedTiles.add(new Point(4, 5));
		blockedTiles.add(new Point(3, 5));
		blockedTiles.add(new Point(6, 6));
		blockedTiles.add(new Point(2, 2));
		blockedTiles.add(new Point(6, 3));
	}

	private TileMap tilemap;

	/**
	 * Helper list for storing blocked tiles. Normally you should have this
	 * information in the tile itself.
	 */
	private List<Point> blockedTiles = new ArrayList<Point>();

	/**
	 * This method is invoked every pulse. It should finish very fast to avoid
	 * unwanted behavior. Every hexagon of the tile map is painted. In real
	 * life, you should paint only the current viewport and maybe paint tile
	 * from images.
	 * 
	 * @param gc
	 * @param map
	 * @throws Exception
	 */
	private void renderMap(GraphicsContext gc, TileMap map) throws Exception {
		gc.clearRect(0, 0, canvasPane.getCanvas().getWidth(), canvasPane.getCanvas().getHeight());
		TileMapLayer layer = tilemap.getMapLayers().get("background");

		// paint mouse
		if (mouseHex != null) {
			Point pEnd = hexMap.getHexLayout().hex2Pixel(mouseHex);
			List<Point> pointsEnd = HexUtil2.hexCorners(hexMap.getHexLayout().getOrientation(), pEnd, hexMap.getHexLayout().getEdgeSize());
			gc.setFill(Color.RED);
			fillPolygon(gc, pointsEnd, 0, 0);
			// drawPath
			gc.setFill(Color.GREEN);
			drawPath(gc, startHex, mouseHex);
		}

		// paint start
		Point pStart = hexMap.getHexLayout().hex2Pixel(startHex);
		List<Point> pointsStart = HexUtil2.hexCorners(hexMap.getHexLayout().getOrientation(), pStart, hexMap.getHexLayout().getEdgeSize());
		gc.setFill(Color.YELLOW);
		fillPolygon(gc, pointsStart, 0, 0);

		gc.setFill(Color.BLACK);
		Point center = new Point(hexMap.getHexLayout().getHexWidth() / 2, hexMap.getHexLayout().getHexHeight() / 2);
		for (int x = 0; x < tilemap.getWidth(); x++) {
			for (int y = 0; y < tilemap.getHeight(); y++) {
				Tile tile = layer.getTile(x, y);
				if (tile.getBlocked() == 0) {
					List<Point> p = HexUtil2
							.hexCorners(hexMap.getHexLayout().getOrientation(), center, hexMap.getHexLayout().getEdgeSize());
					if (x % 2 == 0) {
						renderPolygon(gc, p, x * hexMap.getHexLayout().getHexHorizontalDistance(), y
								* hexMap.getHexLayout().getHexVerticalDistance());
					} else {
						renderPolygon(gc, p, x * hexMap.getHexLayout().getHexHorizontalDistance(), y
								* hexMap.getHexLayout().getHexVerticalDistance() + hexMap.getHexLayout().getHexVerticalDistance() / 2);
					}
				} else {
					// paint blocked
					gc.setFill(Color.BLACK);
					Point p = hexMap.getHexLayout().hex2Pixel(
							HexUtil2.offset2Hex(new Offset(tile.x, tile.y), hexMap.getHexLayout().getLayout(), hexMap.getHexLayout()
									.getOrientation()));
					List<Point> ps = HexUtil2.hexCorners(hexMap.getHexLayout().getOrientation(), p, hexMap.getHexLayout().getEdgeSize());
					fillPolygon(gc, ps, 0, 0);
				}
			}
		}

		// fill the blocked tiles black
		gc.setFill(Color.BLACK);
		for (Point tile : blockedTiles) {
			Point p = hexMap.getHexLayout().hex2Pixel(
					HexUtil2.offset2Hex(new Offset((int) tile.x, (int) tile.y), hexMap.getHexLayout().getLayout(), hexMap.getHexLayout()
							.getOrientation()));
			List<Point> points = HexUtil2.hexCorners(hexMap.getHexLayout().getOrientation(), p, hexMap.getHexLayout().getEdgeSize());
			fillPolygon(gc, points, 0, 0);
		}
	}

	/**
	 * This method paints a polygon
	 * 
	 * @param gc
	 * @param p
	 * @param offsetX
	 * @param offsetY
	 */
	private void renderPolygon(GraphicsContext gc, List<Point> p, double offsetX, double offsetY) {
		double[] x = new double[6];
		double[] y = new double[6];

		for (int i = 0; i < 6; i++) {
			x[i] = p.get(i).x + offsetX;
			y[i] = p.get(i).y + offsetY;
		}
		gc.setFill(null);
		gc.setStroke(Color.BLACK);
		gc.strokePolygon(x, y, 6);

		gc.setStroke(Color.RED);
		double offX = offsetX + hexMap.getHexLayout().getHexWidth() / 2;
		double offY = offsetY + hexMap.getHexLayout().getHexHeight() / 2;
		// center dot of each hex
		// gc.strokeRect(offX, offY, 1, 1);

		FractionalHex h4 = hexMap.getHexLayout().pixel2Hex(new Point(offX, offY));
		Hex hex4 = HexUtil2.hexRound(h4);
		Cube c = HexUtil2.hex2Cube(hex4);
		Offset coo = HexUtil2.cube2Offset(c, hexMap.getHexLayout().getLayout(), hexMap.getHexLayout().getOrientation());
		gc.fillText(coo.col + "/" + coo.row, offX - 10, offY - 10);
	}

	/**
	 * This method puts few status informations in the string to show the hex
	 * math.
	 * 
	 * @param eve
	 * @return a string
	 */
	private String showHexStatusOnClick(MouseEvent eve) {
		String text = eve.getX() + "/" + eve.getY();
		FractionalHex endHexFrac = hexMap.getHexLayout().pixel2Hex(new Point(eve.getX(), eve.getY()));
		Hex hex = HexUtil2.hexRound(endHexFrac);
		text += "   " + hex.toString();
		Point poi = hexMap.getHexLayout().hex2Pixel(hex);

		List<Point> list = HexUtil2.hexCorners(hexMap.getHexLayout().getOrientation(), poi, hexMap.getHexLayout().getEdgeSize());
		Cube cube = HexUtil2.hex2Cube(hex);
		Offset coo = HexUtil2.cube2Offset(cube, hexMap.getHexLayout().getLayout(), hexMap.getHexLayout().getOrientation());
		double offsetY = 0;
		fillPolygon(canvasPane.getCanvas().getGraphicsContext2D(), list, 0, offsetY);
		text += "   " + poi.toString();
		text += "   " + coo.toString();
		return text;
	}

	/**
	 * This method puts few status informations in the string to show the hex
	 * math.
	 * 
	 * @param eve
	 * @return a string
	 */
	private String showHexStatusOnMouseMove(MouseEvent eve) {
		String text = eve.getX() + "/" + eve.getY();
		FractionalHex endHexFrac = hexMap.getHexLayout().pixel2Hex(new Point(eve.getX(), eve.getY()));
		Hex hex = HexUtil2.hexRound(endHexFrac);
		text += "   " + hex.toString();
		Cube cube = HexUtil2.hex2Cube(hex);
		Offset coo = HexUtil2.cube2Offset(cube, hexMap.getHexLayout().getLayout(), hexMap.getHexLayout().getOrientation());
		text += "   " + coo.toString();
		Point poi = hexMap.getHexLayout().hex2Pixel(hex);
		text += "  " + poi.toString();
		return text;
	}
}
