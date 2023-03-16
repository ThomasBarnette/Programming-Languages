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

    public Recognizer(ArrayList<Lexeme> lexemes){
        this.lexemes = lexemes;
        this.nextLexemeIndex = 0;
        advance();
    }

    private Type peek(){
        return currentLexeme.getType();
    }

    private Type peekNext(){
        if(nextLexemeIndex > lexemes.size()) return null;
        return lexemes.get(nextLexemeIndex).getType();
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

        Redstone.syntaxError("Expected " + expected + " instead found " + currentLexeme, currentLexeme.getLineNumber());
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
        statementList();
    }

    private void statementList(){
        while(statementPending()) statement();
    }

    private void statement(){
        if(expressionPending()){
             expression();
             end();
        }
        else if(assignmentPending()){
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
        // else error("Expected statement, none found");
    }

    private void expression(){
        if(unaryExpressionPending()) unaryExpression();
        else if(functionCallPending()) functionCall();
        else if(naryExpressionPending()) naryExpression();
        else if(conditionalExpressionPending()) conditionalExpression();
        else if(primaryPending()) primary();
        // else error("Expected expression, none found");
    }

    private void unaryExpression(){
        unaryOperator();
        primary();
    }

    private void naryExpression(){
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
            consume(IDENTIFIER);
            naryAssignmentOperator();
            expression();
        }
        else error("Expected nary expression, found none");
    }

    private void primaryBlock() {
        primary();
        consume(CONNECTION);
    }

    private void conditionalExpression(){
        primary();
        conditionalOperator();
        primary();
        if(conditinoalLogicOperatorPending()){
            conditionalLogicOperator();
            conditionalExpression();
        }
    }

    private void primary(){
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
        consume(IDENTIFIER);
        consume(ASSIGNMENT);
        expression();
    }

    private void intitialization(){
        declaration();
        consume(ASSIGNMENT);
        expression();
    }

    private void declaration(){
        consume(SUMMON);
        consume(IDENTIFIER);
    }

    private void deletion(){
        consume(KILL);
        consume(IDENTIFIER);
    }

    private void functionCall(){
        consume(IDENTIFIER);
        consume(LINEDOT);
        parameterList();
        consume(DOTLINE);
    }

    private void functionDefiniton(){
        if(hopperFunctionPending()) hopperFunction();
        else if(dropperFunctionPending()) dropperFunction();
        else if(hopperDropperFunctionPending()) hopperDropperFunction();
        else error("Expected function call, found none");
    }

    private void hopperFunction(){
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
        consume(DROPPER);
        consume(IDENTIFIER);
        consume(LINEDOT);
        consume(DOTLINE);
        consume(OCUBE);
        returnStatement();
        consume(CCUBE);
    }

    private void hopperDropperFunction(){
        consume(HOPPER);
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
        expression();
        if(peek() == COMMA){
            consume(COMMA);
            parameterList();
        }
    }

    private void conditionalStatement(){
        ifStatement();
        while(eifStatementPending()) eifStatement();
        if(eseStatementPending()) eseStatement();
    }

    private void ifStatement(){
        consume(IF);
        consume(LINEDOT);
        conditionalExpression();
        consume(DOTLINE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void eifStatement(){
        consume(EIF);
        consume(LINEDOT);
        conditionalExpression();
        consume(DOTLINE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void eseStatement(){
        consume(ESE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void loop(){
        if(repeaterLoopPending()) repeaterLoop();
        else if(comparatorLoopPending()) comparatorLoop();
        else error("Expected loop, found none");
    }


    private void repeaterLoop(){
        consume(REPEAT);
        consume(LINEDOT);
        consume(INTEGER);
        consume(DOTLINE);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void comparatorLoop(){
        consume(COMPARATOR);
        consume(OCUBE);
        statementList();
        consume(CCUBE);
    }

    private void returnStatement(){
        consume(DROP);
        expression();
    }

    private void end(){
        if(currentLexeme.getLineNumber() % 12 != 0) consume(REDSTONE);
        else consume(REPEAT);
    }

    private void booleanLiteral(){
        if(peek() == TRUE) consume(TRUE);
        else if(peek() == FALSE) consume(FALSE);
        else error("Expected boolean literal, found none");
    }

    private void unaryAssignmentOperator(){
        if(peek() == PLUS_PLUS) consume(PLUS_PLUS);
        else if(peek() == MINUS_MINUS) consume(MINUS_MINUS);
        else error("Expected unary assignment operator, found none");
    }

    private void unaryOperator(){
        if(peek() == NOT) consume(NOT);
        else if(peek() == TIMES_TIMES) consume(TIMES_TIMES);
        else if(peek() == SQRT) consume(SQRT);
        else if(peek() == MOD_MOD) consume(MOD_MOD);
        else error("Expected unary operator, found none");
    }

    private void naryOperator(){
        if(peek() == PLUS) consume(PLUS);
        else if(peek() == TIMES) consume(TIMES);
        else if(peek() == MINUS) consume(MINUS);
        else if(peek() == SLASH) consume(SLASH);
        else if(peek() == MOD) consume(MOD);
        else error("Expected nary operator, found none");
    }

    private void naryAssignmentOperator(){
        if(peek() == PLUS_EQUALS) consume(PLUS_EQUALS);
        else if(peek() == TIMES_EQUALS) consume(TIMES_EQUALS);
        else if(peek() == MINUS_EQUALS) consume(MINUS_EQUALS);
        else if(peek() == SLASH_EQUALS) consume(SLASH_EQUALS);
        else error("Expected nary assignment operator, found none");
    }

    private void conditionalOperator(){
        if(peek() == EQUALITY) consume(EQUALITY);
        else if(peek() == INEQUALITY) consume(INEQUALITY);
        else if(peek() == GREATER_THAN) consume(GREATER_THAN);
        else if(peek() == LESS_THAN) consume(LESS_THAN);
        else if(peek() == GREATER_THAN_EQUALTO) consume(GREATER_THAN_EQUALTO);
        else if(peek() == LESS_THAN_EQUALTO) consume(LESS_THAN_EQUALTO);
        else if(peek() == WITHIN_EQUALITY) consume(WITHIN_EQUALITY);
        else error("Expected boolean operator, found none");
    }

    private void conditionalLogicOperator(){
        if(peek() == AND) consume(AND);
        else if(peek() == OR) consume(OR);
        else error("Expected logic operator, found none");
    }


    // -------- pending functions  -------------
    private boolean statementPending(){
        return false;
    }

    private boolean expressionPending(){
       //TODO
       return false;
    }

    private boolean unaryExpressionPending(){
       //TODO
       return false;
    }

    private boolean naryExpressionPending(){
       //TODO
       return false;
    }

    private boolean conditionalExpressionPending(){
       //TODO
       return false;
    }

    private boolean primaryPending(){
       //TODO
       return false;
    }

    private boolean assignmentPending(){
       //TODO
       return false;
    }

    private boolean intitializationPending(){
       //TODO
       return false;
    }

    private boolean declarationPending(){
       //TODO
       return false;
    }

    private boolean deletionPending(){
       //TODO
       return false;
    }

    private boolean functionCallPending(){
       //TODO
       return false;
    }

    private boolean functionDefinitonPending(){
       //TODO
       return false;
    }

    private boolean hopperFunctionPending(){
       //TODO
       return false;
    }

    private boolean dropperFunctionPending(){
       //TODO
       return false;
    }

    private boolean hopperDropperFunctionPending(){
       //TODO
       return false;
    }

    private boolean conditionalStatementPending(){
       //TODO
       return false;
    }

    private boolean eifStatementPending(){
       //TODO
       return false;
    }

    private boolean eseStatementPending(){
       //TODO
       return false;
    }

    private boolean loopPending(){
       //TODO
       return false;
    }

    private boolean comparatorLoopPending(){
       //TODO
       return false;
    }

    private boolean repeaterLoopPending(){
        //TODO
        return false;
     } 

    private boolean booleanLiteralPending(){
       //TODO
       return false;
    }

    private boolean unaryAssignmentOperatorPending(){
       //TODO
       return false;
    }

    private boolean naryOperatorPending(){
       //TODO
       return false;
    }

    private boolean primaryBlockPending() {
        return false;
    }

    private boolean naryAssignmentPending() {
        return false;
    }

    private boolean conditinoalLogicOperatorPending() {
        return false;
    }
}
