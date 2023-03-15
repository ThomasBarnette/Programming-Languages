package Recognizer;

import java.lang.Thread.State;
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
 
    private void statement(){
        if(expressionPending()) expression();
        else if(assignmentPending()) assignment();
        else if(intitializationPending()) intitialization();
        else if(declarationPending()) declaration();
        else if(deletionPending()) deletion();
        else if(conditionalStatementPending()) conditionalStatement();
        else if(loopPending()) loop();
        // else error("Expected statement, none found");
    }

    private void expression(){
        if(unaryExpressionPending()) unaryExpression();
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
        naryOperator();
        consume(LINEDOT);
        while(primaryBlockPending()) primaryBlock();
        consume(DOTLINE);
    }

    private void primaryBlock() {
        primary();
        consume(CONNECTION);
    }

    private void conditionalExpression(){
        primary();
        conditionalOperator();
        primary();
    }

    private void primary(){
        if(check(STRING)) consume(STRING);
        else if(check(IDENTIFIER)) consume(IDENTIFIER);
        else if(check(REAL)) consume(REAL);
        else if(check(INTEGER)) consume(Type.INTEGER);
        else if(functionCallPending()) functionCall();
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
        consume(OCUBE);
        parameterList();
        consume(CCUBE);

    }

    private void functionDefiniton(){

    }

    private void hopperFunction(){

    }

    private void dropperFunction(){

    }

    private void hopperDropperFunction(){

    }

    private void parameterList(){

    }

    private void conditionalStatement(){

    }

    private void ifStatement(){

    }

    private void eifStatement(){

    }

    private void eseStatement(){

    }

    private void loop(){

    }

    private void comparatorLoop(){

    }

    private void whileLoop(){

    }

    private void returnStatement(){

    }

    private void end(){

    }

    private void booleanLiteral(){

    }

    private void unaryAssignmentOperator(){

    }

    private void unaryOperator(){

    }

    private void naryOperator(){

    }

    private void conditionalOperator(){

    }


    // -------- pending functions  -------------
    private boolean statementPending(){
        //TODO
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

    private boolean parameterListPending(){
       //TODO
       return false;
    }

    private boolean conditionalStatementPending(){
       //TODO
       return false;
    }

    private boolean ifStatementPending(){
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

    private boolean whileLoopPending(){
       //TODO
       return false;
    }

    private boolean returnStatementPending(){
       //TODO
       return false;
    }

    private boolean endPending(){
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

    private boolean unaryOperatorPending(){
       //TODO
       return false;
    }

    private boolean naryOperatorPending(){
       //TODO
       return false;
    }

    private boolean conditionalOperatorPending(){
        //TODO
        return false;
    }

    private boolean primaryBlockPending() {
        return false;
    }
    
}
