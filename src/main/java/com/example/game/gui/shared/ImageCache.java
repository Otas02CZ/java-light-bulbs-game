/**
 * IJA PROJECT: Light Bulbs Game
 * FILE: ImageCache.java
 * AUTHOR: Otakar Kočí xkocio00@stud.fit.vutbr.cz
 * CREATED: 04/2025
 * DESCRIPTION: ImageCache for storing already loaded images
 * of game cells.
 */

package com.example.game.gui.shared;

import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 * Provides access to images by loading the from given path.
 * Each image is cached and when it is requested again it is returned immediately.
 * Singleton
 */
public class ImageCache {
    /**
     * singleton instance of ImageCache
     */
    private static ImageCache instance = null;

    /**
     * hash map of cahed images
     */
    private HashMap<String, Image> images;

    /**
     * Logger instance for this class
     */
    private Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Returns singleton instance of ImageCache
     * @return ImageCache instance
     */
    public static ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    /**
     * Private constructor
     */
    private ImageCache() {
        images = new HashMap<>();
    }

    /**
     * Obtains image from given path or cache if already used at least once
     * @param path to the image
     * @return Image or null on error
     */
    public Image getImage(String path) {
        // attempt to use cache
        Image image = images.get(path);
        if (image != null) {
            return image;
        }

        // obtain the path
        URL imageUrl = getClass().getResource(path);
        if (imageUrl == null) {
            logger.log(Level.WARNING, "Image not found: " + path);
            return null;
        }
        // get the image
        logger.log(Level.INFO, "Image loaded: " + path);
        image = new Image(imageUrl.toExternalForm());
        images.put(path, image);
        return image;
    }
}
