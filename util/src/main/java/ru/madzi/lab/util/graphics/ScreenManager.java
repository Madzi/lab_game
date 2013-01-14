package ru.madzi.lab.util.graphics;

import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;

/**
 * Менеджер экрана.
 */
public class ScreenManager {

    private GraphicsDevice device;

    private Window window;

    /**
     * Конструктор, инициализирующий менеджер экрана.
     */
    public ScreenManager() {
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    /**
     * Получение списка доступных режимов экрана.
     * 
     * @return массив доступных режимов экрана
     */
    public DisplayMode[] getCompDisplayModes() {
        return device.getDisplayModes();
    }

    /**
     * Возвращает первый из списка подходящий режим для экрана.
     * 
     * @param modes список желаемых режимов
     * @return подходящий режим или <b>null</b>, если таковой не найден
     */
    public DisplayMode findFirstCompatibleMode(DisplayMode[] modes) {
        DisplayMode[] goodModes = device.getDisplayModes();
        for (DisplayMode mode : modes) {
            for (DisplayMode goodMode : goodModes) {
                if (displayModesMatch(mode, goodMode)) {
                    return mode;
                }
            }
        }
        return null;
    }

    /**
     * Сравнивает два видео-режима.
     * Видео-режимы считаются совпавшими если у них одинаковая ширина и высота,
     * а также одинаковая или неопределённая частота обновления и количество бит на пиксель.
     * 
     * @param mode1 первый видео-режим
     * @param mode2 второй видео-режим
     * @return <b>true</b> если видео-режимы совпадают
     */
    public boolean displayModesMatch(DisplayMode mode1, DisplayMode mode2) {
        boolean match = mode1.getWidth() == mode2.getWidth() 
                     && mode1.getHeight() == mode2.getHeight();
        match = match && (mode1.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI
                       || mode2.getBitDepth() == DisplayMode.BIT_DEPTH_MULTI
                       || mode1.getBitDepth() == mode2.getBitDepth());
        match = match && (mode1.getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN
                       || mode2.getRefreshRate() == DisplayMode.REFRESH_RATE_UNKNOWN
                       || mode1.getRefreshRate() == mode2.getRefreshRate());
        return match;
    }

    /**
     * Возвращает активное окно программы.
     * 
     * @param displayMode видео-режим
     * @param fullscreen признак полно-экранного режима
     * @return окно
     */
    public JFrame setFullscreen(DisplayMode displayMode, boolean fullscreen) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);
        if (fullscreen) {
            frame.setUndecorated(true);
            device.setFullScreenWindow(frame);
            if (displayMode != null && device.isDisplayChangeSupported()) {
                try {
                    device.setDisplayMode(displayMode);
                } catch (IllegalArgumentException e) {
                }
            }
        }
        frame.setSize(displayMode.getWidth(), displayMode.getHeight());
        if (!fullscreen) {
          frame.setVisible(true);
        }
        try {
            EventQueue.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    frame.createBufferStrategy(2);
                }

            });
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
        }
        window = (Window) frame;
        return frame;
    }

    /**
     * Возвращает холст для рисования.
     * 
     * @return холст
     */
    public Graphics2D getGraphics() {
        if (window != null) {
            if (window.getBufferStrategy() != null) {
                return (Graphics2D) window.getBufferStrategy().getDrawGraphics();
            }
            return (Graphics2D) window.getGraphics();
        }
        return null;
    }

    /**
     * Обновляет экран.
     */
    public void update() {
        if (window != null) {
            BufferStrategy strategy = window.getBufferStrategy();
            if (strategy != null && !strategy.contentsLost()) {
                strategy.show();
            }
        }
    }

    /**
     * Возвращает окно программы.
     * 
     * @return окно
     */
    public JFrame getWindow() {
        return (JFrame) window;
    }

    /**
     * Возвращает ширину экрана.
     * 
     * @return ширина
     */
    public int getWidth() {
        return window != null ? window.getWidth() : 0;
    }

    /**
     * Возвращает высоту экрана.
     * 
     * @return высота
     */
    public int getHeight() {
        return window != null ? window.getHeight() : 0;
    }

    /**
     * Восстанавливает экран после работы программы.
     */
    public void resotreScreen() {
        if (window != null) {
            window.dispose();
        }
        device.setFullScreenWindow(null);
    }

    /**
     * Создаёт изображение, совместимое с текущим видео-режимом.
     * 
     * @param w - ширина
     * @param h - высота
     * @param transparancy - прозрачность
     * @return изображение
     */
    public BufferedImage createCompativleImage(int w, int h, int transparancy) {
        return window != null ? window.getGraphicsConfiguration().createCompatibleImage(w, h, transparancy) : null;
    }

}
