package com.example.sudoku_prj1;

import java.util.Random;

public class SudokuGenerator {
    private int[][] board;
    private Random random;

    public SudokuGenerator() {
        board = new int[9][9];
        random = new Random();
    }

    public int[][] generate(int difficulty) {
        fillBoard();
        removeNumbers(difficulty);
        return board;
    }

    private void fillBoard() {
        Sudoku sudoku = new Sudoku();
        sudoku.solve();
        board = sudoku.getBoard();
    }

    private void removeNumbers(int difficulty) {
        int cellsToRemove = difficulty;
        while (cellsToRemove > 0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (board[row][col] != 0) {
                board[row][col] = 0;
                cellsToRemove--;
            }
        }
    }
}

