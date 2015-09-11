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

public class TileSet implements Serializable {
	private static final long serialVersionUID = 4953187896835124187L;

	private String name = "";
	private String source = "";

	private int tilesPerRow = 0;
	private int tileWidth = 0;
	private int tileHeight = 0;
	private int spacing = 0;
	private int margin = 0;

	private int firstGid = -1;

	private int tilecount;

	public TileSet() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getTilesPerRow() {
		return tilesPerRow;
	}

	public void setTilesPerRow(int tilesPerRow) {
		this.tilesPerRow = tilesPerRow;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public void setFirstGid(int gid) {
		this.firstGid = gid;
	}

	public int getFirstGid() {
		return firstGid;
	}

	public void setTilecount(int count) {
		this.tilecount = count;

	}

	public int getTilecount() {
		return tilecount;
	}
}
