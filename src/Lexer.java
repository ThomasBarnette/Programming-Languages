import java.util.ArrayList;
import java.util.HashMap;

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

    //Populating keywords
    private HashMap<String, Types> getKeywords(){
        HashMap<String, Types> keywords = new HashMap<>();
        //TODO
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
        keywords.put("name", TYPE);
    } 
     


}
