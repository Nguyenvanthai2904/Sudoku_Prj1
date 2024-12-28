package com.example.sudoku_prj1;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Start.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 650);
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

    public void onStartButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader recordLoader = new FXMLLoader(Main.class.getResource("Record.fxml"));
            recordLoader.load();
            Record recordController = recordLoader.getController();

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Sudoku.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 650, 650);
            Stage stage = new Stage();
            stage.setTitle("Sudoku");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onGuideButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Guide.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 650, 650);
            Stage stage = new Stage();
            stage.setTitle("Hướng dẫn");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onRecordButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Record.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 650, 650);
            Stage stage = new Stage();
            stage.setTitle("Kỷ lục");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
