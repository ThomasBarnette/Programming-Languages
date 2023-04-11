package Parser;

import java.util.ArrayList;
import LexicalAnalysis.Lexeme;
import LexicalAnalysis.Type;

import static LexicalAnalysis.Type.*;
import Redstone.Redstone;

public class Parser {
    private final ArrayList<Lexeme> lexemes;
    private Lexeme currentLexeme;
    private int nextLexemeIndex;

    private final boolean showLogs = false;

    private boolean inLoop = false;

    public Parser(ArrayList<Lexeme> lexemes){
        this.lexemes = lexemes;
        this.nextLexemeIndex = 0;
        advance();
    }

    private void log(String heading){
        if(showLogs) System.out.println("---------" + heading + "----------");
    }

    private boolean check(Type type){
        return currentLexeme.getType() == type;
    }

    private boolean checkNext(Type type){
        if(nextLexemeIndex > lexemes.size()) return false;
        return lexemes.get(nextLexemeIndex).getType() == type;
    }

    private Lexeme consume(Type expected){
        if(check(expected)) return advance();

        Redstone.syntaxError("Expected " + expected + " instead found " + currentLexeme.getType(), currentLexeme.getLineNumber());
        return new Lexeme(currentLexeme.getLineNumber(), ERROR);
    }

    private Lexeme advance() {
        Lexeme toReturn = currentLexeme;
        currentLexeme = lexemes.get(nextLexemeIndex);
        nextLexemeIndex++;
        return toReturn;
    }

    private Lexeme error(String string) {
        Redstone.syntaxError(string, currentLexeme.getLineNumber());
        return new Lexeme(ERROR);
    }


    // -------- consumption functions ----------
    public Lexeme program(){
        log("program");
        if(statementListPending()) return statementList();
        else return new Lexeme(EMPTY_PROGRAM);
    }

    private Lexeme statementList(){
        log("statementList");
        ArrayList<Lexeme> statements = new ArrayList<>();
        Lexeme statementList = new Lexeme(currentLexeme.getLineNumber(), STATEMENT_LIST);
        while(statementPending()) statements.add(statement());
        statementList.addAll(statements);
        return statementList;
    }

    private Lexeme statement(){
        log("statement");
        Lexeme statement;
        if(assignmentPending()){
            statement = assignment();
            end();
        } 
        else if(intitializationPending()){
            statement = intitialization();
            end();
        }
        else if(declarationPending()){
             statement = declaration();
             end();
        }
        else if(deletionPending()){
             statement = deletion();
             end();
        }
        else if(conditionalStatementPending()) statement = conditionalStatement();
        else if(functionDefinitonPending()) statement = functionDefiniton();
        else if(loopPending()) statement = loop();
        else if(printPending()) statement = print();
        else if(expressionPending()){
            statement = expression();
            end();
       } else if(minePending()){
           if(inLoop) statement = mine();
           else return error("Found mine, but no loop to break");
       }
        else statement = error("Expected statement, none found");
        return statement;
    }

    private Lexeme expression(){
        log("expression");
        if(unaryExpressionPending()) return unaryExpression();
        else if(functionCallPending()) return functionCall();
        else if(naryExpressionPending()) return naryExpression();
        else if(conditionalExpressionPending()) return conditionalExpression();
        else if(primaryPending()) return primary();
        else return error("Expected expression, none found");
    }

    private Lexeme unaryExpression(){
        log("unaryExpression");
        Lexeme root = unaryOperator();
        root.addChild(primary());
        return root;
    }

    private Lexeme naryExpression(){
        log("naryExpression");
        Lexeme root;
        if(naryOperatorPending()){
            root = naryOperator();
            ArrayList<Lexeme> primaryBlocks = new ArrayList<>();
            while(primaryBlockPending()) root.addChild(primaryBlock());
            root.addAll(primaryBlocks);
            root.addChild(primary());
            return root;
        }
        else if(unaryAssignmentOperatorPending()){
            root = unaryAssignmentOperator();
            root.addChild(consume(IDENTIFIER));
            return root;
        }
        else if(naryAssignmentPending()){
            root = naryAssignmentOperator();
            root.addChild(consume(IDENTIFIER));
            root.addChild(expression());
            return root;
        }
        else return error("Expected nary expression, found none");
    }

    private Lexeme primaryBlock() {
        log("primaryBlock");
        Lexeme primary;
        primary = primary();
        consume(CONNECTION);
        return primary;
    }

    private Lexeme conditionalExpression(){
        log("conditionalExpression");
        ArrayList<Lexeme> childeren = new ArrayList<>();
        childeren.add(primary());
        Lexeme root = conditionalOperator();
        childeren.add(primary());
        if(conditinoalLogicOperatorPending()){
            root = conditionalLogicOperator();
            root.addChild(conditionalOperator());
            root.getChild(0).addAll(childeren);
            root.addChild(conditionalExpression());
        }
        else root.addAll(childeren);
        return root;
    }

    private Lexeme primary(){
        log("primary");
        if(check(STRING)) return consume(STRING);
        else if(check(IDENTIFIER)) return consume(IDENTIFIER);
        else if(check(REAL)) return consume(REAL);
        else if(check(INTEGER)) return consume(INTEGER);
        else if(functionCallPending()) return functionCall();
        else if(booleanLiteralPending()) return booleanLiteral();
        else if(check(LINEDOT)) {
            consume(LINEDOT);
            Lexeme expr = expression();
            consume(DOTLINE);
            return expr;
        }
        else return error("Expected primary, found none");
    }

    private Lexeme assignment(){
        log("assignment");
        Lexeme iden = consume(IDENTIFIER);
        Lexeme root = consume(ASSIGNMENT);
        root.addChild(iden);
        root.addChild(expression());
        return root;
    }

    private Lexeme intitialization(){
        log("initialization");
        Lexeme dec = declaration();
        Lexeme root = new Lexeme(INIT);
        consume(ASSIGNMENT);
        root.addChild(dec);
        root.addChild(expression());
        return root;
    }

    private Lexeme declaration(){
        log("declaration");
        Lexeme root = consume(SUMMON);
        root.addChild(consume(IDENTIFIER));
        return root;
    }

    private Lexeme deletion(){
        log("deletion");
        Lexeme root = consume(KILL);
        root.addChild(consume(IDENTIFIER));
        return root;
    }

    private Lexeme functionCall(){
        log("functionCall");
        Lexeme root = new Lexeme(currentLexeme.getLineNumber(), FUNCTION_CALL);
        root.addChild(consume(IDENTIFIER));
        consume(LINEDOT);
        if(parameterListPending()) root.addChild(parameterList());
        consume(DOTLINE);
        return root;
    }

    private Lexeme functionDefiniton(){
        log("functionDefinition");
        if(hopperFunctionPending()) return hopperFunction();
        else if(dropperFunctionPending()) return dropperFunction();
        else if(hopperDropperFunctionPending()) return hopperDropperFunction();
        else return error("Expected function call, found none");
    }

    private Lexeme hopperFunction(){
        log("hopperFunction");
        Lexeme root = consume(HOPPER);
        root.addChild(consume(IDENTIFIER));
        consume(LINEDOT);
        root.addChild(parameterList());
        consume(DOTLINE);
        consume(OCUBE);
        root.addChild(statementList());
        consume(CCUBE);
        return root;
    }

    private Lexeme dropperFunction(){
        log("dropperFunction");
        Lexeme root = consume(DROPPER);
        root.addChild(consume(IDENTIFIER));
        consume(OCUBE);
        root.addChild(statementList());
        root.addChild(returnStatement());
        consume(CCUBE);
        return root;
    }

    private Lexeme hopperDropperFunction(){
        log("hopperDropper Function");
        Lexeme root = consume(HOPPER_DROPPER);
        root.addChild(consume(IDENTIFIER));
        consume(LINEDOT);
        root.addChild(parameterList());
        consume(DOTLINE);
        consume(OCUBE);
        root.addChild(statementList());
        root.addChild(returnStatement());
        consume(CCUBE);
        return root;
    }

    private Lexeme parameterList(){
        log("parameterList");
        Lexeme root = new Lexeme(currentLexeme.getLineNumber(), PARAM_LIST);
        root.addChild(expression());
        while (check(COMMA)){
            consume(COMMA);
            root.addChild(expression());
        }
        return root;
    }

    private Lexeme conditionalStatement(){
        log("condtionalStatement");
        Lexeme root = new Lexeme(currentLexeme.getLineNumber(), CONDITIONAL_BLOCK);
        root.addChild(ifStatement());
        while(eifStatementPending()) root.addChild(eifStatement());
        if(eseStatementPending()) root.addChild(eseStatement());
        return root;
    }

    private Lexeme ifStatement(){
        log("ifStatement");
        Lexeme root = consume(IF);
        consume(LINEDOT);
        root.addChild(conditionalExpression());
        consume(DOTLINE);
        consume(OCUBE);
        root.addChild(statementList());
        consume(CCUBE);
        return root;
    }

    private Lexeme eifStatement(){
        log("eifStatement");
        Lexeme root = consume(EIF);
        consume(LINEDOT);
        root.addChild(conditionalExpression());
        consume(DOTLINE);
        consume(OCUBE);
        root.addChild(statementList());
        consume(CCUBE);
        return root;
    }

    private Lexeme eseStatement(){
        log("eseStatement");
        Lexeme root = consume(ESE);
        consume(OCUBE);
        root.addChild(statementList());
        consume(CCUBE);
        return root;
    }

    private Lexeme loop(){
        log("loop");
        if(repeaterLoopPending()) return repeaterLoop();
        else if(comparatorLoopPending()) return comparatorLoop();
        else return error("Expected loop, found none");
    }


    private Lexeme repeaterLoop(){
        log("repeaterLoop");
        inLoop = true;
        Lexeme root = consume(REPEATER);
        consume(LINEDOT);
        root.addChild(consume(INTEGER));
        consume(DOTLINE);
        consume(OCUBE);
        root.addChild(statementList());
        consume(CCUBE);
        inLoop = false;
        return root;
    }

    private Lexeme comparatorLoop(){
        log("comparatorLoop");
        Lexeme root = consume(COMPARATOR);
        consume(OCUBE);
        root.addChild(statementList());
        consume(CCUBE);
        inLoop = false;
        return root;
    }

    private Lexeme returnStatement(){
        log("returnStatement");
        Lexeme root = consume(DROP);
        root.addChild(expression());
        end();
        return root;
    }

    private Lexeme end(){
        log("end");
        // if(currentLexeme.getLineNumber() % 12 != 0) 
        return consume(REDSTONE);
        // else return consume(REPEAT);
    }

    private Lexeme booleanLiteral(){
        log("booleanLiteral");
        if(check(TRUE)) return consume(TRUE);
        else if(check(FALSE)) return consume(FALSE);
        else return error("Expected boolean literal, found none");
    }

    private Lexeme unaryAssignmentOperator(){
        log("unaryAssignmentOperator");
        if(check(PLUS_PLUS)) return consume(PLUS_PLUS);
        else if(check(MINUS_MINUS)) return consume(MINUS_MINUS);
        else return error("Expected unary assignment operator, found none");
    }

    private Lexeme unaryOperator(){
        log("unaryOperator");
        if(check(NOT)) return consume(NOT);
        else if(check(TIMES_TIMES)) return consume(TIMES_TIMES);
        else if(check(SQRT)) return consume(SQRT);
        else if(check(MOD_MOD)) return consume(MOD_MOD);
        else return error("Expected unary operator, found none");
    }

    private Lexeme naryOperator(){
        log("naryOperator");
        if(check(PLUS)) return consume(PLUS);
        else if(check(TIMES)) return consume(TIMES);
        else if(check(MINUS)) return consume(MINUS);
        else if(check(SLASH)) return consume(SLASH);
        else if(check(MOD)) return consume(MOD);
        else return error("Expected nary operator, found none");
    }

    private Lexeme naryAssignmentOperator(){
        log("naryAssignmentOperator");
        if(check(PLUS_EQUALS)) return consume(PLUS_EQUALS);
        else if(check(TIMES_EQUALS)) return consume(TIMES_EQUALS);
        else if(check(MINUS_EQUALS)) return consume(MINUS_EQUALS);
        else if(check(SLASH_EQUALS)) return consume(SLASH_EQUALS);
        else return error("Expected nary assignment operator, found none");
    }

    private Lexeme conditionalOperator(){
        log("conditionalOperator");
        if(check(EQUALITY)) return consume(EQUALITY);
        else if(check(INEQUALITY)) return consume(INEQUALITY);
        else if(check(GREATER_THAN)) return consume(GREATER_THAN);
        else if(check(LESS_THAN)) return consume(LESS_THAN);
        else if(check(GREATER_THAN_EQUALTO)) return consume(GREATER_THAN_EQUALTO);
        else if(check(LESS_THAN_EQUALTO)) return consume(LESS_THAN_EQUALTO);
        else if(check(WITHIN_EQUALITY)) return consume(WITHIN_EQUALITY);
        else return error("Expected boolean operator, found none");
    }

    private Lexeme conditionalLogicOperator(){
        log("conditionalLogOperator");
        if(check(AND)) return consume(AND);
        else if(check(OR)) return consume(OR);
        else return error("Expected logic operator, found none");
    }

    private Lexeme print(){
        log("print");
        Lexeme root = consume(PRINT);
        consume(LINEDOT);
        root.addChild(primary());
        consume(DOTLINE);
        end();
        return root;
    }

    private Lexeme mine(){
        Lexeme root = consume(MINE);
        end();
        return root;
    }


    // -------- pending functions  -------------

    private boolean statementListPending() {
        return statementPending();
    }

    private boolean statementPending(){
        return (expressionPending() || assignmentPending() || intitializationPending() || deletionPending() 
                || functionDefinitonPending() || declarationPending() || conditionalStatementPending() 
                || loopPending() || printPending() || minePending());
    }

    private boolean expressionPending(){
       return (primaryPending() || unaryExpressionPending() || functionCallPending() || naryExpressionPending() || conditionalExpressionPending());
    }

    private boolean unaryExpressionPending(){
       return (unaryOperatorPending());
    }

    private boolean unaryOperatorPending() {
        return (check(NOT) || check(TIMES_TIMES) || check(SQRT) || check(MOD_MOD));
    }

    private boolean naryExpressionPending(){
       return (naryOperatorPending() || unaryAssignmentOperatorPending() || naryAssignmentOperatorPending());

    }

    private boolean naryAssignmentOperatorPending() {
        return(check(PLUS_EQUALS) || check(SLASH_EQUALS) || check(MINUS_EQUALS) || check(TIMES_EQUALS));
    }

    private boolean conditionalExpressionPending(){
       return primaryPending() && conditonalOperatorPendingNext();
    }

    private boolean conditonalOperatorPendingNext() {
        return (checkNext(GREATER_THAN) || checkNext(LESS_THAN) || checkNext(GREATER_THAN_EQUALTO) || checkNext(LESS_THAN_EQUALTO) || checkNext(EQUALITY) || checkNext(WITHIN_EQUALITY) || checkNext(INEQUALITY));
    }

    private boolean primaryPending(){
       return(check(IDENTIFIER) || check(BOOLEAN) || check(STRING) || check(INTEGER) || check(REAL) || check(OCUBE) || functionCallPending());
    }

    private boolean assignmentPending(){
       return check(IDENTIFIER) && checkNext(ASSIGNMENT);
    }

    private boolean intitializationPending(){
       return declarationPending() && (lexemes.size() > nextLexemeIndex+1 && lexemes.get(nextLexemeIndex+1).getType() == ASSIGNMENT);
    }

    private boolean declarationPending(){
       return check(SUMMON);
    }

    private boolean deletionPending(){
       return check(KILL);
    }

    private boolean functionCallPending(){
       return (check(IDENTIFIER) && checkNext(LINEDOT));
    }

    private boolean functionDefinitonPending(){
       return (hopperFunctionPending() || dropperFunctionPending() || hopperDropperFunctionPending());
    }

    private boolean hopperFunctionPending(){
       return check(HOPPER);
    }

    private boolean dropperFunctionPending(){
       return check(DROPPER);
    }

    private boolean hopperDropperFunctionPending(){
       return check(HOPPER_DROPPER);
    }

    private boolean conditionalStatementPending(){
       return (ifStatementPending());
    }

    private boolean ifStatementPending(){
        return check(IF);
    }

    private boolean eifStatementPending(){
       return check(EIF);
    }

    private boolean eseStatementPending(){
       return check(ESE);
    }

    private boolean loopPending(){
       return (comparatorLoopPending() || repeaterLoopPending());
    }

    private boolean comparatorLoopPending(){
       return check(COMPARATOR);
    }

    private boolean repeaterLoopPending(){
        return check(REPEATER);
     } 

    private boolean booleanLiteralPending(){
       return (check(TRUE) || check(FALSE));
    }

    private boolean unaryAssignmentOperatorPending(){
       return (check(PLUS_PLUS) || check(MINUS_MINUS));
    }

    private boolean naryOperatorPending(){
        return(check(PLUS) || check(MINUS) || check(SLASH) || check(TIMES) || check(MOD));
    }

    private boolean primaryBlockPending() {
        return (primaryPending() && checkNext(CONNECTION));
    }

    private boolean naryAssignmentPending() {
        return(check(PLUS_EQUALS) || check(MINUS_EQUALS) || check(TIMES_EQUALS) || check(SLASH_EQUALS));
    }

    private boolean conditinoalLogicOperatorPending() {
        return (check(AND) || check(OR));
    }

    private boolean parameterListPending() {
        return expressionPending();
    }

    private boolean minePending(){
        return check(MINE);
    }

    private boolean printPending(){
        return check(PRINT);
    }
}
