package com.example.sudoku_prj1;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SudokuController {

    @FXML
    private GridPane gridPane;

    @FXML
    private ComboBox<String> cb_difficulty;

    private final Map<String, TextField> textFieldMap = new HashMap<>();
    private Sudoku sudoku;
    private int[][] initialBoard;


    @FXML
    public void initialize() {
        cb_difficulty.setValue("Easy");
        cb_difficulty.getItems().addAll("Easy", "Medium", "Hard");

        // Ánh xạ các TextField
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String id = "tf" + row + col;
                TextField textField = (TextField) gridPane.lookup("#" + id);
                if (textField != null) {
                    textFieldMap.put(id, textField);
                }
            }
        }
        startNewGame();
        cb_difficulty.setOnAction(this::onDifficultySelected);
    }

    private void onDifficultySelected(ActionEvent actionEvent){
        startNewGame();
    }

//    public TextField getTextField(int row, int col) {
//        return textFieldMap.get("tf" + row + col);
//    }


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

    public void onCheckImgClick(MouseEvent mouseEvent) {

        loadInputToBoard();
        if (sudoku.checkResult()) {
            showAlert("Congratulations", "Sudoku is correct!");
        } else {
            showAlert("Hmm!!!", "Sudoku is incorrect, try again.");
        }
    }

    private void loadInputToBoard() {
        int[][] board = sudoku.getBoard();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String id = "tf" + row + col;
                TextField textField = textFieldMap.get(id);
                String text = textField.getText().trim();
                if (text.isEmpty()) {
                    board[row][col] = 0;
                } else {
                    try {
                        board[row][col] = Integer.parseInt(text);
                    }catch (NumberFormatException e){
                        board[row][col] = 0;
                    }
                }
            }
        }
    }

    public void onSolveImgClick(MouseEvent mouseEvent) {
        sudoku.solve();
        updateTextFieldsFromBoard();
    }

    public void onNewGameImgClick(MouseEvent mouseEvent) {
        startNewGame();
    }


    private void startNewGame() {
        int difficulty = getDifficultyLevel();
        SudokuGenerator generator = new SudokuGenerator();
        initialBoard = generator.generate(difficulty);

        sudoku = new Sudoku();
        for(int i = 0; i < 9; i++){
            System.arraycopy(initialBoard[i], 0, sudoku.getBoard()[i], 0, 9);
        }
        updateTextFieldsFromBoard();
    }

    private int getDifficultyLevel() {
        String selectedDifficulty = cb_difficulty.getValue();
        switch (selectedDifficulty) {
            case "Medium":
                return 40;
            case "Hard":
                return 50;
            default:
                return 30;
        }
    }

    private void updateTextFieldsFromBoard() {
        int[][] board = sudoku.getBoard();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String id = "tf" + row + col;
                TextField textField = textFieldMap.get(id);
                if(initialBoard[row][col] != 0){
                    textField.setText(String.valueOf(board[row][col]));
                    textField.setEditable(false);
                }else{
                    textField.setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
                    textField.setEditable(true);
                }

            }
        }
    }
    private void showAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}