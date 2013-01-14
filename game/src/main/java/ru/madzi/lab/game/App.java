package ru.madzi.lab.game;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.madzi.lab.game.stages.GameStage;
import ru.madzi.lab.game.stages.IntroStage;
import ru.madzi.lab.game.stages.MenuStage;
import ru.madzi.lab.game.stages.RecordsStage;
import ru.madzi.lab.game.stages.SetupStage;
import ru.madzi.lab.game.stages.StatisticsStage;
import ru.madzi.lab.util.Core;
import ru.madzi.lab.util.input.InputManager;
import ru.madzi.lab.util.stages.StageManager;

/**
 * Точка входа в программу.
 */
public class App extends Core {

    private static final Logger _LOG = Logger.getLogger(App.class.getName());

    private InputManager inputManager;

    private StageManager stageManager;

    private ResourceManager resourceManager;

    private IntroStage intro;

    /**
     * Главная функция.
     *
     * @param args аргументы командной строки
     */
    public static void main( String[] args ) {
        _LOG.info("Start application");
        new App().run();
    }

    @Override
    public void init() {
        _LOG.setLevel(Level.INFO);
        _LOG.info("Init application");
        isFullscreen = false;
        displayMode = new DisplayMode(800, 600, DisplayMode.BIT_DEPTH_MULTI, DisplayMode.REFRESH_RATE_UNKNOWN);
        super.init();

        initManagers();

        initStages();

        stageManager.setStage(intro.getName());
    }

    private void initManagers() {
        _LOG.info("Init input manager");
        inputManager = new InputManager((Component) screenManager.getWindow());
        _LOG.info("Init resource manager");
        resourceManager = new ResourceManager(screenManager.getWindow().getGraphicsConfiguration());
        _LOG.info("Init stage manager");
        stageManager = new StageManager(inputManager, resourceManager.loadImage("/images/6backtest.png"));
    }

    private void initStages() {
        _LOG.info("Init stages...");
        intro = new IntroStage();
        stageManager.addStage(intro);
        stageManager.addStage(new MenuStage());
        stageManager.addStage(new SetupStage());
        stageManager.addStage(new GameStage());
        stageManager.addStage(new RecordsStage());
        stageManager.addStage(new StatisticsStage());
        stageManager.loadAllResources(resourceManager);
        stageManager.setStage(intro.getName());
    }

    @Override
    public void stop() {
        _LOG.info("Stop application");
        super.stop();
    }

    /**
     * Обновление состояния сцены.
     *
     * @param elapsedTime время, прошедшее с момента прошлого обновления
     */
    @Override
    public void update(long elapsedTime) {
        if (stageManager.isDone()) {
            stop();
        } else {
            stageManager.update(elapsedTime);
        }
    }

    /**
     * Отрисовка сцены на холсте.
     *
     * @param g холст
     */
    @Override
    public void draw(Graphics2D g) {
        stageManager.draw(g);
    }

}
