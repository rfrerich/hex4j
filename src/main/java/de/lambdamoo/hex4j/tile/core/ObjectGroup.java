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
import java.util.HashMap;
import java.util.Iterator;

public class ObjectGroup implements Serializable {


	//private String name="";
	
	private static final long serialVersionUID = -5205407232764879741L;

	private ArrayList<TileMapObject> objects = new ArrayList<TileMapObject>();
	
	private HashMap<String,Animation> animations = new HashMap<String,Animation>();
	
	public ObjectGroup(){
	}
	
	/*
	public void addAnimation(String name,Animation animation){
		animations.put(name, animation);
	}*/
	
	public void add(HashMap<String,Animation> animations){
		this.animations=animations;
	}

	public void add(ArrayList<TileMapObject> objects){
		this.objects=objects;
	}


	public Animation getAnimation(String name){
		return animations.get(name);
	}

	public void loadShapes(TMXShapesProvider provider){

		Iterator<TileMapObject> objectsIterator = objects.iterator();
	    while (objectsIterator.hasNext()) {
	    	TileMapObject mapObject = objectsIterator.next();

	    	if((mapObject.getWidth()!=0)&&(mapObject.getHeight()!=0)){ //box
	    		provider.box(mapObject, mapObject.getX(), mapObject.getY(), mapObject.getWidth(), mapObject.getHeight());
	    	}else{
		    	Iterator<Polygon> polygonsIterator = mapObject.getPolygonsIterator();
			    while (polygonsIterator.hasNext()) {
			    	provider.polygon(mapObject, polygonsIterator.next());
			    }

		    	Iterator<Polyline> polylinesIterator = mapObject.getPolylinesIterator();
			    while (polylinesIterator.hasNext()) {
			    	provider.polyline(mapObject, polylinesIterator.next());
			    }
	    	}
	    }
	}

	public void loadObjects(TMXObjectsProvider provider){
		Iterator<TileMapObject> objectsIterator = objects.iterator();
	    while (objectsIterator.hasNext()) {
	    	TileMapObject mapObject = objectsIterator.next();
	    	provider.object(mapObject);
	    }
	}
	
	/*
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}*/

	public void add(TileMapObject mapObject) {
		objects.add(mapObject);
	}
}
