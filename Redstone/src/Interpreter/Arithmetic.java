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
        Type type1 = first.getType();
        Type type2 = second.getType();

        //First is int
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getIntValue() + second.getIntValue(), INTEGER);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getIntValue() + second.getRealValue(), REAL);
        if(type1 == INTEGER && type2 == STRING) return new Lexeme(first.getLineNumber(), second.getStringValue() + " ".repeat(first.getIntValue()), STRING);
        if(type1 == INTEGER && type2 == BOOLEAN) {
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), first.getIntValue() + val, INTEGER);
        }

        //First is real
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getRealValue() + second.getIntValue(), REAL);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getRealValue() + second.getRealValue(), REAL);
        if(type1 == REAL && type2 == BOOLEAN) {
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), first.getRealValue() + val, REAL);
        }

        //First is string
        if(type1 == STRING && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getStringValue() + " ".repeat(second.getIntValue()), BOOLEAN);
        if(type1 == STRING && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getStringValue() + second.getStringValue(), STRING);
    
        //First is bool
        if(type1 == BOOLEAN && type2 == INTEGER) return second.getIntValue() != 0 ? new Lexeme(first.getLineNumber(), true, BOOLEAN) : new Lexeme(first.getLineNumber(), false, BOOLEAN);
        if(type1 == BOOLEAN && type2 == REAL) return second.getRealValue() != 0 ? new Lexeme(first.getLineNumber(), true, BOOLEAN) : new Lexeme(first.getLineNumber(), false, BOOLEAN);
        if(type1 == BOOLEAN && type2 == BOOLEAN) return new Lexeme(first.getLineNumber(), first.getBoolValue() || second.getBoolValue(), BOOLEAN);
        return error("Cannot add '" + type1 + "'' with '" + type2 + "'.", first);
    }

    public static Lexeme sub(Lexeme first, Lexeme second){
        Type type1 = first.getType();
        Type type2 = second.getType();
        
        //First is int
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getIntValue() - second.getIntValue(), INTEGER);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getIntValue() - second.getRealValue(), REAL);
        if(type1 == INTEGER && type2 == BOOLEAN) {
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), first.getIntValue() - val, INTEGER);
        }

        //First is real
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getRealValue() - second.getIntValue(), REAL);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getRealValue() - second.getRealValue(), REAL);
        if(type1 == REAL && type2 == BOOLEAN) {
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), first.getRealValue() - val, REAL);
        }

        //First is string
        if(type1 == STRING && type2 == INTEGER && second.getIntValue() > 0 && second.getIntValue() < first.getStringValue().length()-1) return new Lexeme(first.getLineNumber(), first.getStringValue().substring(0, first.getStringValue().length()-second.getIntValue()), STRING);
    
        //First is bool
        if(type1 == BOOLEAN && type2 == INTEGER) return second.getIntValue() != 0 ? new Lexeme(first.getLineNumber(), true, BOOLEAN) : new Lexeme(first.getLineNumber(), false, BOOLEAN);
        if(type1 == BOOLEAN && type2 == REAL) return second.getRealValue() != 0 ? new Lexeme(first.getLineNumber(), true, BOOLEAN) : new Lexeme(first.getLineNumber(), false, BOOLEAN);
        if(type1 == BOOLEAN && type2 == STRING) return error("Cannot add type Boolean with Type String", first);
        if(type1 == BOOLEAN && type2 == BOOLEAN) return new Lexeme(first.getLineNumber(), first.getBoolValue() ^ second.getBoolValue(), BOOLEAN);
        return error("Cannot subtract '" + type2 + "'' from '" + type2 + "'.", first);
    }

    public static Lexeme div(Lexeme first, Lexeme second){
        Type type1 = first.getType();
        Type type2 = second.getType();
        
        //First is int
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getIntValue() / second.getIntValue(), REAL);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getIntValue() / second.getRealValue(), REAL);

        //First is real
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getRealValue() + second.getIntValue(), REAL);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getRealValue() / second.getRealValue(), REAL);

        //First is string
        if(type1 == STRING && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getStringValue().substring(0, first.getStringValue().length()/second.getIntValue()), STRING);
    
        //First is bool
        if(type1 == BOOLEAN && type2 == BOOLEAN) return new Lexeme(first.getLineNumber(), first.getBoolValue() == second.getBoolValue(), BOOLEAN);
        return error("Cannot divide '" + type2 + "'' from '" + type1 + "'.", first);
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
        Type type1 = first.getType();
        Type type2 = second.getType();
        
        //First is int
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getIntValue() * second.getIntValue(), INTEGER);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getIntValue() * second.getRealValue(), REAL);
        if(type1 == INTEGER && type2 == STRING) return new Lexeme(first.getLineNumber(), second.getStringValue().repeat(first.getIntValue()), STRING);
        if(type1 == INTEGER && type2 == BOOLEAN) {
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), first.getIntValue() * val, INTEGER);
        }

        //First is real
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getRealValue() * second.getIntValue(), REAL);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getRealValue() * second.getRealValue(), REAL);
        if(type1 == REAL && type2 == STRING) return new Lexeme(first.getLineNumber(), second.getStringValue().repeat((first.getRealValue()).intValue()), STRING);
        if(type1 == REAL && type2 == BOOLEAN) {
            int val = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), first.getRealValue() * val, REAL);
        }

        //First is string
        if(type1 == STRING && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getStringValue().repeat(second.getIntValue()), STRING);
        if(type1 == STRING && type2 == REAL) return new Lexeme(first.getLineNumber(), first.getStringValue().repeat((second.getRealValue()).intValue()), STRING);
        if(type1 == STRING && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getStringValue().length() * second.getStringValue().length(), STRING);
        if(type1 == STRING && type2 == BOOLEAN) return second.getBoolValue() ? new Lexeme(first.getLineNumber(), first.getStringValue(), STRING) : new Lexeme(first.getLineNumber(), "", STRING);
       
        //First is bool
        if(type1 == BOOLEAN && type2 == INTEGER) {
            int val = first.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), second.getIntValue() * val, INTEGER);
        }
        if(type1 == BOOLEAN && type2 == REAL) {
            int val = first.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), second.getIntValue() * val, REAL);
        }     
        if(type1 == BOOLEAN && type2 == STRING) return first.getBoolValue() ? new Lexeme(first.getLineNumber(), second.getStringValue(), STRING) : new Lexeme(first.getLineNumber(), "", STRING);   
        if(type1 == BOOLEAN && type2 == BOOLEAN) return new Lexeme(first.getLineNumber(), first.getBoolValue() && second.getBoolValue(), BOOLEAN);
        return error("Cannot multiply '" + type1 + "'' with '" + type2 + "'.", first);
    }

    public static Lexeme mod(Lexeme first, Lexeme second){
        Type type1 = first.getType();
        Type type2 = second.getType();
        
        //First is int
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getIntValue() % second.getIntValue(), INTEGER);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getIntValue() % second.getRealValue(), REAL);
        
        //First is real
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getRealValue() % second.getIntValue(), REAL);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getRealValue() % second.getRealValue(), REAL);

        //First is string
        if(type1 == STRING && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getStringValue().length() % second.getIntValue(), STRING);
        return error("Cannot add '" + type1 + "'' with '" + type2 + "'.", first);
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



    //Boolean comparators
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

    public static Lexeme withinEquality(Lexeme first, Lexeme num, Lexeme second){
        Type type1 = first.getType();
        Type type2 = second.getType();
        double val = 0;
        if(num.getType() == REAL)  val = num.getRealValue();
        else if(num.getType() == INTEGER) val = (double)num.getIntValue();
        else return error("Error on within equality — must be of type INT or REAL", first);
        
        //Type 1 = INT
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), Math.abs(first.getIntValue() - second.getIntValue()) <= val, BOOLEAN);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), Math.abs(first.getIntValue() - second.getRealValue()) <= val, BOOLEAN);
        if(type1 == INTEGER && type2 == STRING) return new Lexeme(first.getLineNumber(), Math.abs(first.getIntValue() - second.getStringValue().length()) <= val, BOOLEAN);
        if(type1 == INTEGER && type2 == BOOLEAN){
            int val2 = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), Math.abs(first.getIntValue() - val2) <= val, BOOLEAN);
        } 
        
        //Type 1 = REAL
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), Math.abs(first.getRealValue() - second.getIntValue()) <= val, BOOLEAN);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), Math.abs(first.getRealValue() - second.getRealValue()) <= val, BOOLEAN);
        if(type1 == REAL && type2 == STRING) return new Lexeme(first.getLineNumber(), Math.abs(first.getRealValue() - second.getStringValue().length()) <= val, BOOLEAN);
        if(type1 == REAL && type2 == BOOLEAN){
            int val2 = second.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), Math.abs(first.getRealValue() - val2) <= val, BOOLEAN);
        } 

        //Type 1 = STRING
        if(type1 == STRING && type2 == INTEGER) return new Lexeme(first.getLineNumber(), Math.abs(first.getStringValue().length() - second.getIntValue()) <= val, BOOLEAN);
        if(type1 == STRING && type2 == REAL) return new Lexeme(first.getLineNumber(), Math.abs(first.getStringValue().length() - second.getRealValue()) <= val, BOOLEAN);
        if(type1 == STRING && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getStringValue().equals(second.getStringValue()), BOOLEAN);
        if(type1 == STRING && type2 == BOOLEAN) return error("Cannot use within equality between type string and type boolean", first);

        //Type 1 = BOOLEAN
        if(type1 == BOOLEAN && type2 == INTEGER) {
            int val2 = first.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), Math.abs(val2 - second.getIntValue()) <= val, BOOLEAN);
        }
        if(type1 == BOOLEAN && type2 == REAL){
            int val2 = first.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), Math.abs(val2 - second.getRealValue()) <= val, BOOLEAN);
        } 
        if(type1 == BOOLEAN && type2 == STRING){
            int val2 = first.getBoolValue() ? 1 : 0;
            return new Lexeme(first.getLineNumber(), Math.abs(val2 - second.getStringValue().length()) <= val, BOOLEAN);
        }
        if(type1 == BOOLEAN && type2 == BOOLEAN) return error("cannot use within equality between two booleans", first);
        return error("Cannot compare '" + type1 + "'' with '" + type2 + "'.", first);
    }

    public static Lexeme greaterThan(Lexeme first, Lexeme second){
        Type type1 = first.getType();
        Type type2 = second.getType();
        //Type 1 = INT
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getIntValue() > second.getIntValue(), BOOLEAN);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getIntValue() > second.getRealValue(), BOOLEAN);
        if(type1 == INTEGER && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getIntValue() > second.getStringValue().length(), BOOLEAN);
        if(type1 == INTEGER && type2 == BOOLEAN) return error("Cannot compare magnitude with type boolean", second);
        
        //Type 1 = REAL
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getRealValue() > (double)second.getIntValue(), BOOLEAN);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), first.getRealValue() > second.getRealValue(), BOOLEAN);
        if(type1 == REAL && type2 == STRING) return error("Unable to compare magnitude between type 'REAL' to type 'SRING'", first);
        if(type1 == REAL && type2 == BOOLEAN) return error("Cannot compare magnitude with type boolean", second);

        //Type 1 = STRING
        if(type1 == STRING && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getStringValue().length() > second.getIntValue(), BOOLEAN);
        if(type1 == STRING && type2 == REAL) return new Lexeme(first.getLineNumber(),first.getStringValue().length() > second.getRealValue(), BOOLEAN);
        if(type1 == STRING && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getStringValue().length() > second.getStringValue().length(), BOOLEAN);
        if(type1 == STRING && type2 == BOOLEAN) return error("Cannot compare magnitude with type boolean", second);
        //No boolean case
        return error("Cannot compare '" + type1 + "'' with '" + type2 + "'.", first);
    }

    public static Lexeme lessThan(Lexeme first, Lexeme second){
        Type type1 = first.getType();
        Type type2 = second.getType();
        //Type 1 = INT
        if(type1 == INTEGER && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getIntValue() < second.getIntValue(), BOOLEAN);
        if(type1 == INTEGER && type2 == REAL) return new Lexeme(first.getLineNumber(), (double)first.getIntValue() < second.getRealValue(), BOOLEAN);
        if(type1 == INTEGER && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getIntValue() < second.getStringValue().length(), BOOLEAN);
        if(type1 == INTEGER && type2 == BOOLEAN) return error("Cannot compare magnitude with type boolean", second);
        
        //Type 1 = REAL
        if(type1 == REAL && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getRealValue() < (double)second.getIntValue(), BOOLEAN);
        if(type1 == REAL && type2 == REAL) return new Lexeme(first.getLineNumber(), first.getRealValue() < second.getRealValue(), BOOLEAN);
        if(type1 == REAL && type2 == STRING) return error("Unable to compare magnitude between type 'REAL' to type 'SRING'", first);
        if(type1 == REAL && type2 == BOOLEAN) return error("Cannot compare magnitude with type boolean", second);

        //Type 1 = STRING
        if(type1 == STRING && type2 == INTEGER) return new Lexeme(first.getLineNumber(), first.getStringValue().length() < second.getIntValue(), BOOLEAN);
        if(type1 == STRING && type2 == REAL) return new Lexeme(first.getLineNumber(),first.getStringValue().length() < second.getRealValue(), BOOLEAN);
        if(type1 == STRING && type2 == STRING) return new Lexeme(first.getLineNumber(), first.getStringValue().length() < second.getStringValue().length(), BOOLEAN);
        if(type1 == STRING && type2 == BOOLEAN) return error("Cannot compare magnitude with type boolean", second);
        //No boolean case
        return error("Cannot compare '" + type1 + "'' with '" + type2 + "'.", first);    }

    public static Lexeme greaterThanEqualTo(Lexeme first, Lexeme second){
        return new Lexeme(first.getLineNumber(), !lessThan(first, second).getBoolValue(), BOOLEAN);
    }

    public static Lexeme lessThanEqualTo(Lexeme first, Lexeme second){
        return new Lexeme(first.getLineNumber(), !greaterThan(first, second).getBoolValue(), BOOLEAN);
    }
}
