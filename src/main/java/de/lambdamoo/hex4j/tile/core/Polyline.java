/*
 * Copyright [2012] [Sergey Mukhin]
 *
 * Licensed under the Apache License, Version 2.0 (the �License�); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an �AS IS� BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
*/

package de.lambdamoo.hex4j.tile.core;

import java.io.Serializable;
import java.util.ArrayList;

public class Polyline implements Serializable{

	private static final long serialVersionUID = -4582998900269145839L;

	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	public Polyline(){
	}
	
	public Polyline(ArrayList<Vertex> vertices) {
		this.setVertices(vertices);
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
	}

}
