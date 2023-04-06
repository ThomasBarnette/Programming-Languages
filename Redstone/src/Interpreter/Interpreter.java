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
            case PLUS, MINUS, TIMES, SLASH, MOD -> evalNaryExpression(tree, enviornment);
            case TIMES_TIMES, MOD_MOD, SQRT -> evalUnaryExpression(tree, enviornment);

            case REPEATER -> repeaterLoop(tree, enviornment);
            case COMPARATOR -> comparatorLoop(tree, enviornment);

            case HOPPER, DROPPER, HOPPER_DROPPER -> functionDef(tree, enviornment);
            case FUNCTION_CALL -> functionCall(tree, enviornment);

            case CONDITIONAL_BLOCK -> evalConditionalBlock(tree, enviornment);
            case IF, EIF, ESE -> evalConditional(tree, enviornment);

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

    private Lexeme evalNaryExpression(Lexeme tree, Enviornment enviornment){
        Type type = tree.getType();
        int numChildren = tree.getChildren().size();
        if(numChildren <= 1) return tree;

        if(type == PLUS){
            Lexeme sum = tree.getChild(0);
            for(int i = 2; i< numChildren; i++){
                sum = add(sum, tree.getChild(i-1));
            }
            return sum;
        }

        else if(type == MINUS){
            Lexeme diff = tree.getChild(0);
            for(int i = 2; i< numChildren; i++){
                diff = sub(diff, tree.getChild(i-1));
            }
            return diff;
        }

        else if(type == SLASH){
            Lexeme dividend = tree.getChild(0);
            for(int i = 2; i< numChildren; i++){
                dividend = div(dividend, tree.getChild(i-1));
            }
            return dividend;
        }

        else if(type == TIMES){
            Lexeme product = tree.getChild(0);
            for(int i = 2; i< numChildren; i++){
                product = times(product, tree.getChild(i-1));
            }
            return product;
        }

        return error("Expected nary operator!", tree);
    }

    private Lexeme evalUnaryExpression(Lexeme tree, Enviornment enviornment){
        Type type = tree.getType();
        Lexeme val = tree.getChild(0);
        if(type == TIMES_TIMES) return times(val, null);
        if(type == SQRT) return sqrt(val);
        if(type == MOD_MOD) return modMod(val);
        return null;
    }

    private Lexeme evalConditionalBlock(Lexeme tree, Enviornment enviornment){
        int numChildren = tree.getChildren().size();
        if(numChildren == 0) return error("Expected conditional", tree);
        Lexeme ifCondition = eval(tree.getChild(0).getChild(0), enviornment);

        //Testing the 1 if case
        if(ifCondition.getBoolValue() == true) return eval(tree.getChild(0), enviornment);

        //Testinng EIF cases
        if(numChildren>1 && tree.getChild(1).getType() == EIF){
            for(int i = 1; i<numChildren-1; i++){
                Lexeme eifCondition = eval(tree.getChild(i).getChild(0), enviornment);
                if(eifCondition.getBoolValue() == true) return eval(tree.getChild(i), enviornment);
            }
        }
        //Else case must be ese (which must be last in the array)
        return eval(tree.getChild(numChildren-1), enviornment);
    }

    private Lexeme evalConditional(Lexeme tree, Enviornment enviornment){
        Enviornment conditionalEnviornment = new Enviornment(enviornment);
        Lexeme result;
        result = eval(tree.getChild(1), conditionalEnviornment);
        return result;
    }
    
    private Lexeme comparatorLoop(Lexeme tree, Enviornment enviornment){
        Lexeme result;
        while(true){
            result = evalStatementList(tree, enviornment);
            if(result.getType() == MINE) break;
        }
        return result;
    }

    private Lexeme repeaterLoop(Lexeme tree, Enviornment enviornment){
        Lexeme tick = eval(tree.getChild(0), enviornment);
        if(tick.getType() != INTEGER || !(tick.getIntValue()<4 && tick.getIntValue() >= 0)) return error("Repeaer loops must evaluate to an int between 0 and 3", tree);
        for(int i = tick.getIntValue(); i >= 0; i--){
            Enviornment loopEnviornment = new Enviornment(enviornment);
            Lexeme id = new Lexeme(tree.getLineNumber(), "tick", IDENTIFIER);
            loopEnviornment.add(INTEGER, id, new Lexeme(tick.getLineNumber(), i, INTEGER));
            eval(tree.getChild(1), loopEnviornment);
        }
        return null;
    }

    private Lexeme functionDef(Lexeme tree, Enviornment enviornment){
        //TODO
        return null;
    }

    private Lexeme functionCall(Lexeme tree, Enviornment enviornment){
        //TODO
        return null;
    }

    private Lexeme add(Lexeme first, Lexeme second){
        //TODO
        return null;
    }

    private Lexeme sub(Lexeme first, Lexeme second){
        //TODO
        //first - second
        return null;
    }

    private Lexeme div(Lexeme first, Lexeme second){
        //TODO
        return null;
    }

    private Lexeme times(Lexeme first, Lexeme second){
        
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

    private Lexeme mod(Lexeme first, Lexeme second){
        //TODO
        return null;
    }

    private Lexeme sqrt(Lexeme id){
        Type type = id.getType();
        if(type == INTEGER || type == REAL){
            double value = type == INTEGER ? id.getIntValue() : id.getRealValue();
            Lexeme to = new Lexeme(id.getLineNumber(), Math.sqrt(value), REAL);
            return to;
        }
        if(type == BOOLEAN) return id.getBoolValue() == true ? new Lexeme(id.getLineNumber(), 1, INTEGER) : new Lexeme(id.getLineNumber(), 0, INTEGER);
        return error("Expected type 'int',  with sqrt", id);
    }

    private Lexeme modMod(Lexeme val){
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
