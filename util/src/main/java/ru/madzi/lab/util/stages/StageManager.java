package ru.madzi.lab.util.stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ru.madzi.lab.util.input.InputManager;

/**
 * Менеджер по работе со сценами.
 */
public class StageManager {

    /** Название псевдо-сцены для обозначения выхода */
    public static final String EXIT_GAME = "_exit";

    private Map<String, Stage> stages;

    private Image defaultImage;

    private Stage currentStage;

    private InputManager inputManager;

    private boolean done;

    /**
     * Конструктор, инициализирующий менеджер сцен.
     * 
     * @param inputManager менеджер ввода
     * @param defaultImage изображение для отсутствующих сцен
     */
    public StageManager(InputManager inputManager, Image defaultImage) {
        this.inputManager = inputManager;
        this.defaultImage = defaultImage;
        stages = new HashMap<String, Stage>();
    }

    /**
     * Добавляет сцену для управления ей.
     * 
     * @param stage сцена
     */
    public void addStage(Stage stage) {
        if (stage != null && !EXIT_GAME.equals(stage.getName())) {
            stages.put(stage.getName(), stage);
        }
    }

    /**
     * Возвращает доступные сцены.
     * 
     * @return список доступных сцен
     */
    public Collection<Stage> getStages() {
        return stages.values();
    }

    /**
     * Загружает ресурсы всех доступных сцен.
     * 
     * @param resourceManager менеджер ресурсов
     */
    public void loadAllResources(AbstractResourceManager resourceManager) {
        for (Stage stage : getStages()) {
            stage.loadResources(resourceManager);
        }
    }

    /**
     * Проверяет требуется ли выход из программы.
     * 
     * @return признак выхода
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Устанавливает текущую сцену для работы.
     * 
     * @param name
     */
    public void setStage(String name) {
        if (currentStage != null) {
            currentStage.stop();
        }
        inputManager.clearAllMaps();
        if (EXIT_GAME.equals(name)) {
            done = true;
        } else {
            currentStage = stages.get(name);
            if (currentStage != null) {
                currentStage.start(inputManager);
            }
        }
    }

    /**
     * Обновляет состояние сцены.
     * 
     * @param elapsedTime время, прошедшее с прошлого обновления
     */
    public void update(long elapsedTime) {
        if (currentStage == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        } else {
            String nextStage = currentStage.checkForStageChange();
            if (nextStage != null) {
                setStage(nextStage);
            } else {
                currentStage.update(elapsedTime);
            }
        }
    }

    /**
     * Отрисовывает сцену.
     * 
     * @param g холст для отрисовки
     */
    public void draw(Graphics2D g) {
        if (currentStage != null) {
            currentStage.draw(g);
        } else {
            if (defaultImage != null) {
                g.drawImage(defaultImage, 0, 0, null);
            }
        }
    }

}
