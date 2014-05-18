/*
*	GraphTest.pde - Demo of the focus/relax graphs
*
*	Made by Ben Centra
*/

// Sketch dimensions
int WIDTH = 1280;
int HEIGHT = 600;
int GRAPH_W = 600;
int GRAPH_H = 600;

boolean playing = true;

// Graphs
RiriGraph relax, focus;

// Background images 
PImage bg_focus, bg_relax;

// Setup 
void setup(){ 
	// Sketch setup
	size(WIDTH, HEIGHT); 
	background(0);
	frameRate(60);
	// Background images
	bg_focus = loadImage("focus_gradient2.png");
	bg_relax = loadImage("relax_gradient2.png");
	// Graph
	focus = new RiriGraph(0,0,GRAPH_W,GRAPH_H,bg_focus,1);
	relax = new RiriGraph(WIDTH - GRAPH_W,0,GRAPH_W,GRAPH_H,bg_relax,0);
} 

void draw() {
	if (playing) {
		background(255);
		// Draw the graphs
		focus.draw();
		relax.draw();
		fill(0);
		rect(WIDTH/2 - 1, 0, 2, HEIGHT);
	}	
}

// Set the value of the graphs
void keyPressed() {
	if (key == ' ') {
		playing = !playing;	
	}	
	if (key == '1') {
		focus.setMarkerX(focus.graphWidth);
		relax.setMarkerX(relax.graphWidth);
	}
	if (key == '2') {
		focus.setMarkerX(focus.graphWidth/2);
		relax.setMarkerX(relax.graphWidth/2);
	}
}
