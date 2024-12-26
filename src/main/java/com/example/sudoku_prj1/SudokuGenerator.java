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
        fillBoardRandom(0, 0);
        removeNumbers(difficulty);
        return board;
    }





    private boolean fillBoardRandom(int row, int col) {
        if (row == 9) {
            return true;
        }

        int nextRow = (col == 8) ? row + 1 : row;
        int nextCol = (col == 8) ? 0 : col + 1;

        if (board[row][col] != 0) {
            return fillBoardRandom(nextRow, nextCol);
        }

        int[] numbers = {1,2,3,4,5,6,7,8,9};
        shuffleArray(numbers);

        for(int num: numbers){
            if (isValid(row, col, num)) {
                board[row][col] = num;
                if (fillBoardRandom(nextRow, nextCol)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private boolean isValid(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }


    private void removeNumbers(int difficulty) {
        int remove = difficulty;
        while (remove > 0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (board[row][col] != 0) {
                board[row][col] = 0;
                remove--;
            }
        }
    }
}