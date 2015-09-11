package de.lambdamoo.hex4j.tile.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import de.lambdamoo.hex4j.tile.core.TileMap;
import de.lambdamoo.hex4j.tile.core.TileSet;

public class TileMapImages {

	protected Hashtable<Integer, Image> images = new Hashtable<Integer, Image>();

	public Image copyImage(PixelReader reader, int imageWidth, int imageHeight, int offsetX, int offsetY) {
		WritableImage dest = new WritableImage(imageWidth, imageHeight);
		PixelWriter writer = dest.getPixelWriter();
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				// reading a pixel from src image,
				// then writing a pixel to dest image
				int col = reader.getArgb(x + offsetX, y + offsetY);
				writer.setArgb(x, y, col);
			}
		}
		return dest;
	}

	public Image getImage(Integer gid) {
		return images.get(gid);
	}

	public void initImages(TileMap map) throws Exception {
		Collection<TileSet> sets = map.getTileSets().values();

		Iterator<TileSet> iter = sets.iterator();
		while (iter.hasNext()) {
			TileSet tileset = iter.next();
			initTileSetImages(tileset, loadImage(tileset.getSource()));
		}
	}

	/**
	 * This method proceeds one tile set
	 * 
	 * @param tileset
	 * @param image
	 */
	private void initTileSetImages(TileSet tileset, Image image) {
		PixelReader reader = image.getPixelReader();
		int imageWidth = tileset.getTileWidth();
		int imageHeight = tileset.getTileHeight();
		Integer id = tileset.getFirstGid();
		int offsetX = 0;
		int offsetY = 0;
		int column = 0;

		for (int i = 0; i < tileset.getTilecount(); i++) {
			Image dest = copyImage(reader, imageWidth, imageHeight, offsetX, offsetY);
			images.put(id + i, dest);

			// calculate next offset
			column++;
			if (column >= tileset.getTilesPerRow()) {
				column = 0;
				offsetX = 0;
				offsetY += imageHeight;
			} else {
				offsetX += imageWidth;
			}
		}
	}

	private ImageSourceConverter converter = null;

	public ImageSourceConverter getConverter() {
		return converter;
	}

	public void setConverter(ImageSourceConverter converter) {
		this.converter = converter;
	}

	public Image loadImage(String sourceName) throws IOException {
		if (this.converter != null) {
			sourceName = converter.tranformSource(sourceName);
		}
		try (InputStream in = getClass().getResourceAsStream(sourceName);) {
			Image image = new Image(in);
			return image;
		}
	}

}
