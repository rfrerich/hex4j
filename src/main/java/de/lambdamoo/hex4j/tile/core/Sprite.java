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

public class Sprite implements Serializable {
	private static final long serialVersionUID = 6354328904550630965L;

	private int x = 0;
	private int y = 0;
	private int w = 0;
	private int h = 0;
	private int centerX = 0;
	private int centerY = 0;

	public Sprite() {
	}

	public Sprite(int centerX, int centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
	}

	public void correlate(TileMapObject obj) {
		if ((obj.getX() < centerX) && (obj.getY() < centerY) && ((obj.getX() + obj.getWidth()) > centerX)
				&& ((obj.getY() + obj.getHeight()) > centerY)) {
			// inside boundaries
			this.x = obj.getX();
			this.y = obj.getY();
			this.w = obj.getWidth();
			this.h = obj.getHeight();
		}
	}

}
