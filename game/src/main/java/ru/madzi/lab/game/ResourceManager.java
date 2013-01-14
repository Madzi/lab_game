package ru.madzi.lab.game;

import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import javax.swing.ImageIcon;
import ru.madzi.lab.util.stages.AbstractResourceManager;

/**
 * Менеджер ресурсов.
 */
public class ResourceManager extends AbstractResourceManager {

    public ResourceManager(GraphicsConfiguration gc) {
        init(gc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image loadImage(String name) {
        return new ImageIcon(name).getImage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URL getResource(String filename) {
        return getClass().getClassLoader().getResource(filename);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getResourceAsStream(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

}
