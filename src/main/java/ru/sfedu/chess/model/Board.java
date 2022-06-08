package ru.sfedu.chess.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Board {
    private static final Logger log = LogManager.getLogger(Board.class);
    private Figure[][] chessBoard;

    public Board(int xDimension, int yDimension){
        chessBoard = new Figure[xDimension][yDimension];
    }

    /**
     * Checks that a spot on the board is empty and
     * is a movable spot
     * @param xPosition The x position of spot
     * @param yPosition The y position of spot
     * @return true if empty spot.
     */
    public boolean isEmptyPosition(int xPosition, int yPosition){
        if (isInBounds(xPosition, yPosition)){
            if (chessBoard[xPosition][yPosition] == null)
                return true;
        }
        return false;
    }

    /**
     * Checks if a location is in bounds of the current chess board
     * @param xPosition x position of target
     * @param yPosition y position of target
     * @return true if location is in bounds
     */
    public boolean isInBounds(int xPosition, int yPosition){
        if (xPosition < getXDimension() && xPosition >= 0 &&
                yPosition < getYDimension() && yPosition >= 0)
            return true;
        return false;
    }

    /**
     * Returns the chess figure located at the specified location
     * @param xPosition x position of target
     * @param yPosition y position of target
     * @return The chessfigure at given location, null otherwise
     */
    public Figure figureAt(int xPosition, int yPosition){
        if (isInBounds(xPosition, yPosition)){
            return chessBoard[xPosition][yPosition];
        }
        return null;
    }

    /**
     * Displays the current board
     *
     * THIS IS NOT COMPLETE. MERELY FOR PLAYING WITH GAME EARLY ON.
     */
    public void displayBoard(){
        for (int xBoard = 0; xBoard < getXDimension(); xBoard++){
            for (int yBoard = 0; yBoard < getYDimension(); yBoard++){
                switch (chessBoard[xBoard][yBoard].getFigureType()) {
                    case PAWN -> System.out.print("p");
                    case KNIGHT -> System.out.print("k");
                    case QUEEN -> System.out.print("q");
                    case KING -> System.out.print("K");
                    case ROOK -> System.out.print("r");
                    case BISHOP -> System.out.print("b");
                    default -> System.out.print(".");
                }
            }
            System.out.println();
        }
    }


    public int getXDimension(){
        return chessBoard[0].length;
    }

    public int getYDimension(){
        return chessBoard.length;
    }

    public Figure[][] getChessBoard(){
        return chessBoard;
    }

    public void removeFromBoard(Figure removeFigure){
        int oldXLocation = removeFigure.getXLocation();
        int oldYLocation = removeFigure.getYLocation();

        chessBoard[oldXLocation][oldYLocation] = null;
    }

    public void placeFigure(Figure chessFigure, int xPosition, int yPosition){
        if (isInBounds(xPosition, yPosition))
            chessBoard[xPosition][yPosition] = chessFigure;
    }
}
