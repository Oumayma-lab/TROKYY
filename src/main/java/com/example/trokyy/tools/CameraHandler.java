package com.example.trokyy.tools;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

public class CameraHandler {

    private static final String IMAGE_FILE_PATH = "C:/Users/amara/Downloads/captured_image.jpg";

    public static void captureAndSaveImage() {
        // Open default camera
        VideoCapture camera = new VideoCapture(0);

        if (camera.isOpened()) {
            Mat frame = new Mat();
            camera.read(frame);
            org.bytedeco.opencv.global.opencv_imgcodecs.imwrite(IMAGE_FILE_PATH, frame);
            camera.release();
        } else {
            System.err.println("Failed to open camera!");
        }
    }

    public static String captureImage() {
        // Open default camera
        VideoCapture camera = new VideoCapture(0);

        if (camera.isOpened()) {
            Mat frame = new Mat();
            camera.read(frame);
            String filePath = IMAGE_FILE_PATH;
            org.bytedeco.opencv.global.opencv_imgcodecs.imwrite(filePath, frame);
            camera.release();
            return filePath;
        } else {
            System.err.println("Failed to open camera!");
            return null;
        }
    }
}
