package Enviornments;

import java.util.*;
import LexicalAnalysis.*;
import Redstone.Redstone;

import static LexicalAnalysis.Type.*;

public class Environment {
    private final Environment parent;
    private final ArrayList<NamedValue> entries;
    private final int code;


    public Environment(Environment parent){
        this.parent = parent;
        this.entries = new ArrayList<>();
        code = this.hashCode();
    }

    public Environment(){
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

    public void extend(Lexeme names, Lexeme vals){
        if(names == null && vals == null) return;
        else if (names == null || vals == null){
            error("Unequal number of parameters and arguments", 0);
            return;
        }
        if(names.getChildren().size() != vals.getChildren().size()) error("Trying to match a statement of type '" + names.getType() + "'' with a '" + vals.getType() + ".' However the sizes of these lists do not match", -1);
        ArrayList<Lexeme> nameList = new ArrayList<>();
        for(Lexeme child : names.getChildren()) nameList.add(child);
        ArrayList<Lexeme> valList = new ArrayList<>();
        for(Lexeme child : vals.getChildren()) valList.add(child);

        for(int i = 0; i < nameList.size(); i++){
            Type type = valList.get(i).getType();
            Lexeme value = null;
            if(type == INTEGER) value = new Lexeme(valList.get(i).getLineNumber(), valList.get(i).getIntValue(), INTEGER);
            if(type == REAL) value = new Lexeme(valList.get(i).getLineNumber(), valList.get(i).getRealValue(), REAL);
            if(type == STRING) value = new Lexeme(valList.get(i).getLineNumber(), valList.get(i).getStringValue(), STRING);
            if(type == BOOLEAN) value = new Lexeme(valList.get(i).getLineNumber(), valList.get(i).getBoolValue(), BOOLEAN);

            add(valList.get(i).getType(), nameList.get(i), value);
        }
    }

    //ToString
    public String toString() {
        String end = "";
        String lines = "----------\n";
        String parent = "\n\tThis is the global environment. ";
        if(this.parent != null) parent = "\n\tParent: " + Integer.toString(this.parent.getCode());
        String values = "";

        for(int i = 0; i<entries.size(); i++) values += "\t" + entries.get(i).getName().getStringValue() + ": " + entries.get(i).getValue().toValueOnlyString() + "\n";

        if(this.parent != null) end += this.parent.toString();
        
        return end + lines + "Enviornment " + code + parent + "\n\n\tValues: \n\t" + lines + values;
    }
}
