package com.example.game.gui.shared;

import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;

public class ImageCache {
    private static ImageCache instance = null;

    private HashMap<String, Image> images;

    private Logger logger = Logger.getLogger(getClass().getName());

    public static ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    private ImageCache() {
        images = new HashMap<>();
    }

    public Image getImage(String path) {
        Image image = images.get(path);
        if (image != null) {
            return image;
        }

        URL imageUrl = getClass().getResource(path);
        if (imageUrl == null) {
            logger.log(Level.WARNING, "Image not found: " + path);
            return null;
        }
        logger.log(Level.INFO, "Image loaded: " + path);
        image = new Image(imageUrl.toExternalForm());
        images.put(path, image);
        return image;
    }
}
