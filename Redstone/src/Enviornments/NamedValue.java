package Enviornments;

import LexicalAnalysis.*;

public class NamedValue {
    private Lexeme name;
    private Lexeme value;
    private Type type;

    public NamedValue(Type type, Lexeme name){
        this.name = name;
        this.type = type;
        value = new Lexeme();
    }

    public Lexeme getName(){ return name; }

    public Lexeme getValue(){ return value; }

    public Type getType(){ return this.type; }

    public void setValue(Lexeme val){ this.value = val; }


    public String toString(){
        return name.getStringValue() + ": " + value.toValueOnlyString();
    }

}
