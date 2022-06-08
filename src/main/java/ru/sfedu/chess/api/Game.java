package ru.sfedu.chess.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.chess.model.*;

import java.util.LinkedList;
import java.util.Scanner;
//вынести константы, в модель фигуры, в апи Game, client
public class Game {
    private static final Logger log = LogManager.getLogger(Game.class);

    Scanner userInput = new Scanner(System.in);

    private int currentPlayer;
    private Board chessBoard;
    private LinkedList<Figure> blackFigures;
    private LinkedList<Figure> whiteFigures;
    private King blackKing;
    private King whiteKing;
    private int startX, startY, finishX, finishY;

    public Game(){
        
        chessBoard = new Board(8,8);
        currentPlayer = Constants.WHITE;
        blackFigures = new LinkedList<Figure>();
        whiteFigures = new LinkedList<Figure>();

        blackKing = new King(chessBoard, Constants.BLACK, 1,2);
        whiteKing = new King(chessBoard, Constants.WHITE, 3,5);
        blackFigures.add(blackKing);
        whiteFigures.add(whiteKing);
    }


    public void testSetup() {
        blackKing.moveTo(5, 0);
        whiteKing.moveTo(7, 1);
        this.addPawn(Constants.BLACK, 1, 1);
        this.addPawn(Constants.BLACK, 2, 1);
        this.addPawn(Constants.BLACK, 3, 1);
        this.addPawn(Constants.BLACK, 4, 1);
        this.addPawn(Constants.BLACK, 5, 1);
        this.addPawn(Constants.BLACK, 6, 1);
        this.addQueen(Constants.BLACK, 4, 0);
        this.addRook(Constants.BLACK, 3, 0);
        this.addBishop(Constants.BLACK, 2, 0);
        currentPlayer = Constants.BLACK;
    }

    /*
     * Continues to loop until game is over.
     *
     * IS NOT COMPLETE. only to see if game is working well outside of test cases.
     */
    public void gameLoop(){
        boolean continueGame = true;

        testSetup();

        while(continueGame){
            chessBoard.displayBoard();

            if (isGameOver()){
                break;
            }

            log.info("Which figure to move? X-loc: ");
            startX = userInput.nextInt();
            log.info("Y-loc: ");
            startY = userInput.nextInt();

            Figure target = chessBoard.figureAt(nextX, nextY);
            if (target == null){
                log.info("That location is invalid");
                continueGame = false;
            }
            else if (target.getColor() != currentPlayer){
                log.info("That is not your figure");
                continueGame = false;
            }
            else {
                log.info("Where to move this figure? x-loc: ");
                nextX = userInput.nextInt();
                log.info("Y-loc: ");
                nextY = userInput.nextInt();

                if (target.canMoveTo(nextX, nextY)){
                    target.moveTo(nextX, nextY);
                }
                else {
                    log.info("Cannot move there");
                }
            }
        }
    }

    /**
     * Checks to see if game-ending situation has occurred
     *
     * NOTE: few more game-ending situations should be added,
     * like 50 move rule, threefold repetition.
     *
     * @return - True if game is over
     */
    public boolean isGameOver(){
        if (isCheckmate(Constants.BLACK) || isCheckmate(Constants.WHITE)){
            log.info("CHECKMATE");
            return true;
        }
        else if (!canMove(currentPlayer)){
            log.info("STALEMATE");
            return true;
        }
        return false;
    }

    /**
     * Check to see if the given player
     * is in a checkmate situation
     * @param color - color of the player who may be in checkmate
     * @return - True if player is indeed in checkmate
     */
    public boolean isCheckmate(int color){
        if (isKingInCheck(color)){
            return !canMove(color);
        }

        return false;
    }

    /**
     * Determines whether the given player has any valid
     * moves left to play
     * @param player - Player who's moves are being checked
     * @return - True if the player still has valid moves
     */
    public boolean canMove(int player){
        int oldX, oldY;
        Figure target;
        LinkedList<Figure> checkFigures;

        if (player == Constants.BLACK)
            checkFigures = blackFigures;
        else
            checkFigures = whiteFigures;

        for (int x = 0; x < chessBoard.getXDimension(); x++){
            for (int y = 0; y < chessBoard.getYDimension(); y++){
                // If any figure can move to this spot, move here
                // If king is still in check, then go to next location.
                for (Figure currentFigure : checkFigures){
                    if (currentFigure.canMoveTo(x, y)){
                        target = chessBoard.figureAt(x, y);
                        oldX = currentFigure.getXLocation();
                        oldY = currentFigure.getYLocation();

                        currentFigure.moveTo(x, y);

                        if (!isKingInCheck(player)){
                            currentFigure.moveTo(oldX, oldY);
                            if (target != null)
                                target.moveTo(x, y);
                            return true;
                        } else {
                            currentFigure.moveTo(oldX, oldY);
                            if (target != null)
                                target.moveTo(x, y);
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if a given player's king is in check
     * @param color - the color of the player's king being checked
     * @return - True if the specified king is in check.
     */
    public boolean isKingInCheck(int color){
        boolean result = false;

        LinkedList<Figure> originalList;
        King kingInQuestion;

        if (color == Constants.BLACK){
            originalList = whiteFigures;
            kingInQuestion = blackKing;
        } else {
            originalList = blackFigures;
            kingInQuestion = whiteKing;
        }

        int xKingLoc = kingInQuestion.getXLocation();
        int yKingLoc = kingInQuestion.getYLocation();

        for (Figure currentFigure : originalList){
            if (currentFigure.canMoveTo(xKingLoc, yKingLoc)){
                result = true;
            }
        }

        return result;
    }

    /**
     * Removes this figure from the game
     *
     * ASSERT that the removed figure is already in game.
     * @param removeThisFigure the figure to remove.
     */
    public void removeFigure(Figure removeThisFigure){
        removeThisFigure.removeFigure();
        int color = removeThisFigure.getColor();

        if (color == Constants.BLACK)
            blackFigures.remove(removeThisFigure);
        else
            whiteFigures.remove(removeThisFigure);
    }
    /**
     * Switches color of player, which turn is to move
     */
    public void switchPlayerTurn(){
        if (currentPlayer == Constants.WHITE)
            currentPlayer = Constants.BLACK;
        else currentPlayer = Constants.WHITE;
    }

    public Queen addQueen(int color, int xloc, int yloc){
        Queen queen = new Queen(chessBoard, color, xloc, yloc);
        figureToColorHelper(queen, color);

        return queen;
    }

    public Knight addKnight(int color, int xloc, int yloc){
        Knight knight = new Knight(chessBoard, color, xloc, yloc);
        figureToColorHelper(knight, color);

        return knight;
    }

    public Rook addRook(int color, int xloc, int yloc){
        Rook rook = new Rook(chessBoard, color, xloc, yloc);
        figureToColorHelper(rook, color);

        return rook;
    }

    public Bishop addBishop(int color, int xloc, int yloc){
        Bishop bishop = new Bishop(chessBoard, color, xloc, yloc);
        figureToColorHelper(bishop, color);

        return bishop;
    }

    public Pawn addPawn(int color, int xloc, int yloc){
        Pawn pawn = new Pawn(chessBoard, color, xloc, yloc);
        figureToColorHelper(pawn, color);

        return pawn;
    }

    private void figureToColorHelper(Figure figure, int color){
        if (color == Constants.BLACK)
            blackFigures.add(figure);
        else
            whiteFigures.add(figure);
    }

    public int getPlayerTurn(){
        return currentPlayer;
    }

    public void setPlayer(int player){
        currentPlayer = player;
    }

    public King getBlackKing(){
        return blackKing;
    }

    public King getWhiteKing(){
        return whiteKing;
    }
}
