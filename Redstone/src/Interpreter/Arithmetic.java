package Interpreter;

import LexicalAnalysis.Lexeme;
import LexicalAnalysis.Type;
import Redstone.Redstone;

import static LexicalAnalysis.Type.*;

public enum Arithmetic {
    ;

    public static Lexeme error(String message, Lexeme lexeme){
        Redstone.runtimeError(message, lexeme);
        return new Lexeme(lexeme.getLineNumber(), ERROR);
    }

    public static Lexeme error(String message, int lineNumber){
        Redstone.runtimeError(message, lineNumber);
        return new Lexeme(lineNumber, ERROR);
    }
    
    public static Lexeme add(Lexeme first, Lexeme second){
        //TODO
        return null;
    }

    public static Lexeme sub(Lexeme first, Lexeme second){
        //TODO
        //first - second
        return null;
    }

    public static Lexeme div(Lexeme first, Lexeme second){
        //TODO
        return null;
    }

    public static Lexeme times(Lexeme first, Lexeme second){
        
          //Square
          Lexeme to = null;
          if(second == null){
               if(first.getType() == INTEGER){
                    int value = first.getIntValue();
                    to = new Lexeme(first.getLineNumber(), value*value, first.getType());
               } else if(first.getType() == REAL){
                double value = first.getIntValue();
                to = new Lexeme(first.getLineNumber(), value*value, first.getType());
           }
        }

        if(to != null) return to;
        //TODO
        return error("Error on multpication", first);
    }

    public static Lexeme mod(Lexeme first, Lexeme second){
        //TODO
        return null;
    }

    public static Lexeme sqrt(Lexeme id){
        Type type = id.getType();
        if(type == INTEGER || type == REAL){
            double value = type == INTEGER ? id.getIntValue() : id.getRealValue();
            Lexeme to = new Lexeme(id.getLineNumber(), Math.sqrt(value), REAL);
            return to;
        }
        if(type == BOOLEAN) return id.getBoolValue() == true ? new Lexeme(id.getLineNumber(), 1, INTEGER) : new Lexeme(id.getLineNumber(), 0, INTEGER);
        return error("Expected type 'int',  with sqrt", id);
    }

    public static Lexeme modMod(Lexeme val){
        Type type = val.getType();
        if(type == INTEGER) return new Lexeme(val.getLineNumber(), Integer.toString(val.getIntValue()).length(), INTEGER);
        if(type == REAL) return new Lexeme(val.getLineNumber(), Double.toString(val.getRealValue()).length(), INTEGER);
        if(type == STRING) return new Lexeme(val.getLineNumber(), val.getStringValue().length(), INTEGER);
        if(type == BOOLEAN){
            int value = val.getBoolValue() == true ? 1 : 0;
            return  new Lexeme(val.getLineNumber(), value, INTEGER);
        }
    return error("Unable to evaluate modMod", val);
    } 
}
