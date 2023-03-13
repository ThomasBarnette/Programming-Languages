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


    // -------- consumption functions ----------
    private void statement(){

    }

    private void expression(){

    }

    private void unaryExpression(){

    }

    private void naryExpression(){

    }

    private void conditionalExpression(){

    }

    private void primary(){

    }

    private void assignment(){

    }

    private void intitialization(){

    }

    private void declaration(){

    }

    private void deletion(){

    }

    private void functionCall(){

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
    
}
