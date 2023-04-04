package Interpreter;

import LexicalAnalysis.Lexeme;
import LexicalAnalysis.Type;
import Redstone.Redstone;
import Enviornments.Enviornment;

import static LexicalAnalysis.Type.*;


public class Interpreter {

    private Lexeme error(String message, Lexeme lexeme){
        Redstone.runtimeError(message, lexeme);
        return new Lexeme(lexeme.getLineNumber(), ERROR);
    }

    private Lexeme error(String message, int lineNumber){
        Redstone.runtimeError(message, lineNumber);
        return new Lexeme(lineNumber, ERROR);
    }

    public Lexeme eval(Lexeme tree, Enviornment enviornment){
        return switch(tree.getType()){
            case STATEMENT_LIST -> evalStatementList(tree, enviornment);
            case INTEGER, BOOLEAN, REAL, STRING -> tree;
            case IDENTIFIER -> enviornment.lookup(tree);
            case SUMMON -> declartion(tree, enviornment);
            case ASSIGNMENT -> assignment(tree, enviornment);
            case INIT -> initialization(tree, enviornment);
            default -> error("Cannot evaluate " + tree, tree.getLineNumber());
        };
    }

    private Lexeme evalStatementList(Lexeme tree, Enviornment enviornment) {
        Lexeme result = new Lexeme(EMPTY);
        for(Lexeme lexeme : tree.getChildren()) result = eval(lexeme, enviornment);
        return result;
    }

    private Lexeme declartion(Lexeme tree, Enviornment enviornment) {
        Lexeme id = eval(tree.getChild(0), enviornment);
        enviornment.add(IDENTIFIER, id);
        return id;
    }

    private Lexeme assignment(Lexeme tree, Enviornment enviornment) {
        Lexeme id = eval(tree.getChild(0), enviornment);
        Lexeme value = eval(tree.getChild(1), enviornment);

        enviornment.update(id, value);
        return id;
    }

    private Lexeme initialization(Lexeme tree, Enviornment enviornment){
        Lexeme id = eval(tree.getChild(0), enviornment);
        Lexeme value = eval(tree.getChild(0), enviornment);

        enviornment.update(id, value);
        return id;
    }
}
