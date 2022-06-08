package ru.sfedu.chess.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Figure {
    private static final Logger log = LogManager.getLogger(Figure.class);
    public static final int BLACK = 0;
    public static final int WHITE = 1;
    FigureType figureType;
    private int xLocation;
    private int yLocation;
    private int color;
    protected boolean hasMoved;
    protected Board chessBoard;

    public Figure(Board board, int color){
        this.chessBoard = board;
        this.color = color;
        hasMoved = false;
        xLocation = -1;
        yLocation = -1;
    }

    public Figure(Board board, int color, int xLoc, int yLoc){
        this.chessBoard = board;
        this.color = color;
        this.hasMoved = false;
        this.xLocation = xLoc;
        this.yLocation = yLoc;

        chessBoard.placeFigure(this, xLoc, yLoc);
    }

    /**
     * Checks if a figure can move to a certain spot.
     * To move to a spot, the spot must be within bounds.
     * Also, the spot must either be empty or be occupied by
     * and enemy chess figure.
     * Finally, for specific figures, the move must be a valid
     * style of movement (depending on the chess figure).
     *
     * This method should be overwritten. Only
     * should be used in this form for the generic chess figure.
     *
     * @param xPosition The x location of where to move
     * @param yPosition The y location of where to move
     * @return true if move is possible
     */
    public boolean canMoveTo(int xPosition, int yPosition){
        return canMoveGenerics(xPosition, yPosition);
    }

    /**
     * Helper function that checks whether it is possible,
     * in the most generic sense, for a chess figure to move to a spot.
     *
     * Disregards the figure's move-style.
     * @param xPosition X location of move.
     * @param yPosition Y location of move.
     * @return returns true if move is possible.
     */
    protected boolean canMoveGenerics(int xPosition, int yPosition){
        if (chessBoard.isInBounds(xPosition, yPosition)){
            Figure location = chessBoard.figureAt(xPosition, yPosition);

            if (location == null) return true;
            if (location.getColor() != this.color) return true;
        }
        return false;
    }

    /**
     * Moves the current chess figure to given location
     * @param xPosition The x location of where to move.
     * @param yPosition The y location of where to move.
     */
    public void moveTo(int xPosition, int yPosition){
        if (chessBoard.figureAt(xLocation, yLocation) == this)
            chessBoard.removeFromBoard(this);
        this.xLocation = xPosition;
        this.yLocation = yPosition;

        Figure target = chessBoard.figureAt(xPosition, yPosition);
        if (target != null){
            this.captureFigure(target);
        }


        chessBoard.placeFigure(this, xPosition, yPosition);
        hasMoved = true;
    }

    /**
     * Removes the current chess figure off of the board
     */
    public void removeFigure() {
        chessBoard.removeFromBoard(this);
        xLocation = -1;
        yLocation = -1;
    }

    /**
     * Captures an enemy figure on the board.
     * @param capturedFigure the figure that is to be captured.
     */
    public void captureFigure(Figure capturedFigure){
        capturedFigure.removeFigure();
    }

    /**
     * Checks whether the current figure is on the chess board
     * @return true if is on the chess board.
     */
    public boolean onBoard(){
        if (chessBoard.isInBounds(xLocation, yLocation))
            return true;
        return false;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public int getXLocation(){
        return xLocation;
    }

    public int getYLocation(){
        return yLocation;
    }

    public int getColor(){
        return color;
    }

    public Board getBoard(){
        return chessBoard;
    }

    /**
     * Helper function for determining whether a figure can
     * move in a straight line, in any direction.
     * @param xPosition The specified x location
     * @param yPosition The specified y location
     * @return
     */
    protected boolean isMovingStraight(int xPosition, int yPosition) {
        int currX = this.getXLocation();
        int currY = this.getYLocation();

        int smallerVal;
        int largerVal;

        // Fixed X Position
        if (currX == xPosition){
            if (currY > yPosition){
                smallerVal = yPosition;
                largerVal = currY;
            }
            else if (yPosition > currY){
                smallerVal = currY;
                largerVal = yPosition;
            }
            else return false;

            // Loop to determine if any figure is between
            // target location and this figure.
            smallerVal++;
            for(; smallerVal < largerVal; smallerVal++){
                if (chessBoard.figureAt(currX, smallerVal) != null){
                    return false;
                }
            }
            return true;
        }

        // Fixed Y Position
        if (currY == yPosition){
            if (currX > xPosition){
                smallerVal = xPosition;
                largerVal = currX;
            }
            else if (xPosition > currX){
                smallerVal = currX;
                largerVal = xPosition;
            }
            else return false;

            // Loop to determine if any figure is between
            // target location and this figure.
            smallerVal++;
            for(; smallerVal < largerVal; smallerVal++){
                if (chessBoard.figureAt(smallerVal, currY) != null){
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Helper function for determining whether a figure can
     * move in a diagonal line.
     * @param xPosition The specified x location
     * @param yPosition The specified y location
     * @return
     */
    protected boolean isMovingDiagonal(int xPosition, int yPosition) {
        int xStart = 0;
        int yStart = 0;
        int xFinish = 1;
        //int yFinish = 1;

        //Check if movement is diagonal
        int xTotal = Math.abs(xPosition - this.getXLocation());
        int yTotal = Math.abs(yPosition - this.getYLocation());

        if (xTotal == yTotal){
            if (xPosition < this.getXLocation()){
                xStart = xPosition;
                xFinish = this.getXLocation();
            }
            else if (xPosition > this.getXLocation()){
                xStart = this.getXLocation();
                xFinish = xPosition;
            }
            else
                return false;

            if (yPosition < this.getYLocation()){
                yStart = yPosition;
                //yFinish = this.getYLocation();
            }
            else if (yPosition > this.getYLocation()){
                yStart = this.getYLocation();
                //yFinish = yPosition;
            }
            else
                return false;

            xStart++;
            yStart++;

            // Loop to see if any figure is in between
            for(;xStart < xFinish; xStart++, yStart++){
                if (chessBoard.figureAt(xStart, yStart) != null){
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public FigureType getFigureType() {
        return figureType;
    }
}
