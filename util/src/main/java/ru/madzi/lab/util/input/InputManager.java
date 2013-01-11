package ru.madzi.lab.util.input;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;

/**
 * Менеджер ввода.
 */
public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private static final Toolkit tools = Toolkit.getDefaultToolkit();

    public static final Cursor INVISIBLE_CURSOR = tools.createCustomCursor(tools.getImage(""), new Point(0, 0), "invisible");

    private Map<MouseCode, Action> mouseActions = new HashMap<MouseCode, Action>();

    private Map<Integer, Action> keyActions = new HashMap<Integer, Action>();

    private Point mouseLocation;

    private Point centerLocation;

    private Component comp;

    private Robot robot;

    private boolean isRecentering;

    public InputManager(Component component) {
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

    public void mapToKey(Action action, int keyCode) {
        keyActions.put(Integer.valueOf(keyCode), action);
    }

    public void mapToMouse(Action action, MouseCode mouseCode) {
        mouseActions.put(mouseCode, action);
    }

    public void clearMap(Action action) {
        Iterator<Map.Entry<Integer, Action>> keyItr = keyActions.entrySet().iterator();
        while (keyItr.hasNext()) {
            Map.Entry<Integer, Action> entry = keyItr.next();
            if (entry.getValue() == action) {
                keyItr.remove();
            }
        }
        Iterator<Map.Entry<MouseCode, Action>> mouseItr = mouseActions.entrySet().iterator();
        while (mouseItr.hasNext()) {
            Map.Entry<MouseCode, Action> entry = mouseItr.next();
            if (entry.getValue() == action) {
                mouseItr.remove();
            }
        }
        action.reset();
    }

    public void clearAllMaps() {
        keyActions = new HashMap<Integer, Action>();
        mouseActions = new HashMap<MouseCode, Action>();
    }

    public List<String> getMaps(Action action) {
        List<String> maps = new ArrayList<String>();
        //TODO: add key codes
        for (Map.Entry<MouseCode, Action> entry : mouseActions.entrySet()) {
            if (entry.getValue() == action) {
                maps.add(entry.getKey().getName());
            }
        }
        return maps;
    }

    public void resetAllGameActions() {
        for (Action gameAction : keyActions.values()) {
            gameAction.reset();
        }
        for (Action gameAction : mouseActions.values()) {
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

    private Action getKeyAction(KeyEvent e) {
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

    private Action getMouseButtonAction(MouseEvent e) {
        return mouseActions.get(getMouseButtonCode(e));
    }

    public void keyPressed(KeyEvent e) {
        Action gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.press();
        }
        e.consume();
    }

    public void keyReleased(KeyEvent e) {
        Action gameAction = getKeyAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
        e.consume();
    }

    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    public void mousePressed(MouseEvent e) {
        Action gameAction = getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.press();
        }
    }

    public void mouseReleased(MouseEvent e) {
        Action gameAction = getMouseButtonAction(e);
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
            mouseHelper(MouseCode.MOVE_LEFT, MouseCode.MOVE_RIGHT, dx);
            mouseHelper(MouseCode.MOVE_UP, MouseCode.MOVE_DOWN, dy);
            if (isRelativeMouseMode()) {
                recenterMouse();
            }
        }
        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void mouseHelper(MouseCode codeNeg, MouseCode codePos, int amount) {
        Action action;
        if (amount < 0) {
            action = mouseActions.get(codeNeg);
        }
        else {
            action = mouseActions.get(codePos);
        }
        if (action != null) {
            action.press(Math.abs(amount));
            action.release();
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
