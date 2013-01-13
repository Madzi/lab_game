package ru.madzi.lab.game.stages;

import java.awt.Graphics2D;
import ru.madzi.lab.util.input.InputManager;
import ru.madzi.lab.util.stages.ResourceManager;
import ru.madzi.lab.util.stages.Stage;

public class IntroStage implements Stage {

    private static final long INTRO_TIME = 5000;

    private long showTime;

    private boolean done;

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return "_intro";
    }

    /**
     * {@inheritDoc}
     */
    public String checkForStageChange() {
        return done ? "_exit" : null;
    }

    /**
     * {@inheritDoc}
     */
    public void loadResources(ResourceManager resourceManager) {
    }

    /**
     * {@inheritDoc}
     */
    public void start(InputManager inputManager) {
        showTime = 0;
        done = false;
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
    }

    /**
     * {@inheritDoc}
     */
    public void update(long elapsedTime) {
        showTime += elapsedTime;
        if (showTime > INTRO_TIME) {
            done = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    public void draw(Graphics2D g) {
    }

}
