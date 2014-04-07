/*
*	GraphTest.pde - Demo of the focus/relax graphs
*
*	To-do:
*	- Integrate with BPM
*/

// Sketch dimensions
int WIDTH = 1280;
int HEIGHT = 600;
int GRAPH_W = 600;

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
	//frameRate(60);
	// Background images
	bg_focus = loadImage("focus_gradient2.png");
	bg_relax = loadImage("relax_gradient2.png");
	// Graph
	focus = new RiriGraph(0,0,GRAPH_W,HEIGHT,bg_focus,"FOCUS",1);
	relax = new RiriGraph(WIDTH - GRAPH_W,0,GRAPH_W,HEIGHT,bg_relax,"RELAX",0);
} 

void draw() {
	if (playing) {
		background(0);
		focus.draw();
		relax.draw();
	}	
}

void keyPressed() {
	if (key == ' ') {
		playing = !playing;	
	}	
}
