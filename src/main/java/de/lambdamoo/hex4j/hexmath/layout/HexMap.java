package de.lambdamoo.hex4j.hexmath.layout;

import de.lambdamoo.hex4j.tile.core.TileMap;
import de.lambdamoo.hex4j.tile.reader.TileMapImages;

public class HexMap<T> {
	protected TileMap tilemap = null;

	private TileMapImages tileImages = new TileMapImages();

	private HexLayout hexLayout = new HexLayout();

	public HexLayout getHexLayout() {
		return hexLayout;
	}

	public TileMap getTilemap() {
		return tilemap;
	}

	public void setTilemap(TileMap tilemap) {
		this.tilemap = tilemap;
	}

	public TileMapImages getTileImages() {
		return tileImages;
	}

	public void setTileImages(TileMapImages tileImages) {
		this.tileImages = tileImages;
	}

}
