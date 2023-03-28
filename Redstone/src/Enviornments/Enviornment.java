package Enviornments;

import java.util.*;

import LexicalAnalysis.*;
import Redstone.Redstone;

public class Enviornment {
    private final Enviornment parent;
    private final ArrayList<NamedValue> entries;
    private final int code;


    public Enviornment(Enviornment parent){
        this.parent = parent;
        this.entries = new ArrayList<>();
        code = this.hashCode();
    }

    public Enviornment(){
        this(null);
    }

    public int getCode(){
        return this.code;
    }

    //Support functions
    private Lexeme softLookup(Lexeme identifier){
        for(NamedValue namedValue : entries){
            if(namedValue.getName().equals(identifier)) return namedValue.getValue();
        }
        return null;
    }

    public Lexeme lookup(Lexeme identifier){
        Lexeme value = softLookup(identifier);
        if(value == null){
            if(parent != null) return parent.lookup(identifier);
            else error("'" + identifier.getStringValue() + "' is undefined.", identifier.getLineNumber());
        }
        return value;
    }   

    public void add(Type type, Lexeme identifier, Lexeme value){
        if(softLookup(identifier) != null){
            error("A variable with name " + "'" + identifier.getStringValue() + "' is already defined.", identifier.getLineNumber());
        }
        else{
            entries.add(new NamedValue(type, identifier));
            if(value != null) update(identifier, value);
        }
    }

    public void add(Type type, Lexeme identifier){
        add(type, identifier, null);
    }

    public void update(Lexeme identifier, Lexeme newValue){
        lookup(identifier);

        for(NamedValue namedValue : entries){
            if(namedValue.getName().equals(identifier)){
                namedValue.setValue(newValue);
                return;
            }
        }
        parent.update(identifier, newValue);
    }

    public void error(String message, int lineNumber){
        Redstone.runtimeError(message, lineNumber);
    }

    //ToString
    public String toString() {
        String end = "";
        String lines = "----------\n";
        String parent = "\n\tThis is the global enviornment. ";
        if(this.parent != null) parent = "\n\tParent: " + Integer.toString(this.parent.getCode());
        String values = "";
        for(int i = 0; i<entries.size(); i++){
            values += "\t" + entries.get(i).getName() + ": " + entries.get(i).getValue() + "\n";
        }
        end += lines + "Enviornment " + code + parent + "\n\tValues: \n" + lines + values;
        return end;
    }
}
