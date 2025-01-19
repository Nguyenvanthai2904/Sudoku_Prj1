package com.example.sudoku_prj1;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SudokuController {

    @FXML
    private GridPane gridPane;

    @FXML
    private ComboBox<String> cb_difficulty;

    @FXML
    private Label lb_time;

    private final Map<String, TextField> textFieldMap = new HashMap<>();
    private Sudoku sudoku;
    private int[][] initialBoard;
    private AnimationTimer time;
    private long startTime;
    private boolean timeRun;
    private Record recordController;

    @FXML
    public void initialize() {
        cb_difficulty.setValue("Easy");
        cb_difficulty.getItems().addAll("Easy", "Medium", "Hard");

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String id = "tf" + row + col;
                TextField textField = (TextField) gridPane.lookup("#" + id);
                if (textField != null) {
                    textFieldMap.put(id, textField);
                }
            }
        }

        initTimer();
        startNewGame();
        cb_difficulty.setOnAction(this::onDifficultySelected);
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Record.fxml"));
            loader.load(); //khởi tạo một instance của controller class
            recordController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTimer() {
        time = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (timeRun) {
                    long timeCurrent = (now - startTime) / 1_000_000_000;
                    long minutes = timeCurrent / 60;
                    long seconds = timeCurrent % 60;
                    lb_time.setText(String.format("%02d:%02d", minutes, seconds));
                }
            }
        };
    }

    private void onDifficultySelected(ActionEvent actionEvent) {
        startNewGame();
    }

    private void startTime() {
        startTime = System.nanoTime();
        timeRun = true;
        time.start();
    }

    private void stopTime() {
        if (time != null) {
            timeRun = false;
            time.stop();
        }
    }

    private void resetTime() {
        stopTime();
        lb_time.setText("00:00");
    }

    public void continueTime(long minutes_pause, long seconds_pause) {
        time = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (timeRun) {
                    long timeCurrent = (now - startTime) / 1_000_000_000;
                    long minutes = minutes_pause + timeCurrent / 60;
                    long seconds = seconds_pause + timeCurrent % 60;
                    lb_time.setText(String.format("%02d:%02d", minutes, seconds));
                }
            }
        };
        startTime();
    }

    public void onBackImgClick(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Start.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 650, 650);
            Stage stage = new Stage();
            stage.setTitle("Sudoku");
            stage.setScene(scene);
            stage.setResizable(false);
            Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            currentStage.close();
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onCheckImgClick(MouseEvent mouseEvent) {
        loadInputToBoard();
        if (sudoku.checkResult()) {
            stopTime();
            String timeResult = lb_time.getText();

            // Kiểm tra và cập nhật kỷ lục trước khi hiển thị thông báo
            boolean isNewRecord = false;
            if (recordController != null) {
                String difficulty = cb_difficulty.getValue();
                isNewRecord = recordController.updateRecord(difficulty, timeResult); // Hàm trả về true nếu là kỷ lục mới
            }

            String message = "Sudoku is correct! Your time: " + timeResult;
            if (isNewRecord) {
                message += "\nNew Record!"; // Thêm thông báo kỷ lục mới
            }
            showAlert("Congratulations", message);

        } else {
            stopTime();
            showAlert("Hmm!!!", "Sudoku is incorrect, try again.");
            String[] tmp = lb_time.getText().split(":");
            long minutes_pause = Integer.parseInt(tmp[0]); // Lấy  phút
            long seconds_pause = Integer.parseInt(tmp[1]); // Lấy  giây
            continueTime(minutes_pause, seconds_pause);
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
                    } catch (NumberFormatException e) {
                        board[row][col] = 0;
                    }
                }
            }
        }
    }
    public void onSolveImgClick(MouseEvent mouseEvent) {
        sudoku.solve();
        stopTime();
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

        for (int i = 0; i < 9; i++) {
            System.arraycopy(initialBoard[i], 0, sudoku.getBoard()[i], 0, 9);
        }

        updateTextFieldsFromBoard();
        resetTime();
        startTime();
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

                if (initialBoard[row][col] != 0) {
                    textField.setText(String.valueOf(board[row][col]));
                    textField.setEditable(false);
                    textField.setStyle("-fx-background-color: #EEEEEE; -fx-text-fill: #0288D1;"); // Nền xám nhạt, chữ xanh cho ô cố định
                } else {
                    textField.setText(board[row][col] == 0 ? "" : String.valueOf(board[row][col]));
                    textField.setEditable(true);

                    // listener để lắng nghe thay đổi giá trị
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.matches("[1-9]")) {
                            textField.setText(oldValue); // nếu không khớp, giữ nguyên giá trị cũ
                        } if (newValue.length() > 1) {
                            textField.setText(newValue.substring(newValue.length() - 1));  //chỉ giữ 1 ký tự
                        } else {
                            highlightSameValueCells(textField); // nếu là số hợp lệ, tô sáng
                        }
                    });

                    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        highlightSameValueCells(textField); // gọi highlight sau khi nhập hoặc thay đổi focus
                    });
                }
            }
        }
    }

    private void highlightSameValueCells(TextField clickedField) {
        String highlightStyle = "-fx-background-color: rgba(255,235,59,1); -fx-text-fill: black;";

        // reset tất cả highlight trước
        resetAllHighlights();

        // lấy giá trị trong ô hiện tại
        String clickedValue = clickedField.getText().trim();

        //  highlight nếu ô đang được focus và có giá trị
        if (clickedField.isFocused() && !clickedValue.isEmpty()) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String id = "tf" + row + col;
                    TextField textField = textFieldMap.get(id);
                    if (clickedValue.equals(textField.getText().trim())) {
                        textField.setStyle(highlightStyle);
                    }
                }
            }
        }
    }

    private void resetAllHighlights() {
        String defaultStyle = "-fx-text-fill: black;"; // màu chữ đen cho ô không cố định
        String fixedStyle = "-fx-background-color: #EEEEEE; -fx-text-fill: #0288D1;"; // style cho ô cố định

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String id = "tf" + row + col;
                TextField textField = textFieldMap.get(id);
                if (textField != null) {
                    if (initialBoard[row][col] == 0) {
                        textField.setStyle(defaultStyle); // reset style cho ô không cố định
                    } else {
                        textField.setStyle(fixedStyle); // giữ style cho ô cố định
                    }
                }
            }
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Stage overlayStage = new Stage(); //tạo stage mới
        overlayStage.initOwner(gridPane.getScene().getWindow()); //lay cửa sổ chứa gridpane, đặt làm cửa sổ cha
        overlayStage.initModality(Modality.APPLICATION_MODAL); //khi overlay hien thi, người dùng không tương tác được
        overlayStage.initStyle(StageStyle.TRANSPARENT); //loại ổ vien va thanh tieu de cua cua so

        StackPane overlayPane = new StackPane(); //chứa rectangle
        Rectangle overlay = new Rectangle(gridPane.getWidth(), gridPane.getHeight());
        overlay.setFill(Color.rgb(255, 235, 59, 1)); //  đặt màu cho Rectangle
        overlayPane.getChildren().add(overlay);

        Scene overlayScene = new Scene(overlayPane);
        overlayStage.setScene(overlayScene);

        Bounds gridPaneBounds = gridPane.localToScreen(gridPane.getBoundsInLocal()); //lấy tọa độ, kích thuoc cua gridpane
        overlayStage.setWidth(gridPane.getWidth());
        overlayStage.setHeight(gridPane.getHeight());
        overlayStage.setX(gridPaneBounds.getMinX());
        overlayStage.setY(gridPaneBounds.getMinY());

        overlayStage.show();
        alert.showAndWait();

        // Đóng overlay Stage sau khi đóng Alert
        overlayStage.close();
    }
}