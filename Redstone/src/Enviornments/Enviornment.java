package Enviornments;

import java.util.*;
import LexicalAnalysis.*;

public class Enviornment {
    private final Enviornment parent;
    private final ArrayList<NamedValue> entries;


    public Enviornment(Enviornment parent){
        this.parent = parent;
        this.entries = new ArrayList<>();
    }

    public Enviornment(){
        this(null);
    }
}
