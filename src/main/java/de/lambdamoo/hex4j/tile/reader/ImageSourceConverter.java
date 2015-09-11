package de.lambdamoo.hex4j.tile.reader;

/**
 * This interface can be implemented and set in the TileMapImages object to
 * transform the source path attributes for images in the *.tmx file
 * 
 */
public interface ImageSourceConverter {
	String tranformSource(String source);
}
