public class Lexeme {
   private final Type type;

   private Integer lineNumber;

   private Integer intVal;
   private Double realVal;
   private Boolean booleanVal;
   private String stringVal;
   
   public Lexeme(Type type){
       this.type = type;
   }

   public Lexeme(Type type, Integer lineNumber){
        this.type = type;
        this.lineNumber = lineNumber;
    }   

    public Lexeme(Type type, Integer lineNumber, Integer intVal){
        this.type = type;
         this.lineNumber = lineNumber;
         this.intVal = intVal;
    }

    public Lexeme(Type type, Integer lineNumber, Boolean booleanVal){
        this.type = type;
         this.booleanVal = booleanVal;
    }

    public Lexeme(Type type, Integer lineNumber, Double realVal){
        this.type = type;
         this.realVal = realVal;
    }

    public Lexeme(Type type, Integer lineNumber, String stringVal){
        this.type = type;
         this.stringVal = stringVal;
    }
}
