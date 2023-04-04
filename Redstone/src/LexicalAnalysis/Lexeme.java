package LexicalAnalysis;

import java.util.*;

public class Lexeme {
    //Instance Variables
    private final Type type;
    private int lineNumber;

    //------Instance Variable Declaration------
    private String stringValue;
    private Double realValue;
    private Integer intValue;
    private Boolean boolValue;
    private ArrayList<Lexeme> children = new ArrayList<>();

    //-----------Constructors-------------
    public Lexeme(){
        this.type = null;
    }

    public Lexeme(Type type){
        this.type = type;
    }

    public Lexeme(int lineNumber, Type type) {
        this.lineNumber = lineNumber;
        this.type = type;
    }

    public Lexeme(int lineNumber, String stringValue, Type type) {
        this.lineNumber = lineNumber;
        this.stringValue = stringValue;
        this.type = type;
    }

     public Lexeme(int lineNumber, Double realValue, Type type) {
        this.lineNumber = lineNumber;
        this.realValue = realValue;
        this.type = type;
    }

    public Lexeme(int lineNumber, int intValue, Type type) {
        this.lineNumber = lineNumber;
        this.intValue = intValue;
        this.type = type;
    }

    public Lexeme(int lineNumber, boolean boolValue, Type type) {
        this.lineNumber = lineNumber;
        this.boolValue = boolValue;
        this.type = type;
    }

    //---------Getters and Setters------------
    public Integer getIntValue() { return intValue; }

    public Double getRealValue() { return realValue; }

    public Boolean getBoolValue() { return boolValue; }

    public Integer getLineNumber() { return lineNumber; }

    public String getStringValue() { return stringValue; }

    public Type getType() { return type; }

    public void setIntValue(int intValue) { this.intValue = intValue; }

    public void setBoolValue(boolean boolValue) { this.boolValue = boolValue; }

    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }

    public void setString(String stringValue) {  this.stringValue = stringValue; }

    public void addChild(Lexeme child) { this.children.add(child); }

    public void addAll(ArrayList<Lexeme> childeren) { this.children.addAll(childeren); }

    public Lexeme getChild(int index){ return children.get(index); }

    public ArrayList<Lexeme> getChildren(){ return this.children; }


    //--------toString-----------
    public String toString() {
        String value = "EMPTY";
        if(intValue != null) value = intValue.toString();
        if(realValue != null) value = realValue.toString();
        if(boolValue != null) value = boolValue.toString();
        if(stringValue != null) value = stringValue;
        return !value.equals("EMPTY") ? ("Type " + type +
        " on line number " + lineNumber +
        " (value: " + value + ")") : ("Type " + type +
        " on line number " + lineNumber);
    }

    public String toValueOnlyString(){
        String value = "EMPTY";
        if(intValue != null) value = intValue.toString();
        if(realValue != null) value = realValue.toString();
        if(boolValue != null) value = boolValue.toString();
        if(stringValue != null) value = stringValue;
        return value;
    }

    public boolean equals(Lexeme other){
        if(type != other.getType()) return false;;
        if(realValue != null && realValue.equals(other.realValue)) return true;
        if(stringValue != null && stringValue.equals(other.stringValue)) return true;
        if(intValue != null && intValue.equals(other.intValue)) return true;
        return(boolValue != null && boolValue.equals(other.boolValue));
    }

    //Printing parse trees
    public void printAsParseTree() {
        System.out.println(getPrintableTree(this, 0));
     }
     
     private static String getPrintableTree(Lexeme root, int level) {
        if (root == null) return "(Empty ParseTree)";
        StringBuilder treeString = new StringBuilder(root.toString());     

        StringBuilder spacer = new StringBuilder("\n");
        spacer.append("\t".repeat(level)); 

        int numChildren = root.children.size();  
        if (numChildren > 0) {
            treeString.append(" (with ").append(numChildren).append(numChildren == 1 ? " child):" : " children):");
            for (int i = 0; i < numChildren; i++) {
                Lexeme child = root.children.get(i);
                treeString
                        .append(spacer).append("(").append(i + 1).append(") ")
                        .append(getPrintableTree(child, level + 1));
            }
        }
        return treeString.toString();
     }
}