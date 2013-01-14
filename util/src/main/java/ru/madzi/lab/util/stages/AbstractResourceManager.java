package ru.madzi.lab.util.stages;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 * Менеджер ресурсов.
 */
public abstract class AbstractResourceManager {

    private static final Logger _LOG = Logger.getLogger(AbstractResourceManager.class.getName());

    protected GraphicsConfiguration gc;

    /**
     * Инициализатор класса.
     * 
     * @param gc графическая конфигурация
     */
    protected void init(GraphicsConfiguration gc) {
        this.gc = gc;
        try {
            Enumeration<URL> enumeration = getClass().getClassLoader().getResources(AbstractResourceManager.class.getName());
            while (enumeration.hasMoreElements()) {
                _LOG.info(enumeration.nextElement().toString());
            }
        } catch (IOException e) {
            _LOG.log(Level.WARNING, "Exception: {0}", e.getMessage());
        }
    }

    /**
     * Загружает изображение из ресурса.
     * 
     * @param name название ресурса
     * @return изображение
     */
    public Image loadImage(String name) {
        return new ImageIcon(getResource(name)).getImage();
    }

    /**
     * Возвращает изображение отражённое по вертикали.
     * 
     * @param image исходное изображение
     * @return отражённое изображение
     */
    public Image getMirrorImage(Image image) {
        return getScaledImage(image, -1, 1);
    }

    /**
     * Возвращает изображение отражённое по горизонтали.
     * 
     * @param image исходное изображение
     * @return отражённое изображение
     */
    public Image getFlippedImage(Image image) {
        return getScaledImage(image, 1, -1);
    }

    /**
     * Возвращает URL для ресурса.
     *  
     * @param filename ресурс
     * @return URL
     */
    public abstract URL getResource(String filename);

    /**
     * Возвращает входной поток для ресурса.
     * 
     * @param filename ресурс
     * @return входной поток
     */
    public abstract InputStream getResourceAsStream(String filename);

    /**
     * Возвращает масштабированное изображение.
     * 
     * @param image исходное изображение
     * @param x масштаб по абсциссе
     * @param y масштаб по ординате
     * @return масштабированное изображение
     */
    private Image getScaledImage(Image image, float x, float y) {
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate((x - 1) * image.getWidth(null) / 2, (y - 1) * image.getHeight(null) / 2);
        Image newImage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.BITMASK);
        Graphics2D g = (Graphics2D) newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();
        return newImage;
    }

}
