package ru.madzi.lab.util.graphics;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Анимация спрайтов.
 */
public final class Animation {

    private List<AnimFrame> frames;

    private int currFrameIndex;

    private long animTime;

    private long totalDuration;

    /**
     * Создание новой анимации.
     */
    public Animation() {
        frames = new ArrayList<AnimFrame>();
        totalDuration = 0;
        start();
    }

    /**
     * Добавление спрайта к анимации.
     *
     * @param image спрайт
     * @param duration длительность отображения
     */
    public synchronized void addFrame(Image image, long duration) {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    /**
     * Сброс параметров анимации.
     */
    public synchronized void start() {
        animTime = 0;
        currFrameIndex = 0;
    }

    /**
     * Обновление анимации, выбор текущего кадра (если требуется).
     *
     * @param elapsedTime время между обновлениями
     */
    public synchronized void update(long elapsedTime) {
        if (frames.size() > 1) {
            animTime += elapsedTime;
            if (animTime >= totalDuration) {
                animTime %= totalDuration;
                currFrameIndex = 0;
            }
            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++;
            }
        }
    }

    /**
     * Выбор спрайта, соответствующего кадру анимации.
     *
     * @return спрайт
     */
    public synchronized Image getImage() {
        return frames.isEmpty() ? null : getFrame(currFrameIndex).image;
    }

    /**
     * Выбор кадра анимации.
     *
     * @param idx индекс кадра
     * @return кадр
     */
    private AnimFrame getFrame(int idx) {
        return frames.get(idx);
    }

    /**
     * Класс реализующий кадр анимации.
     */
    private class AnimFrame {

        Image image;

        long endTime;

        /**
         * Инициализация кадра анимации.
         *
         * @param image спрайт
         * @param endTime время отображения
         */
        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }

    }

}
