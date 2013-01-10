package ru.madzi.lab.util.input;

/**
 * Событие.
 */
public class Action {

    private String name;

    private int amount;

    private Behavior behavior;

    private State state;

    /**
     * Инициализация события.
     * 
     * @param name название
     */
    public Action(String name) {
        this(name, Behavior.NORMAL);
    }

    /**
     * Инициализация события.
     * 
     * @param name название
     * @param behavior поведение
     */
    public Action(String name, Behavior behavior) {
        this.name = name;
        this.behavior = behavior;
        reset();
    }

    /**
     * Получение названия события.
     * 
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Сброс параметров события.
     */
    public void reset() {
        state = State.RELEASED;
        amount = 0;
    }

    /**
     * Инициация события. Имитирует одноразовый вызов. И деинициация.
     */
    public synchronized void tap() {
        press();
        release();
    }

    /**
     * Одноразовая инициация события.
     */
    public synchronized void press() {
        press(1);
    }

    /**
     * Многократная инициация события.
     * 
     * @param amount количество инициаций
     */
    public synchronized void press(int amount) {
        if (state != State.WAITING_FOR_RELEASE) {
            this.amount += amount;
            state = State.PRESSED;
        }
    }

    /**
     * Деинициация события.
     */
    public synchronized void release() {
        state = State.RELEASED;
    }

    /**
     * Возвращает признак того, что событие было инициировано.
     * 
     * @return признак инициации
     */
    public synchronized boolean isPressed() {
        return (getAmount() != 0);
    }

    /**
     * Возвращает количество инициаций события.
     * 
     * @return число инициаций
     */
    public synchronized int getAmount () {
        int retVal = amount;
        if (retVal != 0) {
            if (state == State.RELEASED) {
                amount = 0;
            } else if (behavior == Behavior.DETECT_INITIAL_PRESS_ONLY) {
                state = State.WAITING_FOR_RELEASE;
                amount = 0;
            }
        }
        return retVal;
    }

    /***
     * Поведение события.
     */
    public enum Behavior {
        NORMAL, DETECT_INITIAL_PRESS_ONLY;
    }

    /**
     * Состояния события.
     */
    public enum State {
        RELEASED, PRESSED, WAITING_FOR_RELEASE;
    }

}
