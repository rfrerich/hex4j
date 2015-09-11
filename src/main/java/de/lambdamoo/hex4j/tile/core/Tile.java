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

public class Tile implements Serializable {
	private static final long serialVersionUID = -5060395762974647415L;

	private int Tn = -1;

	private int blocked = 0;
	private int lucent = 0;

	public int getBlocked() {
		return blocked;
	}

	public int x = -1;
	public int y = -1;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}

	public int getLucent() {
		return lucent;
	}

	public void setLucent(int lucent) {
		this.lucent = lucent;
	}

	private int n = -1;
	private int gid = -1;

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public Tile() {
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getTn() {
		return Tn;
	}

	public void setTn(int Tn) {
		this.Tn = Tn;
	}
}
