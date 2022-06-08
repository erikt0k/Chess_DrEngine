package ru.sfedu.chess.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.runtime.KieSession;
import ru.sfedu.chess.model.Board;
import ru.sfedu.chess.model.Figure;
import ru.sfedu.chess.model.Pawn;

public class Client {
    private static final Logger log = LogManager.getLogger(Client.class);
    public static void main(String[] args) {
        Board board = new Board(8,8);
        Pawn pawn = new Pawn(board, Figure.WHITE, 3,0);
        log.info("Pawn: x = " + pawn.getXLocation()+", y = "+pawn.getYLocation());
        RuleManager rm = new RuleManager();
        KieSession session = rm.createSession();
        session.insert(pawn);
        session.fireAllRules();
        log.info("new Pawn: x = " + pawn.getXLocation()+", y = "+pawn.getYLocation());
    }
}
