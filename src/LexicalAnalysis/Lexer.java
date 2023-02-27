package LexicalAnalysis;
import java.util.ArrayList;
import java.util.HashMap;

import Redstone.Redstone;

import static LexicalAnalysis.Types.*;

public class Lexer {
    private final String source;
    private final ArrayList<Lexeme> lexemes;
    private final HashMap<String, Types> keywords;

    private int currentPosition = 0;
    private int startOfCurrentLexeme = 0;
    private int lineNumber = 1;

    //constructor
    public Lexer(String source){
        this.source = source;
        this.lexemes = new ArrayList<>();
        this.keywords = getKeywords();
        this.currentPosition = 0;
        this.startOfCurrentLexeme = 0;
        this.lineNumber = 0;
    }

    private boolean isAtEnd(){
        return currentPosition >= source.length();
    }

    private char peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(currentPosition);
    }

    private char peekNext(){
        if(currentPosition + 1 >= source.length()) return '\0';
        return source.charAt(currentPosition + 1);
    }

    private boolean match(char expected){
        if(isAtEnd() || source.charAt(currentPosition) != expected) return false;
        currentPosition++;
        return true;
    }

    private char advance(){
        char currentChar = source.charAt(currentPosition);
        if(currentChar == '\n' || currentChar == '\r') lineNumber++;
        currentPosition++;
        return currentChar;
    }


    //Helper methods

    private boolean isDigit(char c){
        return(c >= '0' || c <= '9');
    }

    private boolean isAlpha(char c){
        return( c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_');
    }

    private boolean isAlphaNumeric(char c){
        return isAlpha(c) || isDigit(c);
    }

    //error reporting
    private void error(String message){
        Redstone.syntaxError(message, lineNumber);
    }

    //TODO
    public ArrayList<Lexeme> Lex(){
        return null;
    }

    //Populating keywords
    private HashMap<String, Types> getKeywords(){
        HashMap<String, Types> keywords = new HashMap<>();
        keywords.put("/summon", SUMMON);
        keywords.put("/kill", KILL);
        keywords.put("repeater", REPEAT);
        keywords.put("repeater", REPEATER);
        keywords.put("comparator", COMPARATOR);
        keywords.put("if", IF);
        keywords.put("eif", EIF);
        keywords.put("ese", ESE);
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("hopper", HOPPER);
        keywords.put("dropper", DROPPER);
        keywords.put("hopper dropper", HOPPER_DROPPER);
        return keywords;
    } 
}
