package com.example.game.gui.shared;

import java.net.URL;
import java.util.HashMap;
import javafx.scene.image.Image;

public class ImageCache {
    private HashMap<String, Image> images;

    public ImageCache() {
        images = new HashMap<>();
    }

    public Image getImage(String path) {
        Image image = images.get(path);
        if (image != null) {
            return image;
        }

        URL imageUrl = getClass().getResource(path);
        if (imageUrl == null) {
            System.out.println("Image not found: " + path);
            return null;
        }
        image = new Image(imageUrl.toExternalForm());
        images.put(path, image);
        return image;
    }
}
