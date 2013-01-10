package ru.madzi.lab.util.stages;

import java.awt.Graphics2D;

import ru.madzi.lab.util.input.InputManager;

/**
 * Интерфейс для работы со сценой.
 */
public interface Stage {

    /**
     * Возвращает название сцены.
     * 
     * @return название
     */
    public String getName();

    /**
     * Возвращает название следующей сцены или <b>null</b>, если не требуется менять сцену.
     * 
     * @return название новой сцены или <b>null</b>
     */
    public String checkForStageChange();

    /**
     * Загрузка ресурсов сцены.
     * 
     * @param resourceManager менеджер ресурсов
     */
    public void loadResources(ResourceManager resourceManager);

    /**
     * Запускает сцену для отображения. Инициализирующие действия.
     * 
     * @param inputManager менеджер ввода
     */
    public void start(InputManager inputManager);

    /**
     * Останавливает выполнение сцены. Завершающие действия.
     */
    public void stop();

    /**
     * Обновляет состояние сцены.
     * 
     * @param elapsedTime время, прошедшее с прошлого обновления
     */
    public void update(long elapsedTime);

    /**
     * Отрисовывает сцену.
     * 
     * @param g холст для отрисовки
     */
    public void draw(Graphics2D g);

}
