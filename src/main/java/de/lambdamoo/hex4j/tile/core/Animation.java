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
import java.util.ArrayList;
import java.util.Iterator;

public class Animation implements Serializable {

	private static final long serialVersionUID = 1597652044776491816L;

	// private ArrayList<Vertex> centers = new ArrayList<Vertex>();

	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();

	/*
	 * private int width=0; private int height=0; private int centerX=0; private
	 * int centerY=0; private int delay=0; private boolean repeat=false;
	 */
	private int mapOffsetX = 0;
	private int mapOffsetY = 0;

	public Animation() {
	}

	public void setCenters(ArrayList<Vertex> centers) {
		Iterator<Vertex> itr = centers.iterator();
		while (itr.hasNext()) {
			Vertex v = itr.next();
			sprites.add(new Sprite((int) (v.getX()) + mapOffsetX, (int) (v.getY()) + mapOffsetY));
		}
	}

	public Animation(int mapOffsetX, int mapOffsetY) {
		this.mapOffsetX = mapOffsetX;
		this.mapOffsetY = mapOffsetY;
	}

	/*
	 * 
	 * 
	 * public int getWidth() { return width; }
	 * 
	 * public void setWidth(int width) { this.width = width; }
	 * 
	 * public int getHeight() { return height; }
	 * 
	 * public void setHeight(int height) { this.height = height; }
	 * 
	 * public int getCenterX() { return centerX; }
	 * 
	 * public void setCenterX(int centerX) { this.centerX = centerX; }
	 * 
	 * public int getCenterY() { return centerY; }
	 * 
	 * public void setCenterY(int centerY) { this.centerY = centerY; }
	 * 
	 * public int getDelay() { return delay; }
	 * 
	 * public void setDelay(int delay) { this.delay = delay; }
	 * 
	 * public void setRepeat(boolean repeat) { this.repeat = repeat; }
	 * 
	 * public boolean isRepeat() { return repeat; }
	 */

	public int getSize() {
		return (sprites.size());
	}

	public void correlate(TileMapObject obj) {
		for (Sprite sprite : sprites) {
			sprite.correlate(obj);
		}

	}
}
