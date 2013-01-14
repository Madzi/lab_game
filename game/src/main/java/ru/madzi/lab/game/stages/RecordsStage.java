package ru.madzi.lab.game.stages;

import java.awt.Graphics2D;
import java.util.logging.Logger;
import ru.madzi.lab.util.input.InputManager;
import ru.madzi.lab.util.stages.AbstractResourceManager;
import ru.madzi.lab.util.stages.Stage;

public class RecordsStage implements Stage {

    private static final Logger _LOG = Logger.getLogger(RecordsStage.class.getName());

    private boolean isResLoaded = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "_records";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String checkForStageChange() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadResources(AbstractResourceManager resourceManager) {
        if (!isResLoaded) {
            _LOG.info("Loading resources...");
            isResLoaded = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(InputManager inputManager) {
        _LOG.info("Start scene...");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        _LOG.info("Stop scene...");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(long elapsedTime) {
        _LOG.info("Update scene...");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics2D g) {
        _LOG.info("Draw scene...");
    }

}
