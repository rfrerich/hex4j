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

package de.lambdamoo.hex4j.tile.core;

import java.io.Serializable;
import java.util.HashMap;

import de.lambdamoo.hex4j.hexmath.obj.Hex;

public class TileMap implements Serializable {

	private static final long serialVersionUID = -4418881735109587622L;

	private int height = 0;
	private HashMap<String, TileMapLayer> mapLayers = new HashMap<String, TileMapLayer>();
	private HashMap<String, ObjectGroup> objectGroups = new HashMap<String, ObjectGroup>();
	private HashMap<String, String> properties = new HashMap<String, String>();

	private int tileHeight = 0;

	private HashMap<Integer, TileSet> tileSets = new HashMap<Integer, TileSet>();
	private int tileWidth = 0;
	private int width = 0;

	public TileMap() {
	}

	public void add(String name, TileMapLayer mapLayer) {
		mapLayers.put(name, mapLayer);
	}

	public void add(String name, ObjectGroup objectGroup) {
		objectGroups.put(name, objectGroup);
	}

	public void add(TileSet tileSet) {
		tileSets.put(tileSets.size(), tileSet);
	}

	public boolean canMove(Hex pos) {
		return true;
	}

	public int getHeight() {
		return height;
	}

	public TileMapLayer getLayer(String name) {
		return mapLayers.get(name);
	}

	public HashMap<String, TileMapLayer> getMapLayers() {
		return mapLayers;
	}

	public ObjectGroup getObjectGroup(String name) {
		return objectGroups.get(name);
	}

	public String getProperty(String key) {
		return (properties.get(key));
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public HashMap<Integer, TileSet> getTileSets() {
		return tileSets;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}

	public void setProperty(String key, String value) {
		properties.put(key, value);
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
