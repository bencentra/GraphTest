GraphTest
=========

Sample sketch for the "Graph" UI component for [EEGJ](https://github.com/codenameriri/EEGJ).

RiriGraph
---------

To create a RiriGraph object:
```java
// Instance vars
PImage backgroundImg;
RiriGraph graph;

void setup() {
	// Load the background image
	backgroundImg = loadImage('pathtofile.png');
	// Create the graph (xPos, yPos, width, height, background, left/right)
	graph = new RiriGraph(0,0,width,height,backgroundImage,0);
}

void draw() {
	// Draw the graph	
	graph.draw();
}

void mousePressed() {
	// Click to set the position of the grpah
	graph.setMarkerX(mouseX);
}
```

The graph starts at position 0. Use the `setMarkerX()` method to set the position of the graph. As long as the graph's `draw()` method is being called in the main sketch's draw loop, it will automatically animate. 