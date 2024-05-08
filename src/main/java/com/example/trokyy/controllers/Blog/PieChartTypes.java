package com.example.trokyy.controllers.Blog;

import com.example.trokyy.models.Blog;
import com.example.trokyy.services.BlogService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.application.Application.launch;

public class PieChartTypes extends Application {

    private static final Map<String, Double> descriptionLengthMap = new HashMap<>();

    @Override
    public void start(Stage stage) throws Exception {
        // Get data from BlogService
        BlogService service = new BlogService(); // Assuming you have a constructor
        List<Blog> blogs = service.getData();

        // Process data and populate descriptionLengthMap
        processDescriptionLength(blogs);

        PieChart pieChart = createPieChart(descriptionLengthMap);
        Scene scene = new Scene(pieChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Pie chart of blog description lengths (Percentage)");
        stage.show();
    }

    private void processDescriptionLength(List<Blog> blogs) {
        int totalBlogs = blogs.size();
        Map<String, Integer> lengthCountMap = new HashMap<>();
        for (Blog blog : blogs) {
            int descriptionLength = blog.getContenu().length();
            String lengthCategory = getCategoryFromLength(descriptionLength);
            lengthCountMap.putIfAbsent(lengthCategory, 0);
            lengthCountMap.put(lengthCategory, lengthCountMap.get(lengthCategory) + 1);
        }

        // Calculate percentages for each description length category
        for (Map.Entry<String, Integer> entry : lengthCountMap.entrySet()) {
            double percentage = ((double) entry.getValue() / totalBlogs) * 100;
            descriptionLengthMap.put(entry.getKey(), percentage);
        }
    }

    private String getCategoryFromLength(int length) {
        if (length < 10) {
            return "Short (< 10 characters)";
        } else if (length < 20) {
            return "Medium (10-20 characters)";
        } else {
            return "Long (> 20 characters)";
        }
    }

    private PieChart createPieChart(Map<String, Double> descriptionLengthMap) {
        PieChart pieChart = new PieChart();
        pieChart.setData(FXCollections.observableArrayList(
                descriptionLengthMap.entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey() + " (" + String.format("%.2f", entry.getValue()) + "%)", entry.getValue()))
                        .toList()
        ));
        return pieChart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}