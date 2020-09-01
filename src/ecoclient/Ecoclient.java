package ecoclient;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3ub;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import ecosim.Herbivore;
import ecosim.Organism;
import ecosim.Plant;

// Temp
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL15.*;

public class Ecoclient {

	private long window;
	private long lastFrameTime = 0;

	int windowWidth = 1280;
	int windowHeight = 720;

	int[] mouseButtons;
	int scrollDir = 0;
	
	float zoomWidth = windowWidth;
	float zoomHeight = windowHeight;
	
	float transX = 0;
	float transY = 0;
	
	float translate = 1f;

	ArrayList<Organism> organisms;

	public Ecoclient() {

		organisms = new ArrayList<Organism>();
		organisms.add(new Herbivore(320, 200, 50));
		organisms.add(new Plant(0, 0, 100));

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		// TODO Find way to get exact number of buttons on mouse
		mouseButtons = new int[5];

		// GLFW Error callbacks
		GLFWErrorCallback.createPrint(System.err).set();

		// GLFW Init
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // Lock window at resolution until WindowWidth & Height are changed
//		glfwWindowHint(GLFW_SAMPLES, 4);

		// Create the window
		window = glfwCreateWindow(windowWidth, windowHeight, "Ecosim", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		// Input callbacks
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			mouseButtons[button] = action;
		});

		glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
			scrollDir = (int) yoffset;
			
			zoomWidth *= 1+(float)-yoffset/10;
			zoomHeight *= 1+(float)-yoffset/10;
			
			transX += 1;

			
			glScalef(1+(float)yoffset/10, 1+(float)yoffset/10, 1);
		});

		 //Get the thread stack and push a new frame
//		try (MemoryStack stack = stackPush()) {
//			IntBuffer pWidth = stack.mallocInt(1); // int*
//			IntBuffer pHeight = stack.mallocInt(1); // int*
//
//			// Get the window size passed to glfwCreateWindow
//			glfwGetWindowSize(window, pWidth, pHeight);
//
//			// Get the resolution of the primary monitor
//			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//
//			// Center the window
//			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
//		} // the stack frame is popped automatically

		glfwMakeContextCurrent(window);

		// Enable or disable VSync
		glfwSwapInterval(1);

		glfwShowWindow(window);

		GL.createCapabilities();
		
//		glEnable(GL_MULTISAMPLE); 
		glClearColor(0.16f, 0.7f, 0.33f, 1f);
		glViewport(0, 0, windowWidth, windowHeight);
	    glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-windowWidth/2, windowWidth/2, -windowHeight/2, windowHeight/2, -1, 1);
		glScalef(1, -1, 1);
//		glTranslatef(0, -windowHeight, 0);
		
	}

	private void loop() {
		// Rendering loop
		while (!glfwWindowShouldClose(window)) {
			
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			

		    for (Organism o : organisms) {
		    	drawOrganism(o);
		    }
			
			

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events to invoke key callback
			glfwPollEvents();

			// Reset Scroll direction to neutral
			scrollDir = 0;
			

			// Calculate and print FPS
			long time = System.nanoTime();
			double framerate = 1000 / ((time - lastFrameTime) / 1000000.0);
		    //System.out.println("Frame Rate: " + framerate + " fps");
			lastFrameTime = time;

		}

	}

	// Functions to retrieve cursor position
	public double mouseX(long windowID) {
		DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(windowID, posX, null);
		return posX.get(0);
	}

	public double mouseY(long windowID) {
		DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(windowID, null, posY);
		return posY.get(0);
	}

	public void drawOrganism(Organism o) {
		int segmentCount = 32;
		float r = o.size / 2;

		glBegin(GL_POLYGON);
		glColor3ub(o.colorR, o.colorB, o.colorG);
//		glColor3f(0,0,0);
		
		for (int i = 0; i < segmentCount; i++) {
			float theta = (float) (i * 2 * Math.PI / segmentCount);

			double cx = r * Math.cos(theta);
			double cy = r * Math.sin(theta);

			glVertex2d(o.x + cx, o.y + cy);
		}
		glEnd();
	}

	public static void main(String[] args) {
		new Ecoclient();

	}

}
