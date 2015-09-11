/*
 * Copyright [2012] [Sergey Mukhin]
 *
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package de.lambdamoo.hex4j.tile.reader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import de.lambdamoo.hex4j.tile.core.Animation;
import de.lambdamoo.hex4j.tile.core.ObjectGroup;
import de.lambdamoo.hex4j.tile.core.Polygon;
import de.lambdamoo.hex4j.tile.core.Polyline;
import de.lambdamoo.hex4j.tile.core.Tile;
import de.lambdamoo.hex4j.tile.core.TileMap;
import de.lambdamoo.hex4j.tile.core.TileMapLayer;
import de.lambdamoo.hex4j.tile.core.TileMapObject;
import de.lambdamoo.hex4j.tile.core.TileSet;
import de.lambdamoo.hex4j.tile.core.Vertex;

public class TMXReader {
	private class AnimationHandler extends DefaultHandler {
		private Animation animation;
		private DefaultHandler previousHandler;
		private HashMap<String, String> properties;

		public AnimationHandler(DefaultHandler previousHandler, Animation animation) {
			this.previousHandler = previousHandler;
			this.animation = animation;
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("object"))
				currentHandler = previousHandler;
		}

		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("properties")) {
				properties = new HashMap<String, String>();
				currentHandler = new PropertiesHandler(this, properties);
			} else if (qName.equalsIgnoreCase("polygon")) {
				Polygon polygon = new Polygon(getPoints(getString(atts, "points")));
				animation.setCenters(polygon.getVertices());
			} else {
				throw new SAXException("unknown tag:" + qName);
			}
		}
	}

	private class MapHandler extends DefaultHandler {
		private DefaultHandler previousHandler;

		public MapHandler(DefaultHandler previousHandler, TileMap map) {
			this.previousHandler = previousHandler;
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("map"))
				currentHandler = previousHandler;
		}

		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {

			if (qName.equalsIgnoreCase("tileset")) {
				TileSet tileSet = new TileSet();
				tileSet.setName(getString(atts, "name"));
				tileSet.setTileWidth(getInteger(atts, "tilewidth"));
				tileSet.setTileHeight(getInteger(atts, "tileheight"));
				tileSet.setSpacing(getInteger(atts, "spacing"));
				tileSet.setMargin(getInteger(atts, "margin"));
				tileSet.setFirstGid(getInteger(atts, "firstgid"));
				tileSet.setTilecount(getInteger(atts, "tilecount"));
				map.add(tileSet);
				currentHandler = new TileSetHandler(this, tileSet, getInteger(atts, "firstgid"));

			} else if (qName.equalsIgnoreCase("objectgroup")) {
				ObjectGroup objectGroup = new ObjectGroup();
				map.add(getString(atts, "name"), objectGroup);
				currentHandler = new ObjectGroupHandler(this, objectGroup);

			} else if (qName.equalsIgnoreCase("layer")) {
				TileMapLayer mapLayer = new TileMapLayer();
				mapLayer.setWidth(getInteger(atts, "width"));
				mapLayer.setHeight(getInteger(atts, "height"));
				map.add(getString(atts, "name"), mapLayer);
				currentHandler = new MapLayerHandler(this, mapLayer);

			} else if (qName.equalsIgnoreCase("properties")) {
				HashMap<String, String> properties = new HashMap<String, String>();
				map.setProperties(properties);
				currentHandler = new PropertiesHandler(this, properties);
			} else {
				throw new SAXException("unknown tag:" + qName);
			}
		}
	}

	private class MapLayerDataHandler extends DefaultHandler {
		private Tile currentTile;
		private String data = "";
		private TileMapLayer mapLayer;
		private DefaultHandler previousHandler;
		private int x = 0;
		private int y = 0;

		public MapLayerDataHandler(DefaultHandler previousHandler, TileMapLayer mapLayer) {
			this.previousHandler = previousHandler;
			this.mapLayer = mapLayer;
			Tile[][] tiles = new Tile[mapLayer.getWidth()][mapLayer.getHeight()];
			for (int ix = 0; ix < mapLayer.getWidth(); ix++) {
				for (int jy = 0; jy < mapLayer.getHeight(); jy++) {
					tiles[ix][jy] = new Tile();
				}
			}
			mapLayer.setTiles(tiles);
			this.currentTile = mapLayer.getTile(x, y);
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("data")) {
				currentHandler = previousHandler;
			}
			if (qName.equalsIgnoreCase("tile")) {
				x++;
				if (x >= mapLayer.getWidth()) {
					x = 0;
					y++;
				}
			}
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("tile")) {
				this.currentTile = mapLayer.getTile(x, y);
				Integer gid = getInteger(atts, "gid");
				this.currentTile.setGid(gid);
				this.currentTile.setX(x);
				this.currentTile.setY(y);
			}
		}
	}

	private class MapLayerHandler extends DefaultHandler {
		private TileMapLayer mapLayer;
		private DefaultHandler previousHandler;

		public MapLayerHandler(DefaultHandler previousHandler, TileMapLayer mapLayer) {
			this.previousHandler = previousHandler;
			this.mapLayer = mapLayer;
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("layer"))
				currentHandler = previousHandler;
		}

		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("data")) {
				// getString(atts, "encoding"), getString(atts, "compression")
				currentHandler = new MapLayerDataHandler(this, mapLayer);
			} else {
				throw new SAXException("unknown tag:" + qName);
			}
		}
	}

	private class MapObjectHandler extends DefaultHandler {
		private TileMapObject mapObject;
		private DefaultHandler previousHandler;

		public MapObjectHandler(DefaultHandler previousHandler, TileMapObject mapObject) {
			this.previousHandler = previousHandler;
			this.mapObject = mapObject;
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("object"))
				currentHandler = previousHandler;
		}

		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("polygon")) {
				Polygon polygon = new Polygon(getPoints(getString(atts, "points")));
				mapObject.add(polygon);
			} else if (qName.equalsIgnoreCase("polyline")) {
				Polyline polyline = new Polyline(getPoints(getString(atts, "points")));
				mapObject.add(polyline);
			} else if (qName.equalsIgnoreCase("properties")) {
				HashMap<String, String> properties = new HashMap<String, String>();
				mapObject.setProperties(properties);
				currentHandler = new PropertiesHandler(this, properties);
			} else {
				throw new SAXException("unknown tag:" + qName);
			}
		}
	}

	private class ObjectGroupHandler extends DefaultHandler {
		private HashMap<String, Animation> animations = new HashMap<String, Animation>();
		private ObjectGroup objectGroup;
		private ArrayList<TileMapObject> objects = new ArrayList<TileMapObject>();
		private DefaultHandler previousHandler;

		public ObjectGroupHandler(DefaultHandler previousHandler, ObjectGroup objectGroup) {
			this.previousHandler = previousHandler;
			this.objectGroup = objectGroup;
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("objectgroup"))
				currentHandler = previousHandler;

			// build all animation sprite sets
			Iterator<Entry<String, Animation>> iA = animations.entrySet().iterator();
			while (iA.hasNext()) {
				Entry<String, Animation> e = iA.next();
				Animation animation = e.getValue();

				for (TileMapObject obj : objects) {
					animation.correlate(obj);
				}

			}

			objectGroup.add(animations);
			objectGroup.add(objects);
		}

		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
			if ((qName.equalsIgnoreCase("object")) && (getString(atts, "type").equalsIgnoreCase("animation"))) {
				Animation animation = new Animation(getInteger(atts, "x"), getInteger(atts, "y"));
				animations.put(getString(atts, "name"), animation);
				currentHandler = new AnimationHandler(this, animation);

			} else if (qName.equalsIgnoreCase("object")) {
				TileMapObject mapObject = new TileMapObject();
				mapObject.setName(getString(atts, "name"));
				mapObject.setType(getString(atts, "type"));
				mapObject.setX(getInteger(atts, "x"));
				mapObject.setY(getInteger(atts, "y"));
				mapObject.setHeight(getInteger(atts, "height"));
				mapObject.setWidth(getInteger(atts, "width"));
				objects.add(mapObject);
				currentHandler = new MapObjectHandler(this, mapObject);
			} else {
				throw new SAXException("unknown tag:" + qName);
			}

		}

	}

	private class PropertiesHandler extends DefaultHandler {
		private DefaultHandler previousHandler;
		private HashMap<String, String> properties;

		public PropertiesHandler(DefaultHandler previousHandler, HashMap<String, String> properties) {
			this.previousHandler = previousHandler;
			this.properties = properties;
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("properties"))
				currentHandler = previousHandler;
		}

		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("property")) {
				properties.put(getString(atts, "name"), getString(atts, "value"));
			} else {
				throw new SAXException("unknown tag:" + qName);
			}
		}
	}

	// SAX library don't have SAXParser.setHandler(DefaultHandler) method, thats
	// why we will use this workaround stub to parse hierarchy
	private class SAXStub extends DefaultHandler {
		public void characters(char[] ch, int start, int length) throws SAXException {
			currentHandler.characters(ch, start, length);
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			currentHandler.endElement(uri, name, qName);
		}

		public void startElement(String uri, String name, String qName, Attributes attributes) throws SAXException {
			currentHandler.startElement(uri, name, qName, attributes);
		}

	}

	private class TileSetHandler extends DefaultHandler {
		private int firstgid;
		private DefaultHandler previousHandler;
		private TileSet tileSet;

		public TileSetHandler(DefaultHandler previousHandler, TileSet tileSet, int firstgid) {
			this.previousHandler = previousHandler;
			this.tileSet = tileSet;
			this.firstgid = firstgid;
		}

		public void endElement(String uri, String name, String qName) throws SAXException {
			if (qName.equalsIgnoreCase("tileset"))
				currentHandler = previousHandler;
		}

		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("image")) {
				tileSet.setSource(getString(atts, "source"));
				tileSet.setTilesPerRow((getInteger(atts, "width") - 2 * tileSet.getMargin() + tileSet.getSpacing())
						/ (tileSet.getTileWidth() + tileSet.getSpacing()));
				tileSetsGids.put(firstgid, tileSetIndex);
				tileSetIndex++;
			} else {
				throw new SAXException("unknown tag:" + qName);
			}

		}

	}

	private class TMXHandler extends DefaultHandler {

		public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
			if (qName.equalsIgnoreCase("map")) {
				map = new TileMap();
				map.setWidth(getInteger(atts, "width"));
				map.setHeight(getInteger(atts, "height"));
				map.setTileWidth(getInteger(atts, "tilewidth"));
				map.setTileHeight(getInteger(atts, "tileheight"));
				currentHandler = new MapHandler(this, map);
			}
		}

	}

	private DefaultHandler currentHandler = new TMXHandler();

	private TileMap map = null;

	private int tileSetIndex = 0;

	private HashMap<Integer, Integer> tileSetsGids = new HashMap<Integer, Integer>();

	/*
	 * private int getInteger(String value){ if(value!=null){
	 * return(Integer.valueOf(value)); } return 0; }
	 */

	private Tile[][] data2tilesArray(String data, String encoding, String compression, TileMapLayer mapLayer) throws IOException {
		Tile[][] tiles = new Tile[mapLayer.getWidth()][mapLayer.getHeight()];

		if (encoding != null && "base64".equalsIgnoreCase(encoding) || true) {
			if (data != null) {
				char[] enc = data.trim().toCharArray();
				byte[] dec = Base64.decode(enc);
				ByteArrayInputStream bais = new ByteArrayInputStream(dec);
				InputStream is;

				if ("gzip".equalsIgnoreCase(compression)) {
					is = new GZIPInputStream(bais);
				} else if ("zlib".equalsIgnoreCase(compression)) {
					is = new InflaterInputStream(bais);
				} else {
					is = bais;
				}

				for (int y = 0; y < mapLayer.getHeight(); y++) {
					for (int x = 0; x < mapLayer.getWidth(); x++) {
						int tileId = 0;
						tileId |= is.read();
						tileId |= is.read() << 8;
						tileId |= is.read() << 16;
						tileId |= is.read() << 24;

						int TGID = -1;
						Iterator<Integer> i = tileSetsGids.keySet().iterator();
						while (i.hasNext()) {
							int gid = i.next();
							if ((gid <= tileId) && (gid >= TGID)) {
								TGID = gid;
							}
						}

						Tile tile = new Tile();
						tile.setGid(TGID);

						if (TGID != -1) {
							tile.setTn(tileSetsGids.get(TGID));
							tile.setN(tileId - TGID);
						} else {
							tile.setTn(-1);
							tile.setN(-1);
						}

						tiles[x][y] = tile;
					}
				}
			}
		}
		return (tiles);
	}

	private int getInteger(Attributes atts, String name) {
		String value = atts.getValue(name);
		if (value != null) {
			return (Integer.valueOf(value));
		}
		return 0;
	}

	private ArrayList<Vertex> getPoints(String pointsString) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		String pointsArrayString[] = pointsString.split("\\ ");
		for (int i = 0; i < pointsArrayString.length; i++) {
			String pointCoordsArrayString[] = pointsArrayString[i].split("\\,");
			double x = Double.valueOf(pointCoordsArrayString[0]);
			double y = Double.valueOf(pointCoordsArrayString[1]);
			vertices.add(new Vertex(x, y));
		}
		return vertices;
	}

	private String getString(Attributes atts, String name) {
		String value = atts.getValue(name);
		if (value == null) {
			return ("");
		}
		return value;
	}

	public TileMap getMap(String sourceName) throws Exception {
		TileMap result = null;
		InputStream in = getClass().getResourceAsStream(sourceName);
		TMXReader tmxReader = new TMXReader();
		result = tmxReader.readMap(in);
		in.close();
		return result;
	}

	public TileMap readMap(InputStream IS) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(IS, new SAXStub());
		return (map);
	}
}
