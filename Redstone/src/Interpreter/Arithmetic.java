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

    public static Lexeme equality(Lexeme first, Lexeme second){
        Type type1 = first.getType();
        Type type2 = second.getType();
        //Type 1 = INT
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getIntValue() == second.getIntValue(), BOOLEAN);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getIntValue() == second.getRealValue(), BOOLEAN);
        if(type1 == INTEGER && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getIntValue() == second.getStringValue().length(), BOOLEAN);
        if(type1 == INTEGER && type2 == BOOLEAN){
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), first.getIntValue() == val, BOOLEAN);
        } 
        
        //Type 1 = REAL
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getRealValue() == (double)second.getIntValue(), BOOLEAN);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), first.getRealValue() == second.getRealValue(), BOOLEAN);
        if(type1 == REAL && type2 == STRING) return error("Unable to compare type 'REAL' to type 'SRING'", first);
        if(type1 == REAL && type2 == BOOLEAN){
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), first.getRealValue() == val, BOOLEAN);
        } 

        //Type 1 = STRING
        if(type1 == STRING && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getStringValue().equals(Integer.toString(second.getIntValue())), BOOLEAN);
        if(type1 == STRING && type2 == REAL) return new Lexeme(first.getLineNumber(),first.getStringValue().equals(Double.toString(second.getRealValue())), BOOLEAN);
        if(type1 == STRING && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getStringValue().equals(second.getStringValue()), BOOLEAN);
        if(type1 == STRING && type2 == BOOLEAN){
            String val = second.getBoolValue() ? "true" : "false";
            return new Lexeme(first.getLineNumber(), first.getStringValue().equals(val), BOOLEAN);
        } 

        //Type 1 = BOOLEAN
        if(type1 == BOOLEAN && type2 == INTEGER) {
            int val = first.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), second.getIntValue() == val, BOOLEAN);
        }
        if(type1 == BOOLEAN && type2 == REAL){
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), second.getRealValue() == val, BOOLEAN);
        } 
        if(type1 == BOOLEAN && type2 == STRING){
            String val = second.getBoolValue() ? "true" : "false";
            return new Lexeme(first.getLineNumber(), first.getStringValue().equals(val), BOOLEAN);
        }
        if(type1 == BOOLEAN && type2 == BOOLEAN) return new Lexeme(first.getLineNumber(), first.getBoolValue() == second.getBoolValue(), BOOLEAN);
        return error("Cannot compare '" + type1 + "'' with '" + type2 + "'.", first);
    }

    public static Lexeme inequality(Lexeme first, Lexeme second){
        return new Lexeme(first.getLineNumber(), !equality(first, second).getBoolValue(), BOOLEAN);
    }

    public static Lexeme withinEquality(Lexeme first, Lexeme val, Lexeme second){
        return null;
    }

    public static Lexeme greaterThan(Lexeme first, Lexeme second){
        return null;
    }

    public static Lexeme lessThan(Lexeme first, Lexeme second){
        return null;
    }

    public static Lexeme greaterThanEqualTo(Lexeme first, Lexeme second){
        return null;
    }

    public static Lexeme lessThanEqualTo(Lexeme first, Lexeme second){
        return null;
    }
}
