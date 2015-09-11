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
import java.util.HashMap;

public class TileMapObject implements Serializable{
	private static final long serialVersionUID = 186860584846921354L;

	private String name="";
	private String type="";
	private int x=0;
	private int y=0;
	private int width=0;
	private int height=0;
	
	private HashMap<String,String> properties = new HashMap<String,String>();
	private ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	private ArrayList<Polyline> polylines = new ArrayList<Polyline>();
	
	public TileMapObject(){
	}

	public String getProperty(String key){
		return(properties.get(key));
	}

	public void setProperty(String key, String value){
		properties.put(key, value);
	}
	
	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void add(Polygon polygon){
		polygons.add(polygon);
	}
	
	public void add(Polyline polyline){
		polylines.add(polyline);
	}

	public Iterator<Polygon> getPolygonsIterator(){
		return polygons.iterator();
	}

	public Iterator<Polyline> getPolylinesIterator(){
		return polylines.iterator();
	}
	
}
