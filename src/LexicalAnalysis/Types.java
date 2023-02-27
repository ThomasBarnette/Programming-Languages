package LexicalAnalysis;
public enum Types{
     //Keywords
     SUMMON, KILL, REPEAT,

     //Looping
     REPEATER, COMPARATOR,

     //Condtionals
     IF, EIF, ESE,
     TRUE, FALSE,

    //Functions
    HOPPER, DROPPER, HOPPER_DROPPER,
     
     //Single character tokens
     OCUBE, CCUBE, LINEDOT, DOTLINE,
     REDSTONE,
    
     //Oerators
     PLUS, MINUS, TIMES, SLASH, SQRT, TIMES_TIMES, PLUS_EQUALS, MINUS_EQUALS, TIMES_EQUALS, 
     SLASH_EQUALS, PLUS_PLUS, MINUS_MINUS, MOD, MOD_MOD, 

     AND, OR, NOT, XOR, NOR, NAND, XAND,

     //Conditional Operators
     EQUALITY, WITHIN_EQUALITY, LESS_THAN, GREATER_THAN, LESS_THAN_EQUALTO, GREATER_THAN_EQUALTO,
}