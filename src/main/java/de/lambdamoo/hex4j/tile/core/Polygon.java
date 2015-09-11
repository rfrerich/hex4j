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

public class Polygon implements Serializable{

	private static final long serialVersionUID = 3554421561379376753L;

	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

	public Polygon(){
	}
	
	public Polygon(ArrayList<Vertex> vertices) {
		this.vertices=vertices;
	}

	public boolean isCCW() {
		    return (getArea() > 0.0f);
	}

	private float getArea() {
		  // TODO: fix up the areaIsSet caching so that it can be used
		  //if (areaIsSet) return area;
		  float area = 0.0f;
		  Vertex v_last=vertices.get(vertices.size()-1);
		  Vertex v_first=vertices.get(0);
		  
		  area += v_last.getX()*v_first.getY()-v_first.getX()*v_last.getY();

		  for (int i=0; i<vertices.size()-1; ++i){
			  Vertex v=vertices.get(i);
			  Vertex v_next=vertices.get(i+1);
			  area += v.getX()*v_next.getY()-v_next.getX()*v.getY();
		  }
		  area *= .5f;
		  return area;
	}

	public void reversePolygon() {
        if (vertices.size() == 1)
            return;
        int low = 0;
        int high = vertices.size() - 1;
        while (low < high) {
        	Vertex l=vertices.get(low);
        	Vertex h=vertices.get(high);
        
            double buffer = l.getX();
            l.setX(h.getX());
            h.setX(buffer);
            buffer = l.getY();
            l.setY(h.getY());
            h.setY(buffer);
            ++low;
            --high;
        }
	}

	public ArrayList<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
	}
	
}
