package pcp;

public class CardInstruction {

    private String instructionName;
    private Integer code;

    public CardInstruction(String name, Integer code) { 
        this.instructionName = name;
        this.code = code;
    }

    public String getInstructionName() {
        return this.instructionName;
    }

    public Integer getCode() {
        return this.code;
    }

}
