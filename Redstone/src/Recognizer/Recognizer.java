package Recognizer;

import java.util.ArrayList;
import LexicalAnalysis.Lexeme;
import LexicalAnalysis.Type;

import static LexicalAnalysis.Type.*;
import Redstone.Redstone;

public class Recognizer {
    private final ArrayList<Lexeme> lexemes;
    private Lexeme currentLexeme;
    private int nextLexemeIndex;

    private final boolean showLogs = true;

    public Recognizer(ArrayList<Lexeme> lexemes){
        this.lexemes = lexemes;
        this.nextLexemeIndex = 0;
        advance();
        program();
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

    private void error(String string) {
        Redstone.syntaxError(string, currentLexeme.getLineNumber());
    }


    // -------- consumption functions ----------
    private void program(){
        log("program");
        statementList();
    }

    private void statementList(){
        log("statementList");
        while(statementPending()) statement();
    }

    private void statement(){
        log("statement");
        if(assignmentPending()){
            assignment();
            end();
        } 
        else if(intitializationPending()){
             intitialization();
             end();
        }
        else if(declarationPending()){
             declaration();
             end();
        }
        else if(deletionPending()){
             deletion();
             end();
        }
        else if(conditionalStatementPending()) conditionalStatement();
        else if(functionDefinitonPending()) functionDefiniton();
        else if(loopPending()) loop();
        else if(expressionPending()){
            expression();
            end();
       }
        else error("Expected statement, none found");
    }

    private void expression(){
        log("expression");
        if(unaryExpressionPending()) unaryExpression();
        else if(functionCallPending()) functionCall();
        else if(naryExpressionPending()) naryExpression();
        else if(conditionalExpressionPending()) conditionalExpression();
        else if(primaryPending()) primary();
        else error("Expected expression, none found");
    }

    private void unaryExpression(){
        log("unaryExpression");
        unaryOperator();
        primary();
    }

    private void naryExpression(){
        log("naryExpression");
        if(naryOperatorPending()){
            naryOperator();
            consume(LINEDOT);
            while(primaryBlockPending()) primaryBlock();
            consume(DOTLINE);
        }
        else if(unaryAssignmentOperatorPending()){
            unaryAssignmentOperator();
            consume(IDENTIFIER);
        }
        else if(naryAssignmentPending()){
            naryAssignmentOperator();
            consume(IDENTIFIER);
            expression();
        }
        else error("Expected nary expression, found none");
    }

    private void primaryBlock() {
        log("primaryBlock");
        primary();
        consume(CONNECTION);
    }

    private void conditionalExpression(){
        log("conditionalExpression");
        primary();
        conditionalOperator();
        primary();
        if(conditinoalLogicOperatorPending()){
            conditionalLogicOperator();
            conditionalExpression();
        }
    }

    private void primary(){
        log("primary");
        if(check(STRING)) consume(STRING);
        else if(check(IDENTIFIER)) consume(IDENTIFIER);
        else if(check(REAL)) consume(REAL);
        else if(check(INTEGER)) consume(Type.INTEGER);
        else if(functionCallPending()) functionCall();
        else if(booleanLiteralPending()) booleanLiteral();
        else{
            consume(LINEDOT);
            expression();
            consume(DOTLINE);
        }
    }

    private void assignment(){
        log("assignment");
        consume(IDENTIFIER);
        consume(ASSIGNMENT);
        expression();
    }

    private void intitialization(){
        log("initialization");
        declaration();
        consume(ASSIGNMENT);
        expression();
    }

    private void declaration(){
        log("declaration");
        consume(SUMMON);
        consume(IDENTIFIER);
    }

    private void deletion(){
        log("deletion");
        consume(KILL);
        consume(IDENTIFIER);
    }

    private void functionCall(){
        log("functionCall");
        consume(IDENTIFIER);
        consume(LINEDOT);
        if(parameterListPending()) parameterList();
        consume(DOTLINE);
    }

    private void functionDefiniton(){
        log("functionDefinition");
        if(hopperFunctionPending()) hopperFunction();
        else if(dropperFunctionPending()) dropperFunction();
        else if(hopperDropperFunctionPending()) hopperDropperFunction();
        else error("Expected function call, found none");
    }

    private void hopperFunction(){
        log("hopperFunction");
        consume(HOPPER);
        consume(IDENTIFIER);
        consume(LINEDOT);
        parameterList();
        consume(DOTLINE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void dropperFunction(){
        log("dropperFunction");
        consume(DROPPER);
        consume(IDENTIFIER);
        consume(OCUBE);
        statementList();
        returnStatement();
        consume(CCUBE);
    }

    private void hopperDropperFunction(){
        log("hopperDropper Function");
        consume(HOPPER_DROPPER);
        consume(IDENTIFIER);
        consume(LINEDOT);
        parameterList();
        consume(DOTLINE);
        consume(OCUBE);
        statementList();
        returnStatement();
        consume(CCUBE);
    }

    private void parameterList(){
        log("parameterList");
        expression();
        if(check(COMMA)){
            consume(COMMA);
            parameterList();
        }
    }

    private void conditionalStatement(){
        log("condtionalStatement");
        ifStatement();
        while(eifStatementPending()) eifStatement();
        if(eseStatementPending()) eseStatement();
    }

    private void ifStatement(){
        log("ifStatement");
        consume(IF);
        consume(LINEDOT);
        conditionalExpression();
        consume(DOTLINE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void eifStatement(){
        log("eifStatement");
        consume(EIF);
        consume(LINEDOT);
        conditionalExpression();
        consume(DOTLINE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void eseStatement(){
        log("eseStatement");
        consume(ESE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void loop(){
        log("loop");
        if(repeaterLoopPending()) repeaterLoop();
        else if(comparatorLoopPending()) comparatorLoop();
        else error("Expected loop, found none");
    }


    private void repeaterLoop(){
        log("repeaterLoop");
        consume(REPEATER);
        consume(LINEDOT);
        consume(INTEGER);
        consume(DOTLINE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void comparatorLoop(){
        log("comparatorLoop");
        consume(COMPARATOR);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void returnStatement(){
        log("returnStatement");
        consume(DROP);
        expression();
        end();
    }

    private void end(){
        log("end");
        if(currentLexeme.getLineNumber() % 12 != 0) consume(REDSTONE);
        else consume(REPEAT);
    }

    private void booleanLiteral(){
        log("booleanLiteral");
        if(check(TRUE)) consume(TRUE);
        else if(check(FALSE)) consume(FALSE);
        else error("Expected boolean literal, found none");
    }

    private void unaryAssignmentOperator(){
        log("unaryAssignmentOperator");
        if(check(PLUS_PLUS)) consume(PLUS_PLUS);
        else if(check(MINUS_MINUS)) consume(MINUS_MINUS);
        else error("Expected unary assignment operator, found none");
    }

    private void unaryOperator(){
        log("unaryOperator");
        if(check(NOT)) consume(NOT);
        else if(check(TIMES_TIMES)) consume(TIMES_TIMES);
        else if(check(SQRT)) consume(SQRT);
        else if(check(MOD_MOD)) consume(MOD_MOD);
        else error("Expected unary operator, found none");
    }

    private void naryOperator(){
        log("naryOperator");
        if(check(PLUS)) consume(PLUS);
        else if(check(TIMES)) consume(TIMES);
        else if(check(MINUS)) consume(MINUS);
        else if(check(SLASH)) consume(SLASH);
        else if(check(MOD)) consume(MOD);
        else error("Expected nary operator, found none");
    }

    private void naryAssignmentOperator(){
        log("naryAssignmentOperator");
        if(check(PLUS_EQUALS)) consume(PLUS_EQUALS);
        else if(check(TIMES_EQUALS)) consume(TIMES_EQUALS);
        else if(check(MINUS_EQUALS)) consume(MINUS_EQUALS);
        else if(check(SLASH_EQUALS)) consume(SLASH_EQUALS);
        else error("Expected nary assignment operator, found none");
    }

    private void conditionalOperator(){
        log("conditionalOperator");
        if(check(EQUALITY)) consume(EQUALITY);
        else if(check(INEQUALITY)) consume(INEQUALITY);
        else if(check(GREATER_THAN)) consume(GREATER_THAN);
        else if(check(LESS_THAN)) consume(LESS_THAN);
        else if(check(GREATER_THAN_EQUALTO)) consume(GREATER_THAN_EQUALTO);
        else if(check(LESS_THAN_EQUALTO)) consume(LESS_THAN_EQUALTO);
        else if(check(WITHIN_EQUALITY)) consume(WITHIN_EQUALITY);
        else error("Expected boolean operator, found none");
    }

    private void conditionalLogicOperator(){
        log("conditionalLogOperator");
        if(check(AND)) consume(AND);
        else if(check(OR)) consume(OR);
        else error("Expected logic operator, found none");
    }


    // -------- pending functions  -------------
    private boolean statementPending(){
        return (expressionPending() || assignmentPending() || intitializationPending() || deletionPending() 
                || functionDefinitonPending() || declarationPending() || conditionalStatementPending() 
                || loopPending());
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
}
