package ru.madzi.lab.util;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import javax.swing.ImageIcon;
import ru.madzi.lab.util.graphics.ScreenManager;

/**
 * Ядро игрового движка.
 */
public abstract class Core {

    protected static final int FONT_SIZE = 24;

    private static final DisplayMode[] POSSIBLE_MODES = {
        new DisplayMode(1280, 1024, 24, 0),
        new DisplayMode(1280, 1024, 32, 0),
        new DisplayMode(1280, 1024, 16, 0),
        new DisplayMode(1280, 1024,8, 0),
        new DisplayMode(1024, 768, 24, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(1024, 768, 8, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 8, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 8, 0),
    };

    private boolean isRunning;

    protected boolean isFullscreen = false;

    protected DisplayMode displayMode = null;

    protected ScreenManager screenManager;

    public void stop() {
        isRunning = false;
    }

    public void run() {
        try {
            init();
            loop();
        } finally {
            if (isFullscreen) {
                screenManager.resotreScreen();
            }
            lazilyExit();
        }
    }

    public void lazilyExit() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {}
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    public void init() {
        screenManager = new ScreenManager();
        if (displayMode == null) {
            displayMode = screenManager.findFirstCompatibleMode(POSSIBLE_MODES);
        }
        if (!isFullscreen && displayMode == null) {
            displayMode = POSSIBLE_MODES[0];
        }
        Window window = screenManager.setFullscreen(displayMode, isFullscreen);
        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        window.setBackground(Color.BLUE);
        window.setForeground(Color.WHITE);
        isRunning = true;
    }

    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    public void loop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;
        while (isRunning) {
            long elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;
            update(elapsedTime);
            Graphics2D g = screenManager.getGraphics();
            if (g != null) {
                draw(g);
                g.dispose();
            }
            screenManager.update();
        }
    }

    public void update(long elapsedTime) {}

    public abstract void draw(Graphics2D g);

}
