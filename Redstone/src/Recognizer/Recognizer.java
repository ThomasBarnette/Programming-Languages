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
    private void statementPending(){

    }

    private void expressionPending(){

    }

    private void unaryExpressionPending(){

    }

    private void naryExpressionPending(){

    }

    private void conditionalExpressionPending(){

    }

    private void primaryPending(){

    }

    private void assignmentPending(){

    }

    private void intitializationPending(){

    }

    private void declarationPending(){

    }

    private void deletionPending(){

    }

    private void functionCallPending(){

    }

    private void functionDefinitonPending(){

    }

    private void hopperFunctionPending(){

    }

    private void dropperFunctionPending(){

    }

    private void hopperDropperFunctionPending(){

    }

    private void parameterListPending(){

    }

    private void conditionalStatementPending(){

    }

    private void ifStatementPending(){

    }

    private void eifStatementPending(){

    }

    private void eseStatementPending(){

    }

    private void loopPending(){

    }

    private void comparatorLoopPending(){

    }

    private void whileLoopPending(){

    }

    private void returnStatementPending(){

    }

    private void endPending(){

    }

    private void booleanLiteralPending(){

    }

    private void unaryAssignmentOperatorPending(){

    }

    private void unaryOperatorPending(){

    }

    private void naryOperatorPending(){

    }

    private void conditionalOperatorPending(){
        
    }
    
}
