package LexicalAnalysis;
public class Lexeme {
    //Instance Variables
    private final Types type;
    private int lineNumber;

    //------Instance Variable Declaration------
    private String stringValue;
    private Double realValue;
    private Integer intValue;
    private Boolean boolValue;

    //-----------Constructors-------------
    public Lexeme(int lineNumber, Types type) {
        this.lineNumber = lineNumber;
        this.type = type;
    }

    public Lexeme(int lineNumber, String stringValue, Types type) {
        this.lineNumber = lineNumber;
        this.stringValue = stringValue;
        this.type = type;
    }

     public Lexeme(int lineNumber, Double realValue, Types type) {
        this.lineNumber = lineNumber;
        this.realValue = realValue;
        this.type = type;
    }

    public Lexeme(int lineNumber, int intValue, Types type) {
        this.lineNumber = lineNumber;
        this.intValue = intValue;
        this.type = type;
    }

    public Lexeme(int lineNumber, boolean boolValue, Types type) {
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

    public Types getType() { return type; }

    public void setIntValue(int intValue) { this.intValue = intValue; }

    public void setBoolValue(boolean boolValue) { this.boolValue = boolValue; }

    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }

    public void setWord(String stringValue) {  this.stringValue = stringValue; }

    //--------toString-----------
    public String toString() {
        return ("Type" + type +
        "\nline number " + lineNumber +
        "\nBoolean value: " + boolValue +
        "\nInteger Value: " + intValue +
        "\nString value: " + stringValue);
    }
}