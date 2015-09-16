# hex4j
This is a java implementation of the algorithms and the cube logic from Amit Patels "Hex Grids Implementation" site. I made few additions and added code for processing of Tiled TMX-files from Kevin Kelley and his project "simpletmx" (https://code.google.com/p/simpletmx/) and search algorithms from zeroturnaround and his project "jf-hw-performance" (https://github.com/zeroturnaround/jf-hw-performance). 

The main objective is to decouple the different parts of game processing: 
- hex math
- map and tiles
- pathfinding, line of sight, etc.

##Tile (from simpletmx)
I copied the classes to handle the data/object-model and the reading of TMX-files. I added variables to the data classes and added few helper class/methods to handle and load the images that are referenced in the TMX-files.

## hexmath (from Amit Patels cube logic)
I copied the example code for the implementation from Amit Patels web site and adapted it to instance and static class methhods. Additionally I took over some code for Tiled file handling and few search algorithms and added it to other sub packages 

- HexLayout: this is the class for holding the layout attributes like orientation and EVEN- or ODD-layout. I added all methods to this class, which need the orientation or layout variables to do the calculation.

- HexUtil2: this is the class with static methods to do calculations independent from the current layout or orientation

- HexMap: This class stores a tile map, the images and the HexLayout. There is no storage of hex fields, because the tile and tileset information comes from the TMX-files in offset coordinates (col/row). The hex math is only necessary to do the calculations, where the Offset class references the col/row (or x/y) of the tiles.

## Search algorithms (from jf-hw-performance)
I copied the interfaces structure and the standard algorithms and adapted it with the usage of the above interfaces, so this  is de-coupled from the hex math and tiles. Additionally i re-implemented the AStar-algorithm the way it is described in wikipedia.

- Finder: The finder class is de-coupled from the other components by three new interfaces: 
	SuccessorProducer returns the successors of a node in the pathfinding graph
	Blocked returns whether the tile is blocked or not. If it is blocked, it will not be added to the successor list
	LucentVisible	returns whether this tile is lucent or not. If not the line of sight is interrupted by this tile 

## Rendering
The rendering is not in scope of this packages. Personally I prefer the entity component systems in game design, where the data/object model is separated from the processing and rendering. You can find rendering code in the example in the GuiTest class. I prefer to use images for the tile rendering instead of painting polygons.

## Example and test classes
I took over the test cases from Amil Patel and added a test for an user interface to show example code when using this package. The full path to the test class is: \src\main\java\de\lambdamoo\hex4j\test\GuiTest.
