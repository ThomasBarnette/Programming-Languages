package Enviornments;

import LexicalAnalysis.*;
import static LexicalAnalysis.Type.*;

public class EnviornmentTest {


        public static void main(String[] args){
            runTests();
        }

        private static void runTests(){
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
