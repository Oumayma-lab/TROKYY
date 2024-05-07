package com.example.trokyy.tools;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_core.CV_32SC2;
import static org.bytedeco.opencv.global.opencv_core.CV_8UC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
public class ModelTrainer {

    public static FaceRecognizer trainModel(String datasetPath) {
        List<Mat> images = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();
        File datasetDir = new File(datasetPath);
        File[] userDirs = datasetDir.listFiles();

        if (userDirs != null) {
            for (File userDir : userDirs) {
                File[] userImages = userDir.listFiles();
                if (userImages != null) {
                    int label = Integer.parseInt(userDir.getName());
                    for (File imageFile : userImages) {
                        Mat image = imread(imageFile.getAbsolutePath(), IMREAD_GRAYSCALE);
                        images.add(image);
                        labels.add(label);
                    }
                }
            }
        }

        MatVector imagesVector = new MatVector(images.size());
        Mat labelsMat = new Mat(images.size(), 1, CV_32SC1);
        IntBuffer labelsBuffer = labelsMat.createBuffer();
        for (int i = 0; i < images.size(); i++) {
            imagesVector.put(i, images.get(i));
            labelsBuffer.put(i, labels.get(i));
        }

        LBPHFaceRecognizer recognizer = LBPHFaceRecognizer.create();
        recognizer.train(imagesVector, labelsMat);
        return recognizer;
    }
}
