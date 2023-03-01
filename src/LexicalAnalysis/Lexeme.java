package LexicalAnalysis;
public class Lexeme {
    //Instance Variables
    private final Type type;
    private int lineNumber;

    //------Instance Variable Declaration------
    private String stringValue;
    private Double realValue;
    private Integer intValue;
    private Boolean boolValue;

    //-----------Constructors-------------
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

    //--------toString-----------
    public String toString() {
        return ("\nType " + type +
        " on line number " + lineNumber +
        "\nBoolean value: " + boolValue +
        "\nInteger Value: " + intValue +
        "\nReal Value: " + realValue +
        "\nString value: " + stringValue);
    }
}