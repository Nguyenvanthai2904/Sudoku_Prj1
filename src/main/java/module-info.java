module com.example.sudoku_prj1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.sudoku_prj1 to javafx.fxml;
    exports com.example.sudoku_prj1;
}