import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class GraphTest extends PApplet {

/*
*	GraphTest.pde - Demo of the focus/relax graphs
*
*	To-do:
*	- Add 3rd color to gradient for real
*	- Integrate with BPM
*/

// Sketch dimensions
int WIDTH = 1280;
int HEIGHT = 600;
int GRAPH_W = 600;

boolean playing = true;

// Graphs
RiriGraph relax, focus;

// Setup 
public void setup(){ 
	// Sketch setup
	size(WIDTH, HEIGHT); 
	background(0);
	//frameRate(60);
	// Colors
	int veryR = color(13, 255, 234);
	int someR = color(16, 255, 160);
	int neutral = color(255, 220, 73);
	int someF = color(240, 133, 46);
	int veryF = color(255, 34, 184);
	int[] colors_relax = {neutral, someR, veryR};
	int[] colors_focus = {veryF, someF, neutral};
	// Graph
	focus = new RiriGraph(0,0,GRAPH_W,HEIGHT,veryF,neutral,"FOCUS",1);
	//focus = new RiriGraph(0,0,GRAPH_W,HEIGHT,colors_focus,"FOCUS",1);
	relax = new RiriGraph(WIDTH - GRAPH_W,0,GRAPH_W,HEIGHT,neutral,veryR,"RELAX",0);
	//relax = new RiriGraph(WIDTH - GRAPH_W,0,GRAPH_W,HEIGHT,colors_relax,"RELAX",0);
} 

public void draw() {
	if (playing) {
		background(0);
		focus.draw();
		relax.draw();
	}	
}

public void keyPressed() {
	if (key == ' ') {
		playing = !playing;	
	}	
}

class RiriGraph {

	// Constants
	private int HEADER_Y = 35;
	private int MARKER_WIDTH = 10;
	private int SETTLE_RATE = 4;
	private int EASING_STEPS = 5;
	private int INTERVAL = 750;
	private int RIGHT = 0;
	private int LEFT = 1;

	// Fonts
	private PFont bold, thin;
	
	// Colors
	private int neutral, very;
	private int[] colors = new int[3];
	
	// Graph positions and size
	private int xPos, yPos, graphWidth, graphHeight, direction;
	
	// Name
	private String name;
	
	// Marker positions
	private int markerX, oldX, newX;
	
	// Timekeeping
	private int lastMillis, easing;

	public RiriGraph() {
		background(0);
		// Font and text setup
		bold = loadFont("GothamBold-24.vlw");
		thin = loadFont("GothamThin-24.vlw");
		// Variable initialization
		neutral = color(0,0,0);
		very = color(0,0,0);
		xPos = 0;
		yPos = 0;
		graphWidth = 0;
		graphHeight = 0;
		markerX = 0;
		oldX = 0;
		newX = 0;
		lastMillis = 0;
		easing = EASING_STEPS;
		name = "GRAPH";
		direction = RIGHT;
	}
	
	public RiriGraph(int aX, int aY, int aW, int aH, int cN, int cV, String aName, int aD) {
		background(0);
		// Font and text setup
		bold = loadFont("GothamBold-24.vlw");
		thin = loadFont("GothamThin-24.vlw");
		// Variable initialization
		neutral = cN;
		very = cV;
		xPos = aX;
		yPos = aY;
		graphWidth = aW;
		graphHeight = aH;
		markerX = 0;
		oldX = 0;
		newX = 0;
		lastMillis = 0;
		easing = EASING_STEPS;
		name = aName;
		direction = (aD == LEFT || aD == RIGHT) ? aD : RIGHT;
	}
	
	public RiriGraph(int aX, int aY, int aW, int aH, int[] c, String aName, int aD) {
		background(0);
		// Font and text setup
		bold = loadFont("GothamBold-24.vlw");
		thin = loadFont("GothamThin-24.vlw");
		// Variable initialization
		colors = c;
		xPos = aX;
		yPos = aY;
		graphWidth = aW;
		graphHeight = aH;
		markerX = 0;
		oldX = 0;
		newX = 0;
		lastMillis = 0;
		easing = EASING_STEPS;
		name = aName;
		direction = (aD == LEFT || aD == RIGHT) ? aD : RIGHT;	
	}
	
	public void draw() {
		fill(0,0,0);
		noStroke();
		rect(xPos, yPos, graphWidth, graphHeight);
		// Gradient background
		if (colors[0] != 0) {
			gradientRect(xPos, yPos + HEADER_Y+1, graphWidth, graphHeight-HEADER_Y+1, colors, 'y');	
		}
		else {
			gradientRect(xPos, yPos + HEADER_Y+1, graphWidth, graphHeight-HEADER_Y+1, neutral, very, 'y');
		}
		// Determine the value of the graph
		if (millis() > lastMillis + INTERVAL) {
			lastMillis = millis();
			newX = (int) random(0, graphWidth);
			// Rise
			if (newX > markerX) {
				oldX = markerX;
				easing = 0;	
			}
			// Settle
			else {
				markerX -= SETTLE_RATE;
				if (markerX < 0) {
					markerX = 0;	
				}	
			}
		}
		else {
			// Rise
			if (easing < EASING_STEPS) {
				markerX += (int) ((newX - oldX) / EASING_STEPS);
				easing++;
			}
			// Settle
			else {
				markerX -= SETTLE_RATE;
				if (markerX < 0) {
					markerX = 0;	
				}	
			}
		}	
		// Marker
		noStroke();
		if (direction == RIGHT) {
			fill(255,255,255);
			rect(xPos + markerX, yPos + HEADER_Y+1, MARKER_WIDTH, graphHeight-HEADER_Y+1);
			fill(0,0,0);
			rect(xPos + markerX + MARKER_WIDTH, yPos + HEADER_Y+1, graphWidth - (markerX + MARKER_WIDTH), graphHeight-HEADER_Y+1);
		}
		else {
			fill(255,255,255);
			rect(xPos + graphWidth - markerX, yPos + HEADER_Y+1, MARKER_WIDTH, graphHeight-HEADER_Y+1);
			fill(0,0,0);
			rect(0, yPos + HEADER_Y+1, xPos + graphWidth - markerX, graphHeight-HEADER_Y+1);
		}
		// Column headers
		textAlign(CENTER);
		textFont(thin, 24);
		fill(255, 255, 255);
		if (direction == RIGHT) {
			text("NEUTRAL", xPos + graphWidth/6, 25);
			text("VERY", xPos + (graphWidth - graphWidth/6), 25);
		}
		else {
			text("VERY", xPos + graphWidth/6, 25);
			text("NEUTRAL", xPos + (graphWidth - graphWidth/6), 25);
		}
		textFont(bold, 24);
		text(name, xPos + graphWidth/2, 25);
		// Column lines
		stroke(255,255,255);
		line(xPos, yPos + HEADER_Y, xPos + graphWidth, yPos + HEADER_Y);
		line(xPos + graphWidth/3, yPos, xPos + graphWidth/3, yPos + graphHeight);
		line(xPos + (2*graphWidth/3), yPos, xPos + (2*graphWidth/3), yPos + graphHeight);
	}
	
	// Draw a rectangular gradient
	// Based on: http://processing.org/examples/lineargradient.html
	private void gradientRect(int x, int y, float w, float h, int c1, int c2, char axis) {
		noFill();
		// Top to bottom gradient
		if (axis == 'x') {  
			for (int i = y; i <= y+h; i++) {
		    	float inter = map(i, y, y+h, 0, 1);
		    	int c = lerpColor(c1, c2, inter);
		    	stroke(c);
		    	line(x, i, x+w, i);
		    }
		}  
		// Left to right gradient
		else if (axis == 'y') {  
			for (int i = x; i <= x+w; i++) {
		      	float inter = map(i, x, x+w, 0, 1);
		      	int c = lerpColor(c1, c2, inter);
		      	stroke(c);
		      	line(i, y, i, y+h);
		    }
		}
	}
	
	// Draw a rectangular gradient with more than two colors (at equal intervals apart)
	// Based on: http://processing.org/examples/lineargradient.html
	private void gradientRect(int x, int y, float w, float h, int[] colors, char axis) {
		noFill();
		// Top to bottom gradient
		if (axis == 'x') {
			for (int i = 0; i < colors.length-1; i++) {
				int start = (int) (y + (y+h) * i);
				int end = (int) (y + (y+h) * (i+1));
				for (int j = start; j < end; j++) {
					float inter = map(j, start, end, 0, 1);
					int c = lerpColor(colors[i], colors[i+1], inter);
					stroke(c);
					line(x, j, x+w, j);	
				}
			}
		}
		// Left to right gradient
		else if (axis == 'y') {
			for (int i = 0; i < colors.length-1; i++) {
				int start = (int) (x + (x+w) * i);
				int end = (int) (x + (x+w) * (i+1));	
				for (int j = start; j < end; j++) {
					float inter = map(j, start, end, 0, 1);
					int c = lerpColor(colors[i], colors[i+1], inter);
					stroke(c);
					line(y, j, y+w, j);	
				}
			}	
		}
	}
	
	// Getters
	public PFont getBoldFont() {
		return bold;	
	}
	
	public PFont getThinFont() {
		return thin;	
	}
	
	public int getNeutralColor() {
		return neutral;	
	}
	
	public int getVeryColor() {
		return very;	
	}
	
	public int getXPos() {
		return xPos;	
	}
	
	public int getYPos() {
		return yPos;	
	}
	
	public int getGraphWidth() {
		return graphWidth;	
	}
	
	public int getGraphHeight() {
		return graphHeight;	
	}
	
	public String getGraphName() {
		return name;	
	}
	
	public int getMarkerX() {
		return markerX;	
	}
	
	// Setters
	public void setBoldFont(PFont f) {
		bold = f;	
	}
	
	public void setThinFont(PFont f) {
		thin = f;	
	}
	
	public void setNeutralColor(int c) {
		neutral = c;	
	}
	
	public void setVeryColor(int c) {
		very = c;
	}
	
	public void setXPos(int x) {
		xPos = x;	
	}
	
	public void setYPos(int y) {
		yPos = y;
	}
	
	public void setGraphWidth(int w) {
		graphWidth = w;	
	}
	
	public void setGraphHeight(int h) {
		graphHeight = h;
	}
	
	public void setGraphName(String n) {
		name = n;	
	}
	
	public void setMarkerX(int x) {
		markerX = x;	
	}
}
    static public void main(String args[]) {
        PApplet.main(new String[] { "--bgcolor=#ECE9D8", "GraphTest" });
    }
}
