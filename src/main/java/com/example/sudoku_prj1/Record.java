package com.example.sudoku_prj1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Record {
    @FXML
    private ImageView img_back;

    @FXML
    private Label lb_easy;

    @FXML
    private Label lb_medium;

    @FXML
    private Label lb_hard;

    private Map<String, String> records;

    public void initialize() {
        records = new HashMap<>();
        loadRecords();
        updateLabels();
    }

    @FXML
    public void onBackImgClick(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Start.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 650, 650);
            Stage stage = new Stage();
            stage.setTitle("Sudoku");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRecord(String difficulty, String time) {
        String existingRecord = records.getOrDefault(difficulty, "99:99");
        if (compareTime(time, existingRecord) < 0) {
            records.put(difficulty, time);
            updateLabels();
            saveRecords();
        }
    }

    private void updateLabels() {
        lb_easy.setText("Easy: " + records.getOrDefault("Easy", "00:00"));
        lb_medium.setText("Medium: " + records.getOrDefault("Medium", "00:00"));
        lb_hard.setText("Hard: " + records.getOrDefault("Hard", "00:00"));
    }

    private int compareTime(String time1, String time2) {
        String[] parts1 = time1.split(":");
        String[] parts2 = time2.split(":");
        int minutes1 = Integer.parseInt(parts1[0]);
        int seconds1 = Integer.parseInt(parts1[1]);
        int minutes2 = Integer.parseInt(parts2[0]);
        int seconds2 = Integer.parseInt(parts2[1]);

        if (minutes1 != minutes2) {
            return minutes1 - minutes2;
        } else {
            return seconds1 - seconds2;
        }
    }

    private void loadRecords() {

        try (BufferedReader reader = new BufferedReader(new FileReader("records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                records.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveRecords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("records.txt"))) {
            for (Map.Entry<String, String> entry : records.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}