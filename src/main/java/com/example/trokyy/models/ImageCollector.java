package com.example.trokyy.models;

import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class ImageCollector {

    private Webcam webcam;
    private int imageCount = 0;

    public ImageCollector() {
        webcam = Webcam.getDefault();
        if (webcam != null) {
            webcam.setViewSize(WebcamResolution.VGA.getSize());
            webcam.open();
        } else {
            System.out.println("No webcam detected!");
        }
    }

    public Image captureImage() {
        return SwingFXUtils.toFXImage(webcam.getImage(), null);
    }

    public void saveImage() {
        Image image = captureImage();
        if (image != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}