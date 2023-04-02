package Redstone;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import Enviornments.Enviornment;
import LexicalAnalysis.Lexeme;
import LexicalAnalysis.Lexer;
import LexicalAnalysis.Type;
import static LexicalAnalysis.Type.*;
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

        //Lexing
        Lexer lexer = new Lexer(source);
        ArrayList<Lexeme> lexemes = lexer.lex();
        // Parsing
        Parser parser = new Parser(lexemes);
        Lexeme programParseTree = parser.program();
        // programParseTree.printAsParseTree();

        //TODO delete this later - testing enviornments
        testEnviornment();


        printErrors();
    }

    public static void syntaxError(String message, int lineNumber){
        syntaxErrorMessages.add(randomMotivationToCodeBetter() + "There is a syntax error" + "(line " + lineNumber +  "): " + message);
    }

    public static void syntaxError(String message, Lexeme lexeme){
        syntaxErrorMessages.add(randomMotivationToCodeBetter() + "There is a syntax error with " + lexeme + ": " + message);
    }

    public static void runtimeError(String message, int lineNumber){
        syntaxErrorMessages.add(randomMotivationToCodeBetter() + "There is a runtime error" + "(line " + lineNumber +  "): " + message);
        System.exit(65);
    }

    public static void runtimeError(String message, Lexeme lexeme){
        syntaxErrorMessages.add(randomMotivationToCodeBetter() + "There is a runtime error with " + lexeme + ": " + message);
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

    //Testing enviornments
    public static void testEnviornment(){
        //Pre defined lexemes
        Lexeme testString = new Lexeme(0, "test", STRING);
        Lexeme testInt = new Lexeme(0, 5, INTEGER);
        Lexeme testReal = new Lexeme(0, 1.3, REAL);

        Lexeme identifier1 = new Lexeme(0, "x", IDENTIFIER);
        Lexeme identifier2 = new Lexeme(0, "b", IDENTIFIER);
        Lexeme identifier3 = new Lexeme(0, "test", IDENTIFIER);

        //Global enviornment
        Enviornment global = new Enviornment();
        global.add(STRING, identifier1, testString);

        Enviornment child1 = new Enviornment(global);
        child1.add(INTEGER, identifier2, testInt);
        child1.add(REAL, identifier1, testReal);

        Enviornment grandchild = new Enviornment(child1);
        grandchild.add(INTEGER, identifier3, testInt);

        Enviornment child2 = new Enviornment(global);
        child2.add(INTEGER, identifier1, testInt);

        System.out.println(grandchild);
    }
}
