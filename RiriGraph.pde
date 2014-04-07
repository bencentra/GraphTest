class RiriGraph {

	// Constants
	private int HEADER_Y = 35;
	private int MARKER_WIDTH = 10;
	private int SETTLE_RATE = 5;
	private int EASING_STEPS = 5;
	private int INTERVAL = 750;
	private int RIGHT = 0;
	private int LEFT = 1;

	// Fonts
	public PFont bold, thin;
	
	// Colors
	public PImage background = null;
	
	// Graph positions and size
	public int xPos, yPos, graphWidth, graphHeight, direction;
	
	// Name
	private String name;
	
	// Marker positions
	public int markerX;
	private int oldX, newX;
	
	// Timekeeping
	private int lastMillis, easing;

	public RiriGraph() {
		background(0);
		// Font and text setup
		bold = loadFont("GothamBold-24.vlw");
		thin = loadFont("GothamThin-24.vlw");
		// Variable initialization
		background = null;
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

	public RiriGraph(int aX, int aY, int aW, int aH, PImage bgFile, String aName, int aD) {
		background(0);
		// Font and text setup
		bold = loadFont("GothamBold-24.vlw");
		thin = loadFont("GothamThin-24.vlw");
		// Image setup
		background = bgFile;
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
		image(background, xPos, yPos + HEADER_Y+1, graphWidth, graphHeight);
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
}