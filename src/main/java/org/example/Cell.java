package org.example;

public class Cell {
    public boolean isMine;
    public boolean isRevealed;
    boolean isFlagged;
    boolean hasNumbers;
    int mineCounter;

    Cell() {
        isMine = false;
        isRevealed = false;
        isFlagged = false;
        hasNumbers = false;
        mineCounter = 0;

    }
}