package com.example.sudoku_prj1;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SudokuController {
    public void sudoku(){}

    public void onBackImgClick(MouseEvent mouseEvent){
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

    public void onCheckImgClick(MouseEvent mouseEvent){

    }

    public void onSolveImgClick(MouseEvent mouseEvent){

    }

    public void onNewGameImgClick(MouseEvent mouseEvent){}

}
