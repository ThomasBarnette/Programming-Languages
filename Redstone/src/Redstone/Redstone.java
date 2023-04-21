package Redstone;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import Enviornments.Enviornment;
import Interpreter.Interpreter;
import LexicalAnalysis.Lexeme;
import LexicalAnalysis.Lexer;
import Parser.Parser;

public class Redstone {
    private static final ArrayList<String> syntaxErrorMessages = new ArrayList<>();
    private static final ArrayList<String> runtimeErrorMessages = new ArrayList<>();

    private static final boolean annoyingComments = false;

    public static void main(String[] args) throws IOException{
        try{
            if(args.length == 1) runFile(args[0]);
            else{
                System.out.println("Usage: Redstone [path to .red file]");
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

        // Step 1) Turn text into list of lexemes
        Lexer lexer = new Lexer(source);
        ArrayList<Lexeme> lexemes = lexer.lex();
        // Step 2) Turn the list lexmees into a parse tree
        Parser parser = new Parser(lexemes);
        Lexeme programParseTree = parser.program();
        // Step 3) Define global enviornment
        Enviornment globalEnviornment = new Enviornment();
        //Step 4) Interpret parse tree
        Interpreter interpreter = new Interpreter();

        Lexeme programResult = interpreter.eval(programParseTree, globalEnviornment);
        System.out.println("Program Result: \n" + programResult);
        printErrors();
    }

    public static void syntaxError(String message, int lineNumber){
        syntaxErrorMessages.add(randomMotivationToCodeBetter() + "There is a syntax error" + "(line " + lineNumber +  "): " + message);
    }

    public static void syntaxError(String message, Lexeme lexeme){
        syntaxErrorMessages.add(randomMotivationToCodeBetter() + "There is a syntax error with " + lexeme + ": " + message);
    }

    public static void runtimeError(String message, int lineNumber){
        runtimeErrorMessages.add(randomMotivationToCodeBetter() + "There is a runtime error" + "(line " + lineNumber +  "): " + message);
        System.exit(65);
    }

    public static void runtimeError(String message, Lexeme lexeme){
        runtimeErrorMessages.add(randomMotivationToCodeBetter() + "There is a runtime error with " + lexeme + ": " + message);
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

    private static String randomMotivationToCodeBetter(){
        if(!annoyingComments) return "";

        int random = (int)(Math.random()*10)+1;
        if(random == 1) return "You idiot!!! ";
        if(random == 2) return "Obviously ";
        if(random == 3) return "Just switch to Scratch. You'd clearly be better at it.\n";
        if(random == 4) return "This isn't python. You actually have to try writing this language.\n";
        if(random == 5) return "I have seen ancient civilazations write code that would compile better than yours.\n";
        if(random == 6) return "I'm not mad... just dissapointed. \n";
        if(random == 7) return "*Sigh* ";
        if(random == 8) return "You will never succeed. With anything. Ever. \n";
        if(random == 9) return "You have an error, but good news! I know how to fix the issue. \n Step 1: open google \n Step 2: google \"Coding for dummies\" \n Step 3: Spend at least 30 hours learning how to code because you clearly have no idea what you're doing \n";
        if(random == 10) return "You are not worthy of controlling any device. This computer will now self destruct in 5 seconds \n";
        return "Keep it up, you're doing great :)";
    }
}
