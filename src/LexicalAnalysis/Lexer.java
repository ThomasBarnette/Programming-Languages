package LexicalAnalysis;
import java.util.ArrayList;
import java.util.HashMap;

import Redstone.Redstone;

import static LexicalAnalysis.Type.*;

public class Lexer {
    private final String source;
    private final ArrayList<Lexeme> lexemes;
    private final HashMap<String, Type> keywords;

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
        this.lineNumber = 1;
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
        return(c >= '0' && c <= '9');
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

    public ArrayList<Lexeme> lex(){
        while(!isAtEnd()) {
            startOfCurrentLexeme = currentPosition;
            Lexeme nextLexeme = getNextLexeme();
            if(nextLexeme != null) lexemes.add(nextLexeme);
        }
        lexemes.add(new Lexeme(lineNumber, EOF));
        return lexemes;
    }

    private Lexeme getNextLexeme() {
        char c = advance();
        switch(c){
            //Eliminate whitespace
            case '\r':
            case '\t':
            case '\n':
            case ' ':
                return null;
            //Single character tokens
            case '[':
                return new Lexeme(lineNumber, OCUBE);
            case ']':
                return new Lexeme(lineNumber, CCUBE);
            case '܍':
                return new Lexeme(lineNumber, REDSTONE);
            case '=':
                return new Lexeme(lineNumber, ASSIGNMENT);
            case '!':
                return new Lexeme(lineNumber, NOT);
            case '|':
                if(match('.')) return new Lexeme(lineNumber, LINEDOT);
                else Redstone.syntaxError("Invalid token \"|\" . Did you mean \"|.\"?", lineNumber);
            case '.':
                if(match('|')) return new Lexeme(lineNumber, DOTLINE);
                else Redstone.syntaxError("Invalid token \".\" . Did you mean \".|\"?", lineNumber);
            case '*':
                if(match('*')) return new Lexeme(lineNumber, TIMES_TIMES);
                else if(match('=')) return new Lexeme(lineNumber, TIMES_EQUALS);
                else return new Lexeme(lineNumber, TIMES);
            case '/':
                if(match('=')) return new Lexeme(lineNumber, SLASH_EQUALS);
                else if(match('.') && peekNext() == '/') return new Lexeme(lineNumber, SQRT);
                // How to distinguish / between longer keywords like /summon?
                else return new Lexeme(lineNumber, SLASH);
            case '%':
                return match('%')? new Lexeme(lineNumber, MOD_MOD) : new Lexeme(lineNumber, MOD);
            case '+':
                if(match('+')) return new Lexeme(lineNumber, PLUS_PLUS);
                else if(match('=')) return new Lexeme(lineNumber, PLUS_EQUALS);
                else return new Lexeme(lineNumber, PLUS);
            case '-':
                if(match('-')) return new Lexeme(lineNumber, MINUS_MINUS);
                else if(match('=')) return new Lexeme(lineNumber, MINUS_EQUALS);
                else return new Lexeme(lineNumber, MINUS);
            case '<':
                if(match('>')) return new Lexeme(lineNumber, EQUALITY);
                else if(match('=')) return new Lexeme(lineNumber, LESS_THAN_EQUALTO);
                else return new Lexeme(lineNumber, LESS_THAN);
            case '>':
                if(match('<')) return new Lexeme(lineNumber, INEQUALITY);
                else if(match('=')) return new Lexeme(lineNumber, GREATER_THAN_EQUALTO);
                else return new Lexeme(lineNumber, GREATER_THAN);
            case '"':
                return lexString();
            default:
                if(isDigit(c)) return lexNumber();
                if(isAlpha(c)) return lexIdentifierOrKeyword();
                else Redstone.syntaxError("Unexpected token on \" " + c + "\"", lineNumber);;
        }
        return null;
    }

    private Lexeme lexIdentifierOrKeyword() {
        while(isAlphaNumeric(peek())) advance();
        String text = source.substring(startOfCurrentLexeme, currentPosition);

        Type type = keywords.get(text);
        if(type == null) return new Lexeme(lineNumber, text, IDENTIFIER);
        else return new Lexeme(lineNumber, type);
    }

    private Lexeme lexNumber() {
        boolean isInteger = true;
        while(isDigit(peek())) advance();

        //Checking if real or double
        if(peek() == '.'){
            if(!isDigit(peekNext())) Redstone.syntaxError("Expecting number following decimal point", lineNumber);
            isInteger = false;
            advance();
            while(isDigit(peek())) advance();
        }

        String numberString = source.substring(startOfCurrentLexeme, currentPosition);
        if(isInteger){
            int number = Integer.parseInt(numberString);
            return new Lexeme(lineNumber, number, INTEGER);
        } else {
            double number = Double.parseDouble(numberString);
            return new Lexeme(lineNumber, number, REAL);
        }
    }

    private Lexeme lexString() {
       while(!isAtEnd() || peek() != '"') advance();
       if(isAtEnd()) Redstone.syntaxError("Unclosed String", lineNumber);
       String string = source.substring(startOfCurrentLexeme, currentPosition-1);
       return new Lexeme(lineNumber, string, STRING);
    }

    //Populating keywords
    private HashMap<String, Type> getKeywords(){
        HashMap<String, Type> keywords = new HashMap<>();
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
        keywords.put("End of File", EOF);
        keywords.put("and", AND);
        keywords.put("or", OR);
        return keywords;
    } 
}