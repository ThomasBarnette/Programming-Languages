package Interpreter;

import static Interpreter.Arithmetic.*;
import LexicalAnalysis.Lexeme;
import LexicalAnalysis.Type;
import Redstone.Redstone;
import Enviornments.Environment;

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

    public Lexeme eval(Lexeme tree, Environment environment){
        return switch(tree.getType()){
            case STATEMENT_LIST -> evalStatementList(tree, environment);
            case INTEGER, BOOLEAN, REAL, STRING, MINE, DROP-> tree;
            case IDENTIFIER -> environment.lookup(tree);

            
            case SUMMON -> declartion(tree, environment);
            case ASSIGNMENT -> assignment(tree, environment);
            case INIT -> initialization(tree, environment);

            case PLUS_EQUALS, MINUS_EQUALS, SLASH_EQUALS, TIMES_EQUALS -> evalNaryAssignment(tree, environment);
            case PLUS, MINUS, TIMES, SLASH, MOD -> evalNaryExpression(tree, environment);
            case TIMES_TIMES, MOD_MOD, SQRT -> evalUnaryExpression(tree, environment);

            case REPEATER -> repeaterLoop(tree, environment);
            case COMPARATOR -> comparatorLoop(tree, environment);

            case HOPPER, DROPPER, HOPPER_DROPPER -> functionDef(tree, environment);
            // case DROP -> evalReturn(tree, environment);
            case FUNCTION_CALL -> functionCall(tree, environment);
            case PARAM_LIST -> evalAllChildren(tree, environment);

            case CONDITIONAL_BLOCK -> evalConditionalBlock(tree, environment);
            case IF, EIF, ESE -> evalConditional(tree, environment);

            case AND, OR -> evalConditionalLogic(tree, environment);
            case EQUALITY, INEQUALITY, WITHIN_EQUALITY, GREATER_THAN, GREATER_THAN_EQUALTO, LESS_THAN, LESS_THAN_EQUALTO -> evalConditionalExpr(tree, environment);
            case PRINT -> evalPrint(tree, environment);

            default -> error("Cannot evaluate " + tree, tree.getLineNumber());
        };
    }

    private Lexeme evalStatementList(Lexeme tree, Environment environment) {
        Lexeme result = new Lexeme(EMPTY);
        for(int i = 0; i < tree.getChildren().size(); i++) result = eval(tree.getChild(i), environment);
        return result;
    }

    private Lexeme declartion(Lexeme tree, Environment environment) {
        Lexeme id = tree.getChild(0);
        environment.add(IDENTIFIER, id);
        return id;
    }

    private Lexeme assignment(Lexeme tree, Environment environment) {
        Lexeme id = tree.getChild(0);
        Lexeme value = eval(tree.getChild(1), environment);

        environment.update(id, value);
        return id;
    }

    private Lexeme initialization(Lexeme tree, Environment environment){
        Lexeme id = eval(tree.getChild(0), environment);
        Lexeme value = eval(tree.getChild(1), environment);

        environment.update(id, value);
        return id;
    }

    private Lexeme evalNaryAssignment(Lexeme tree, Environment environment){
        Lexeme id = tree.getChild(0);
        id = environment.lookup(id);
        Lexeme to = eval(tree.getChild(1), environment);

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

        environment.update(id, to);
        return id;
    }

    private Lexeme evalNaryExpression(Lexeme tree, Environment environment){
        Type type = tree.getType();
        int numChildren = tree.getChildren().size();
        if(numChildren <= 1) return tree;

        if(type == PLUS){
            Lexeme sum = eval(tree.getChild(0), environment);
            for(int i = 2; i <= numChildren; i++){
                sum = add(sum, eval(tree.getChild(i-1), environment));
            }
            return sum;
        }

        else if(type == MINUS){
            Lexeme diff = eval(tree.getChild(0), environment);
            for(int i = 2; i <= numChildren; i++){
                 diff = sub(diff, eval(tree.getChild(i-1), environment));
            }
            return diff;
        }

        else if(type == SLASH){
            Lexeme dividend = eval(tree.getChild(0), environment);
            for(int i = 2; i <= numChildren; i++){
                dividend = div(dividend, eval(tree.getChild(i-1), environment));
            }
            return dividend;
        }

        else if(type == TIMES){
            Lexeme product = eval(tree.getChild(0), environment);
            for(int i = 2; i <= numChildren; i++){
                product = times(product, eval(tree.getChild(i-1), environment));
            }
            return product;
        }

        else if(type == MOD){
            Lexeme remainder = eval(tree.getChild(0), environment);
            for(int i = 2; i <= numChildren; i++){
                remainder = mod(remainder, eval(tree.getChild(i-1), environment));
            }
            return remainder;
        }

        return error("Expected nary operator!", tree);
    }

    private Lexeme evalUnaryExpression(Lexeme tree, Environment environment){
        Type type = tree.getType();
        Lexeme val = eval(tree.getChild(0), environment);
        if(type == IDENTIFIER) val = environment.lookup(val);
        if(type == TIMES_TIMES) return times(val, null);
        if(type == SQRT) return sqrt(val);
        if(type == MOD_MOD) return modMod(val);
        return null;
    }

    private Lexeme evalConditionalBlock(Lexeme tree, Environment environment){
        int numChildren = tree.getChildren().size();
        if(numChildren == 0) return error("Expected conditional", tree);
        Lexeme ifCondition = eval(tree.getChild(0).getChild(0), environment);

        //Testing the 1 if case
        if(ifCondition.getBoolValue() == true) return eval(tree.getChild(0), environment);

        //Testinng EIF cases
        if(numChildren>1 && tree.getChild(1).getType() == EIF){
            for(int i = 1; i<numChildren-1; i++){
                Lexeme eifCondition = eval(tree.getChild(i).getChild(0), environment);
                if(eifCondition.getBoolValue() == true) return eval(tree.getChild(i), environment);
            }
        }
        //Else case must be ese (which must be last in the array)
        return eval(tree.getChild(numChildren-1), environment);
    }

    private Lexeme evalConditional(Lexeme tree, Environment environment){
        Environment conditionalEnviornment = new Environment(environment);
        Lexeme result;
        if(tree.getType() != ESE) result = eval(tree.getChild(1), conditionalEnviornment);
        else result = eval(tree.getChild(0), conditionalEnviornment);
        return result;
    }
    
    private Lexeme comparatorLoop(Lexeme tree, Environment environment){
        Lexeme result = new Lexeme();
        while(result.getType() != MINE) {
            Environment loopEnviornment = new Environment(environment);
            for(int i = 0; i < tree.getChildren().size(); i++) result = eval(tree.getChild(0).getChild(i), loopEnviornment); 
            if(result.getType() == MINE) break;
        }
        return result;
    }

    private Lexeme repeaterLoop(Lexeme tree, Environment environment){
        Lexeme tick = eval(tree.getChild(0), environment);
        Lexeme result = null;
        if(tick.getType() != INTEGER || !(tick.getIntValue()<4 && tick.getIntValue() >= 0)) return error("Repeaer loops must evaluate to an int between 0 and 3", tree);
        for(int i = tick.getIntValue(); i >= 0; i--){
            Environment loopEnviornment = new Environment(environment);
            Lexeme id = new Lexeme(tree.getLineNumber(), "tick", IDENTIFIER);
            loopEnviornment.add(INTEGER, id, new Lexeme(tick.getLineNumber(), i, INTEGER));
            result = eval(tree.getChild(1), loopEnviornment);
            if(result.getType() == MINE) break;
        }
        return result;
    }

    private Lexeme functionDef(Lexeme tree, Environment environment){
        environment.add(tree.getType(), tree.getChild(0), tree);
        tree.setDefiningEnviornment(environment);
        return null;
    }

    private Lexeme functionCall(Lexeme tree, Environment environment){
        Lexeme functionName = tree.getChild(0);
        Lexeme functionTree = environment.lookup(functionName);

        if(functionTree.getType() != HOPPER && functionTree.getType() != DROPPER && functionTree.getType() != HOPPER_DROPPER) return error("Attempted to call function, but none exists", tree);

        Environment definingEnviornment = functionTree.getDefiningEnviornment();
        Environment callEnviornment = new Environment(definingEnviornment);
        Lexeme parameterList = null;
        Lexeme argumentList = null;
        Lexeme functionStatements = null;
        if(functionTree.getType() == HOPPER || functionTree.getType() == HOPPER_DROPPER){
            parameterList = functionTree.getChild(1);
            functionStatements = functionTree.getChild(2);
            if(tree.getChildren().size()==2){
                 argumentList = tree.getChild(1);
                 evalAllChildren(argumentList, environment);
            }
            else return error("Attempting to call function that expectes arguments, but none given", tree);
        } else if(tree.getChildren().size()==2) return error("Trying to call dropper function with arguments", tree);
        callEnviornment.extend(parameterList, argumentList);

        if(functionTree.getType() == DROPPER){
             functionStatements = functionTree.getChild(1);
             return evalFunction(functionStatements, callEnviornment);

        } else if(functionTree.getType() == HOPPER_DROPPER){
            functionStatements = functionTree.getChild(2);
            return evalFunction(functionStatements, callEnviornment);
        }
        return eval(functionStatements, callEnviornment);
    }

    public Lexeme evalFunction(Lexeme tree, Environment environment){
        Lexeme result;
        for(int i = 0; i<tree.getChildren().size(); i++){
            result = eval(tree.getChild(i), environment);
            if(result.getType() == DROP) return eval(result.getChild(0), environment);
        }
        return error("Lexeme: '" + tree + "' must drop a vaule!", tree.getLineNumber());
    }

    public Lexeme evalAllChildren(Lexeme tree, Environment environment){
        Lexeme result = null;
        for(int i = 0; i < tree.getChildren().size(); i++){
            result = tree.setChild(i, eval((tree.getChild(i)), environment));
        }
        return result;
    }

    public Lexeme evalConditionalLogic(Lexeme tree, Environment environment){
        Type type = tree.getType();
        if(type == OR) return new Lexeme(tree.getLineNumber(), eval(tree.getChild(1), environment).getBoolValue() || eval(tree.getChild(0), environment).getBoolValue(), BOOLEAN);
        if(type == AND) return new Lexeme(tree.getLineNumber(), eval(tree.getChild(1), environment).getBoolValue() && eval(tree.getChild(0), environment).getBoolValue(), BOOLEAN);
        return error("Can not evaluate conditional logic", tree);
    }

    public Lexeme evalConditionalExpr(Lexeme tree, Environment environment){

        //Evaluate within equality
        Lexeme withinNum = null;
        if(tree.getType() == WITHIN_EQUALITY){
            if(tree.getRealValue() != null) withinNum = new Lexeme(tree.getLineNumber(), tree.getRealValue(), REAL);
            else if(tree.getIntValue() != null) withinNum = new Lexeme(tree.getLineNumber(), tree.getIntValue(), INTEGER);
            else error("Not a valid expression for within equality", tree);
        }

        if(tree.getType() == EQUALITY) return equality(eval(tree.getChild(0), environment), eval(tree.getChild(1), environment));
        if(tree.getType() == INEQUALITY) return inequality(eval(tree.getChild(0), environment), eval(tree.getChild(1), environment));
        if(tree.getType() == WITHIN_EQUALITY) return withinEquality(eval(tree.getChild(0), environment), withinNum, eval(tree.getChild(1), environment));
        if(tree.getType() == GREATER_THAN) return greaterThan(eval(tree.getChild(0), environment), eval(tree.getChild(1), environment));
        if(tree.getType() == GREATER_THAN_EQUALTO) return greaterThanEqualTo(eval(tree.getChild(0), environment), eval(tree.getChild(1), environment));
        if(tree.getType() == LESS_THAN) return lessThan(eval(tree.getChild(0), environment), eval(tree.getChild(1), environment));
        if(tree.getType() == LESS_THAN_EQUALTO) return lessThanEqualTo(eval(tree.getChild(0), environment), eval(tree.getChild(1), environment));
        return error("Expected conditional operator", tree);
    }

    public Lexeme evalPrint(Lexeme tree, Environment environment){
        Lexeme expr = eval(tree.getChild(0), environment);
        if(expr.getType() == IDENTIFIER) expr = environment.lookup(expr);
        System.out.println(expr.toValueOnlyString());
        return expr;
    }
}