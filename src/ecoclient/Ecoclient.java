package ecoclient;

import ecosim.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import sun.font.TrueTypeFont;

import java.awt.*;
import java.nio.DoubleBuffer;
import java.time.Duration;
import java.time.Instant;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

// Temp

public class Ecoclient {
    public Ecosystem ecosystem;

    private long window;
    private long lastFrameTime = 0;

    int windowWidth = 1280;
    int windowHeight = 720;

    int[] mouseButtons;
    int scrollDir = 0;

    int zoomWidth = windowWidth;
    int zoomHeight = windowHeight;
    float totalZoomScale = 1;

    float transX = 0;
    float transY = 0;

    double pmouseX = 0;
    double pmouseY = 0;

    double clickX = 0;
    double clickY = 0;

    double scalex = 1;

    float translate = 1f;

    TrueTypeFont font;


    public Ecoclient(Ecosystem ecosystem) {

        this.ecosystem = ecosystem;

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
                glfwSetWindowShouldClose(window, true);
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            // Sets mouse button in array to either 1 or 0, clicked or not
            mouseButtons[button] = action;

            if(button == 0 && action == 1) {
                clickX = mouseX(window);
                clickY = mouseY(window);
            }

            if (button == 0 && action == 0) {

                if(clickX == mouseX(window) && clickY == mouseY(window)) {
                    // Convert mouse coordinates on screen to world coordinates
                    float mWorldX = (((float) mouseX(window) - windowWidth / 2) * totalZoomScale) - transX;
                    float mWorldY = (((float) mouseY(window) - windowHeight / 2) * totalZoomScale) - transY;

                    for (Organism o : ecosystem.organisms) {
                        // If the world mouse coordinates are inside of organism's circle,
                        if (Math.hypot(mWorldX - o.x, mWorldY - o.y) < o.size) {
                            o.printData();
                        }
                    }
                }
            }
        });

        glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {

            float zoomScale = 1;

            if (yoffset > 0) {
                zoomScale += 0.1;
            } else {
                zoomScale -= 0.1;
            }

            if (zoomWidth * zoomScale <= windowWidth * 5 && zoomWidth * zoomScale > windowWidth / 5) {

                // TODO: 2/28/2021 make screen zoom on mouse cursor rather than origin

                zoomWidth *= zoomScale;
                zoomHeight *= zoomScale;
                totalZoomScale /= zoomScale;

                double mXOffset = windowWidth / 2 - mouseX(window);
                double mYOffset = windowHeight / 2 - mouseY(window);

                //System.out.println("X: " + mouseX(window) + "\nY: " + mouseY(window) + "\n");

                glTranslated(-transX - ecosystem.worldWidth / 2, -transY - ecosystem.worldHeight / 2, 0);
                glScalef(zoomScale, zoomScale, 1f);
                glTranslated(transX + ecosystem.worldWidth / 2, transY + ecosystem.worldHeight / 2, 0);
                scalex *= 1 + (float) zoomScale / 10;
            }
        });

        glfwMakeContextCurrent(window);


        // Enable or disable VSync
        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();

        // Move view to roughly center of the world
        glTranslated(-ecosystem.worldWidth / 2, -ecosystem.worldHeight / 2, 0);

        transX -= ecosystem.worldWidth / 2;
        transY -= ecosystem.worldHeight / 2;


//		glEnable(GL_MULTISAMPLE);
        glClearColor(0.16f, 0.7f, 0.33f, 1f);
//        glViewport(0, 0, windowWidth, windowHeight);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-windowWidth / 2, windowWidth / 2, -windowHeight / 2, windowHeight / 2, -1, 1);
        glScalef(1, -1, 1);
    }

    private void loop() {
        // Rendering loop
        while (!glfwWindowShouldClose(window)) {

            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

            for (int i = ecosystem.organisms.size()-1; i > 0; i--) {
                Organism o = ecosystem.organisms.get(i);

                o.update(ecosystem);
                drawOrganism(o);

                if (o instanceof Plant && o.size <= 0.5) {
                    ecosystem.organisms.remove(o);
                }
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

            if (mouseButtons[0] == 1) {
                double translatex = (mouseX(window) * windowWidth / zoomWidth - pmouseX * windowWidth / zoomWidth);
                double translatey = (mouseY(window) * windowHeight / zoomHeight - pmouseY * windowHeight / zoomHeight);

                transX += translatex;
                transY += translatey;
                glTranslated(translatex, translatey, 0);
            }


            pmouseX = mouseX(window);
            pmouseY = mouseY(window);

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
        if (o instanceof Animal) {
            try {
                glBegin(GL_LINE_STRIP);
                glVertex2f(o.x, o.y);
                glVertex2f(((Animal) o).target.x, ((Animal) o).target.y);
                glEnd();
            } catch (Exception e) {
                glEnd();
            }
        }

        int segmentCount = 32;
        float r = o.size / 2;

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBegin(GL_POLYGON);
        glColor4f(Math.abs(o.colorR) / 255f, Math.abs(o.colorG) / 255f, Math.abs(o.colorB) / 255f, /*Math.abs(o.colorA) / 255f*/ 1);

        for (int i = 0; i < segmentCount; i++) {
            float theta = (float) (i * 2 * Math.PI / segmentCount);

            double cx = r * Math.cos(theta);
            double cy = r * Math.sin(theta);

            glVertex2d(o.x + cx, o.y + cy);
        }
        glEnd();
        glDisable(GL_BLEND);

    }

    public static void main(String[] args) {
        Ecosystem e = new Ecosystem(1280 * 4, 720 * 4);
        e.createAllOrganisms(5, 10, 5, 4, 20, 10);

        for (Organism o : e.organisms) {
            if (o instanceof Herbivore) {
                ((Animal) o).findFood(e.organisms);
            }
            if (o instanceof Carnivore) {
                ((Animal) o).findFood(e.organisms);
            }
        }

        new Ecoclient(e);

    }

}