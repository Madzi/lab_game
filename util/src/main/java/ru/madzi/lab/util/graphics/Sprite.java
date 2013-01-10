package ru.madzi.lab.util.graphics;

import java.awt.Image;

/**
 * Спрайт.
 */
public class Sprite {

    private Animation anim;

    private float x;

    private float y;

    private float dx;

    private float dy;

    /**
     * Инициализация спрайта.
     * 
     * @param anim анимация
     */
    public Sprite(Animation anim) {
        this.anim = anim;
    }

    /**
     * Обновление спрайта.
     * 
     * @param elapsedTime время, прошедшее с момента предыдущего обновления
     */
    public void update(long elapsedTime) {
        x += dx * elapsedTime;
        y += dy * elapsedTime;
        anim.update(elapsedTime);
    }

    /**
     * Получение абсциссы.
     * 
     * @return абсцисса
     */
    public float getX() {
        return x;
    }

    /**
     * Установка абсциссы.
     * 
     * @param x абсцисса
     * @return текущий экземпляр объекта
     */
    public Sprite setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Получение ординаты.
     * 
     * @return ордината
     */
    public float getY() {
        return y;
    }

    /**
     * Установка ординаты.
     * 
     * @param y ордината
     * @return текущий экземпляр объекта
     */
    public Sprite setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * Установка координат спрайта.
     * 
     * @param x абсцисса
     * @param y ордината
     * @return текущий экземпляр объекта
     */
    public Sprite setXY(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Получение ширины спрайта.
     * 
     * @return ширина
     */
    public int getWidth() {
        Image image = anim.getImage();
        return image != null ? image.getWidth(null) : 0;
    }

    /**
     * Получение высоты спрайта.
     * 
     * @return высота
     */
    public int getHeight() {
        Image image = anim.getImage();
        return image != null ? image.getHeight(null) : 0;
    }

    /**
     * Устанавливает скорость движения по абсциссе.
     * 
     * @param dx проекция скорости движения на абсциссу
     * @return текущий экземпляр объекта
     */
    public Sprite setVelocityX(float dx) {
        this.dx = dx;
        return this;
    }

    /**
     * Устанавливает скорость движения по ординате.
     * 
     * @param dy проекция скорости движения на ординату
     * @return текущий экземпляр объекта
     */
    public Sprite setVelocityY(float dy) {
        this.dy = dy;
        return this;
    }

    /**
     * Устанавливает скорость движения.
     * 
     * @param dx проекция скорости движения на абсциссу
     * @param dy проекция скорости движения на ординату
     * @return текущий экземпляр объекта
     */
    public Sprite setVelocity(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
        return this;
    }

    /**
     * Возвращает изображение текущего кадра анимации.
     * 
     * @return изображение
     */
    public Image getImage() {
        return anim.getImage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        return new Sprite(anim);
    }

}
