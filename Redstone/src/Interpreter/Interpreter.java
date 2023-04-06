package Interpreter;

import static Interpreter.Arithmetic.*;
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
            case INTEGER, BOOLEAN, REAL, STRING, MINE -> tree;
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
            case PARAM_LIST -> evalAllChildren(tree, enviornment);

            case CONDITIONAL_BLOCK -> evalConditionalBlock(tree, enviornment);
            case IF, EIF, ESE -> evalConditional(tree, enviornment);

            case AND, OR -> evalConditionalLogic(tree, enviornment);
            case EQUALITY, INEQUALITY, WITHIN_EQUALITY, GREATER_THAN, GREATER_THAN_EQUALTO, LESS_THAN, LESS_THAN_EQUALTO -> evalConditionalExpr(tree, enviornment);

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
        evalAllChildren(tree, enviornment);
        if(numChildren <= 1) return tree;

        if(type == PLUS){
            Lexeme sum = tree.getChild(0);
            for(int i = 2; i <= numChildren; i++){
                sum = add(sum, tree.getChild(i-1));
            }
            return sum;
        }

        else if(type == MINUS){
            Lexeme diff = tree.getChild(0);
            for(int i = 2; i <= numChildren; i++){
                diff = sub(diff, tree.getChild(i-1));
            }
            return diff;
        }

        else if(type == SLASH){
            Lexeme dividend = tree.getChild(0);
            for(int i = 2; i <= numChildren; i++){
                dividend = div(dividend, tree.getChild(i-1));
            }
            return dividend;
        }

        else if(type == TIMES){
            Lexeme product = tree.getChild(0);
            for(int i = 2; i <= numChildren; i++){
                product = times(product, tree.getChild(i-1));
            }
            return product;
        }

        else if(type == MOD){
            Lexeme remainder = tree.getChild(0);
            for(int i = 2; i <= numChildren; i++){
                remainder = mod(remainder, tree.getChild(i-1));
            }
            return remainder;
        }

        return error("Expected nary operator!", tree);
    }

    private Lexeme evalUnaryExpression(Lexeme tree, Enviornment enviornment){
        Type type = tree.getType();
        Lexeme val = eval(tree.getChild(0), enviornment);
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
        enviornment.add(tree.getType(), tree);
        tree.setDefiningEnviornment(enviornment);
        return null;
    }

    private Lexeme functionCall(Lexeme tree, Enviornment enviornment){
        Lexeme functionName = eval(tree.getChild(0), enviornment);
        Lexeme functionTree = enviornment.lookup(functionName);

        if(functionTree.getType() != HOPPER || functionTree.getType() != DROPPER || functionTree.getType() != HOPPER_DROPPER) return error("Attempted to call function, but none exists", tree);

        Enviornment definingEnviornment = functionTree.getDefiningEnviornment();
        Enviornment callEnviornment = new Enviornment(definingEnviornment);
        Lexeme parameterList = null;
        Lexeme argumentList = null;
        Lexeme functionStatements = null;
        if(functionTree.getType() == HOPPER || functionTree.getType() == HOPPER_DROPPER){
            parameterList = eval(functionTree.getChild(1), enviornment);
            functionStatements = functionTree.getChild(2);
            if(tree.getChildren().size()==2) argumentList = eval(tree.getChild(1), enviornment);
            else return error("Attempting to call function that expectes arguments, but none given", tree);
        } else if(tree.getChildren().size()==2) return error("Trying to call dropper function with arguments", tree);
        
        callEnviornment.extend(parameterList, argumentList);

        if(functionTree.getType() == DROPPER) functionStatements = functionTree.getChild(1);
        return eval(functionStatements, callEnviornment);
    }

    public Lexeme evalAllChildren(Lexeme tree, Enviornment enviornment){
        Lexeme result = null;
        for(int i = 0; i < tree.getChildren().size(); i++){
            result = tree.setChild(i, eval((tree.getChild(i)), enviornment));
        }
        return result;
    }

    public Lexeme evalConditionalLogic(Lexeme tree, Enviornment enviornment){
        return null;
    }

    public Lexeme evalConditionalExpr(Lexeme tree, Enviornment enviornment){
        evalAllChildren(tree, enviornment);
        if(tree.getType() == EQUALITY) return equality(tree.getChild(0), tree.getChild(1));
        if(tree.getType() == INEQUALITY) return inequality(tree.getChild(0), tree.getChild(1));
        if(tree.getType() == WITHIN_EQUALITY) return withinEquality(tree.getChild(0), tree.getChild(1), tree.getChild(2));
        if(tree.getType() == GREATER_THAN) return greaterThan(tree.getChild(0), tree.getChild(1));
        if(tree.getType() == GREATER_THAN_EQUALTO) return greaterThanEqualTo(tree.getChild(0), tree.getChild(1));
        if(tree.getType() == LESS_THAN) return lessThan(tree.getChild(0), tree.getChild(1));
        if(tree.getType() == LESS_THAN_EQUALTO) return lessThanEqualTo(tree.getChild(0), tree.getChild(1));
        return error("Expected conditional operator", tree);
    }
}