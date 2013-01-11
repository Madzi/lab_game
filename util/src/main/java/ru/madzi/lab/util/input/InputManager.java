package ru.madzi.lab.util.input;

import java.awt.Component;

/**
 * Менеджер ввода.
 */
public class InputManager {

    public InputManager(Component component) {}

    public void clearAllMaps() {
        // TODO Auto-generated method stub
    }

}
/**
 * (c) BioInTech 2012.
 */
package com.bit.hedgehog.engine.input;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
The InputManager manages input of key and mouse events.
Events are mapped to GameActions.
*/
public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * An invisible cursor.
     */
    public static final Cursor INVISIBLE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
            Toolkit.getDefaultToolkit().getImage(""), new Point(0,0), "invisible");

    // mouse codes
    public static final int MOUSE_MOVE_LEFT = 0;
    public static final int MOUSE_MOVE_RIGHT = 1;
    public static final int MOUSE_MOVE_UP = 2;
    public static final int MOUSE_MOVE_DOWN = 3;
    public static final int MOUSE_WHEEL_UP = 4;
    public static final int MOUSE_WHEEL_DOWN = 5;
    public static final int MOUSE_BUTTON_1 = 6;
    public static final int MOUSE_BUTTON_2 = 7;
    public static final int MOUSE_BUTTON_3 = 8;

    private static final int NUM_MOUSE_CODES = 9;

    // key codes are defined in java.awt.KeyEvent.
    // most of the codes (except for some rare ones like
    // "alt graph") are less than 600.
    private static final int NUM_KEY_CODES = 600;

    private GameAction[] keyActions = new GameAction[NUM_KEY_CODES];
    private GameAction[] mouseActions = new GameAction[NUM_MOUSE_CODES];

    private Point mouseLocation;
    private Point centerLocation;
    private Component comp;
    private Robot robot;
    private boolean isRecentering;

    /**
     * Creates a new InputManager that listens to input from the specified component.
     */
    public InputManager(Component comp) {
        this.comp = comp;
        mouseLocation = new Point();
        centerLocation = new Point();
        comp.addKeyListener(this);
        comp.addMouseListener(this);
        comp.addMouseMotionListener(this);
        comp.addMouseWheelListener(this);
        comp.setFocusTraversalKeysEnabled(false);
    }

    /**
     * Sets the cursor on this InputManager's input component.
     */
    public void setCursor(Cursor cursor) {
        comp.setCursor(cursor);
    }

    /**
     * Sets whether relative mouse mode is on or not. For relative mouse mode,
     * the mouse is "locked" in the center of the screen, and only the changed in mouse movement is measured.
     * In normal mode, the mouse is free to move about the screen.
     */
    public void setRelativeMouseMode(boolean mode) {
        if (mode == isRelativeMouseMode()) {
            return;
        }
        if (mode) {
            try {
                robot = new Robot();
                recenterMouse();
            }
            catch (AWTException ex) {
                // couldn't create robot!
                robot = null;
            }
        }
        else {
            robot = null;
        }
    }

    /**
     * Returns whether or not relative mouse mode is on.
     */
    public boolean isRelativeMouseMode() {
        return (robot != null);
    }

    /**
     * Maps a GameAction to a specific key. The key codes are defined in java.awt.KeyEvent. If the key already has a GameAction mapped to it, the new GameAction overwrites it.
     */
    public void mapToKey(GameAction gameAction, int keyCode) {
        keyActions[keyCode] = gameAction;
    }

    /**
     * Maps a GameAction to a specific mouse action. The mouse codes are defined here in InputManager 
     * (MOUSE_MOVE_LEFT, MOUSE_BUTTON_1, etc). 
     * If the mouse action already has a GameAction mapped to it, the new GameAction overwrites it.
     */
    public void mapToMouse(GameAction gameAction, int mouseCode) {
        mouseActions[mouseCode] = gameAction;
    }

    /**
     * Clears all mapped keys and mouse actions to this GameAction.
     */
    public void clearMap(GameAction gameAction) {
        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] == gameAction) {
                keyActions[i] = null;
            }
        }
        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] == gameAction) {
                mouseActions[i] = null;
            }
        }
        gameAction.reset();
    }

    /**
     * Gets a List of names of the keys and mouse actions mapped to this GameAction.
     * Each entry in the List is a String.
     */
    public List<String> getMaps(GameAction gameCode) {
        List<String> list = new ArrayList<String>();
        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] == gameCode) {
                list.add(getKeyName(i));
            }
        }
        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] == gameCode) {
                list.add(getMouseName(i));
            }
        }
        return list;
    }

    /**
     * Resets all GameActions so they appear like they haven't been pressed.
     */
    public void resetAllGameActions() {
        for (int i=0; i<keyActions.length; i++) {
            if (keyActions[i] != null) {
                keyActions[i].reset();
            }
        }
        for (int i=0; i<mouseActions.length; i++) {
            if (mouseActions[i] != null) {
                mouseActions[i].reset();
            }
        }
    }

    /**
     * Gets the name of a key code.
     */
    public static String getKeyName(int keyCode) {
        return KeyEvent.getKeyText(keyCode);
    }

    /**
     * Gets the name of a mouse code.
     */
    public static String getMouseName(int mouseCode) {
        switch (mouseCode) {
            case MOUSE_MOVE_LEFT: return "Mouse Left";
            case MOUSE_MOVE_RIGHT: return "Mouse Right";
            case MOUSE_MOVE_UP: return "Mouse Up";
            case MOUSE_MOVE_DOWN: return "Mouse Down";
            case MOUSE_WHEEL_UP: return "Mouse Wheel Up";
            case MOUSE_WHEEL_DOWN: return "Mouse Wheel Down";
            case MOUSE_BUTTON_1: return "Mouse Button 1";
            case MOUSE_BUTTON_2: return "Mouse Button 2";
            case MOUSE_BUTTON_3: return "Mouse Button 3";
            default: return "Unknown mouse code " + mouseCode;
        }
    }

    /**
     * Gets the x position of the mouse.
     */
    public int getMouseX() {
        return mouseLocation.x;
    }

    /**
     * Gets the y position of the mouse.
     */
    public int getMouseY() {
        return mouseLocation.y;
    }

    /**
     * Uses the Robot class to try to position the mouse in the center of the screen.
     * <p>Note that use of the Robot class may not be available on all platforms.
     */
    private synchronized void recenterMouse() {
        if (robot != null && comp.isShowing()) {
            centerLocation.x = comp.getWidth() / 2;
            centerLocation.y = comp.getHeight() / 2;
            SwingUtilities.convertPointToScreen(centerLocation,
                comp);
            isRecentering = true;
            robot.mouseMove(centerLocation.x, centerLocation.y);
        }
    }

    private GameAction getKeyAction(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keyActions.length) {
            return keyActions[keyCode];
        }
        else {
            return null;
        }
    }

    /**
     * Gets the mouse code for the button specified in this MouseEvent.
     */
    public static int getMouseButtonCode(MouseEvent e) {
         switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                return MOUSE_BUTTON_1;
            case MouseEvent.BUTTON2:
                return MOUSE_BUTTON_2;
            case MouseEvent.BUTTON3:
                return MOUSE_BUTTON_3;
            default:
                return -1;
        }
    }

    private GameAction getMouseButtonAction(MouseEvent e) {
        int mouseCode = getMouseButtonCode(e);
        if (mouseCode != -1) {
             return mouseActions[mouseCode];
        }
        else {
             return null;
        }
    }

    // from the KeyListener interface
    public void keyPressed(KeyEvent e) {
        GameAction gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.press();
        }
        // make sure the key isn't processed for anything else
        e.consume();
    }

    // from the KeyListener interface
    public void keyReleased(KeyEvent e) {
        GameAction gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
        // make sure the key isn't processed for anything else
        e.consume();
    }

    // from the KeyListener interface
    public void keyTyped(KeyEvent e) {
        // make sure the key isn't processed for anything else
        e.consume();
    }

    // from the MouseListener interface
    public void mousePressed(MouseEvent e) {
        GameAction gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.press();
        }
    }

    // from the MouseListener interface
    public void mouseReleased(MouseEvent e) {
        GameAction gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
    }

    // from the MouseListener interface
    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    // from the MouseListener interface
    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    // from the MouseListener interface
    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }

    // from the MouseMotionListener interface
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    // from the MouseMotionListener interface
    public synchronized void mouseMoved(MouseEvent e) {
        // this event is from re-centering the mouse - ignore it
        if (isRecentering && centerLocation.x == e.getX() && centerLocation.y == e.getY()) {
            isRecentering = false;
        }
        else {
            int dx = e.getX() - mouseLocation.x;
            int dy = e.getY() - mouseLocation.y;
            mouseHelper(MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, dx);
            mouseHelper(MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, dy);
            if (isRelativeMouseMode()) {
                recenterMouse();
            }
        }
        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();
    }

    // from the MouseWheelListener interface
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseHelper(MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN, e.getWheelRotation());
    }

    private void mouseHelper(int codeNeg, int codePos, int amount) {
        GameAction gameAction;
        if (amount < 0) {
            gameAction = mouseActions[codeNeg];
        }
        else {
            gameAction = mouseActions[codePos];
        }
        if (gameAction != null) {
            gameAction.press(Math.abs(amount));
            gameAction.release();
        }
    }

}

package com.e2.games.tools.input;

import javax.swing.*;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.*;
import java.util.*;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private static final Toolkit tools = Toolkit.getDefaultToolkit();

    public static final Cursor INVISIBLE_CURSOR = tools.createCustomCursor(tools.getImage(""), new Point(0, 0), "invisible");

    private Map<MouseCode, GameAction> mouseActions = new HashMap<MouseCode, GameAction>();
    private Map<Integer, GameAction> keyActions = new HashMap<Integer, GameAction>();

    private Point mouseLocation;
    private Point centerLocation;
    private Component comp;
    private Robot robot;
    private boolean isRecentering;

    public InputManager(Component comp) {
        this.comp = comp;
        mouseLocation = new Point();
        centerLocation = new Point();
        comp.addKeyListener(this);
        comp.addMouseListener(this);
        comp.addMouseMotionListener(this);
        comp.addMouseWheelListener(this);
        comp.setFocusTraversalKeysEnabled(false);
    }

    public void setCursor(Cursor cursor) {
        comp.setCursor(cursor);
    }

    public void setRelativeMouseMode(boolean mode) {
        if (mode == isRelativeMouseMode()) {
            return;
        }
        if (mode) {
            try {
                robot = new Robot();
                recenterMouse();
            } catch (AWTException ex) {
                robot = null;
            }
        } else {
            robot = null;
        }
    }

    public boolean isRelativeMouseMode() {
        return (robot != null);
    }

    public void mapToKey(GameAction gameAction, int keyCode) {
        keyActions.put(Integer.valueOf(keyCode), gameAction);
    }

    public void mapToMouse(GameAction gameAction, MouseCode mouseCode) {
        mouseActions.put(mouseCode, gameAction);
    }

    public void clearMap(GameAction gameAction) {
        Iterator<Map.Entry<Integer, GameAction>> keyItr = keyActions.entrySet().iterator();
        while (keyItr.hasNext()) {
            Map.Entry<Integer, GameAction> entry = keyItr.next();
            if (entry.getValue() == gameAction) {
                keyItr.remove();
            }
        }
        Iterator<Map.Entry<MouseCode, GameAction>> mouseItr = mouseActions.entrySet().iterator();
        while (mouseItr.hasNext()) {
            Map.Entry<MouseCode, GameAction> entry = mouseItr.next();
            if (entry.getValue() == gameAction) {
                mouseItr.remove();
            }
        }
        gameAction.reset();
    }

    public void clearAllMaps() {
        keyActions = new HashMap<Integer, GameAction>();
        mouseActions = new HashMap<MouseCode, GameAction>();
    }

    public List<String> getMaps(GameAction gameAction) {
        List<String> maps = new ArrayList<String>();
        //TODO: add key codes
        for (Map.Entry<MouseCode, GameAction> entry : mouseActions.entrySet()) {
            if (entry.getValue() == gameAction) {
                maps.add(entry.getKey().getName());
            }
        }
        return maps;
    }

    public void resetAllGameActions() {
        for (GameAction gameAction : keyActions.values()) {
            gameAction.reset();
        }
        for (GameAction gameAction : mouseActions.values()) {
            gameAction.reset();
        }
    }
    public static String getKeyName(int keyCode) {
        return KeyEvent.getKeyText(keyCode);
    }
    public static String getMouseName(MouseCode mouseCode) {
        return mouseCode.getName();
    }

    public int getMouseX() {
        return mouseLocation.x;
    }

    public int getMouseY() {
        return mouseLocation.y;
    }

    private synchronized void recenterMouse() {
        if (robot != null && comp.isShowing()) {
            centerLocation.x = comp.getWidth() / 2;
            centerLocation.y = comp.getHeight() / 2;
            SwingUtilities.convertPointToScreen(centerLocation, comp);
            isRecentering = true;
            robot.mouseMove(centerLocation.x, centerLocation.y);
        }
    }

    private GameAction getKeyAction(KeyEvent e) {
        return keyActions.get(Integer.valueOf(e.getKeyCode()));
    }

    public static MouseCode getMouseButtonCode(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1 :
                return MouseCode.BUTTON_1;
            case MouseEvent.BUTTON2 :
                return MouseCode.BUTTON_2;
            case MouseEvent.BUTTON3 :
                return MouseCode.BUTTON_3;
            default :
                return null;
        }
    }

    private GameAction getMouseButtonAction(MouseEvent e) {
        return mouseActions.get(getMouseButtonCode(e));
    }

    public void keyPressed(KeyEvent e) {
        GameAction gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.press();
        }
        e.consume();
    }

    public void keyReleased(KeyEvent e) {
        GameAction gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
        e.consume();
    }

    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    public void mousePressed(MouseEvent e) {
        GameAction gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.press();
        }
    }

    public void mouseReleased(MouseEvent e) {
        GameAction gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public synchronized void mouseMoved(MouseEvent e) {
        if (isRecentering && centerLocation.x == e.getX() && centerLocation.y == e.getY()) {
            isRecentering = false;
        } else {
            int dx = e.getX() - mouseLocation.x;
            int dy = e.getY() - mouseLocation.y;
            mouseHelper()
        }
    }



    public enum MouseCode {

        MOVE_LEFT(0, "Mouse Left"),
        MOVE_RIGHT(1, "Mouse Right"),
        MOVE_UP(2, "Mouse Up"),
        MOVE_DOWN(3, "Mouse Down"),
        WHEEL_UP(4, "Mouse Wheel Up"),
        WHEEL_DOWN(5, "Mouse Wheel Down"),
        BUTTON_1(6, "Mouse Button 1"),
        BUTTON_2(7, "Mouse Button 2"),
        BUTTON_3(8, "Mouse Button 3");

        private int code;
        private String name;

        MouseCode (int code, String name) {
            this.code = code;
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

    }
}
