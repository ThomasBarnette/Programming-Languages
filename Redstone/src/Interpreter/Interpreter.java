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

            case PLUS_EQUALS, MINUS_EQUALS, SLASH_EQUALS, TIMES_EQUALS -> evalNaryAssignment(tree, enviornment);
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

    private Lexeme evalNaryAssignment(Lexeme tree, Enviornment enviornment){
        Lexeme id = eval(tree.getChild(0), enviornment);
        Lexeme to = eval(tree.getChild(1), enviornment);

        switch(tree.getType()){
            case PLUS_EQUALS:
                to = add(id, to);
                break;
            case MINUS_EQUALS:
                to = sub(id, to);
                break;
            case SLASH_EQUALS:
                to = div(id, to);
                break;
            case TIMES_EQUALS:
                to = times(id, to);
                break;
            default:
                error("Expected nary assignment", tree);
        }

        enviornment.update(id, to);
        return id;
    }

    private Lexeme add(Lexeme first, Lexeme second){
        return null;
    }

    private Lexeme sub(Lexeme first, Lexeme second){
        //second - frist
        return null;
    }

    private Lexeme div(Lexeme first, Lexeme second){
        return null;
    }

    private Lexeme times(Lexeme first, Lexeme second){
          //Square
          Lexeme to;
          if(second == null){
               if(first.getType() == INTEGER){
                    int value = first.getIntValue();
                    to = new Lexeme(first.getLineNumber(), value*value, first.getType());
               } else if(first.getType() == REAL){
                double value = first.getIntValue();
                to = new Lexeme(first.getLineNumber(), value*value, first.getType());
           }
        }

        return null;
    }

    private Lexeme mod(Lexeme first, Lexeme second){
      
        return null;
    }

    private Lexeme sqrt(Lexeme id){
        Type type = id.getType();
        if(type == INTEGER || type == REAL){
            double value = type == INTEGER ? id.getIntValue() : id.getRealValue();
            Lexeme to = new Lexeme(id.getLineNumber(), Math.sqrt(value), REAL);
            return to;
        }
        return error("Expected type 'int' or 'real' with sqrt", id);
    }
}
