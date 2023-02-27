package Redstone;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import LexicalAnalysis.Lexeme;
import LexicalAnalysis.Lexer;

public class Redstone {
    private static final ArrayList<String> syntaxErrorMessages = new ArrayList<>();
    private static final ArrayList<String> runtimeErrorMessages = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        try{
            if(args.length == 1) runFile(args[0]);
            else{
                System.out.println("Usage: Redstone [path to .arg file]");
                System.exit(64);
            }
        }
        catch (IOException exception){
            throw new IOException(exception.toString());
        }
    }

    private static String getSourceCodeFromFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String(bytes, Charset.defaultCharset());
    }

    private static void runFile(String path) throws IOException {
        System.out.println("Running" + path + "...");
        String source = getSourceCodeFromFile(path);

        //Lexing
        Lexer lexer = new Lexer(source);
        ArrayList<Lexeme> lexemes = lexer.lex();
        System.out.println(lexemes);

        printErrors();
    }

    public static void syntaxError(String message, int lineNumber){
        syntaxErrorMessages.add("Syntax error" + "(line " + lineNumber +  "): " + message);
    }

    public static void syntaxError(String message, Lexeme lexeme){
        syntaxErrorMessages.add("Syntax error at " + lexeme + ": " + message);
    }

    public static void runtimeError(String message, int lineNumber){
        syntaxErrorMessages.add("Runtime error" + "(line " + lineNumber +  "): " + message);
        System.exit(65);
    }

    public static void runtimeError(String message, Lexeme lexeme){
        syntaxErrorMessages.add("Runtime error at " + lexeme + ": " + message);
        System.exit(65);
    }

    private static void printErrors(){
        final String YELLOW = "\u001B[33m";
        final String RED = "\u001B[41m";
        final String RESET = "\u001B[0m";
        
        for(String syntaxErrorMessage : syntaxErrorMessages){
            System.out.println(YELLOW + syntaxErrorMessage + RESET);
        }

        for(String runtimeErrorMessage : runtimeErrorMessages){
            System.out.println(RED + runtimeErrorMessage + RESET);
        }
    }
}
