package com.example.sudoku_prj1;

import java.util.Arrays;

public class Sudoku {
    private int[][] board;

    public Sudoku() {
        board = new int[9][9];
    }

//    public Sudoku(int[][] board) {
//        this.board = new int[9][9];
//        for (int i = 0; i < 9; i++) {
//            System.arraycopy(board[i], 0, this.board[i], 0, 9);
//        }
//    }
//
//    public void newGame(int[][] generatedBoard) {
//        for (int i = 0; i < 9; i++) {
//            System.arraycopy(generatedBoard[i], 0, this.board[i], 0, 9);
//        }
//    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isValid(int row, int col, int num) {
        // Kiểm tra hàng và cột
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        // Kiểm tra ô 3x3
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

    private boolean isValidPlacement(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (i != col && board[row][i] == num) return false;
            if (i != row && board[i][col] == num) return false;
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if ((i != row || j != col) && board[i][j] == num) return false;
            }
        }
        return true;
    }

    public boolean solve() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(row, col, num)) {
                            board[row][col] = num;

                            if (solve()) {
                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isComplete() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) return false;
            }
        }
        return true;
    }

    public boolean checkResult() {
        if (!isComplete()) return false;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int num = board[row][col];
                if (!isValidPlacement(row, col, num) || num == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
