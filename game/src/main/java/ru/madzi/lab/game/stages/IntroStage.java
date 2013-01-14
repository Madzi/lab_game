package ru.madzi.lab.game.stages;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import ru.madzi.lab.util.input.Action;
import ru.madzi.lab.util.input.InputManager;
import ru.madzi.lab.util.stages.AbstractResourceManager;
import ru.madzi.lab.util.stages.Stage;

public class IntroStage implements Stage {

    private static final long INTRO_TIME = 5000;

    private long showTime;

    private boolean done;

    private Image background;

    private Action exitAction;

    private double fps;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "_intro";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String checkForStageChange() {
        return done ? "_exit" : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadResources(AbstractResourceManager resourceManager) {
        background = resourceManager.loadImage("/images/6backtest.png");
//        background = new ImageIcon("/images/6backtest.png").getImage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(InputManager inputManager) {
        showTime = 0;
        done = false;
        exitAction = new Action("exit");
        inputManager.mapToKey(exitAction, KeyEvent.VK_ESCAPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(long elapsedTime) {
        showTime += elapsedTime;
        if (exitAction.isPressed() || showTime > INTRO_TIME) {
            done = true;
        }
        fps = elapsedTime == 0 ? 1000 : 1000 / elapsedTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics2D g) {
        if (background != null) {
            g.drawImage(background, null, null);
        }
        g.drawString("INTRO", 10, 60);
        g.drawString(Double.toString(fps), 10, 160);
    }

}
