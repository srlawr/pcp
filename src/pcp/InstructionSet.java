package pcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InstructionSet {

    private Map<Integer, String> instructions;
    
    public InstructionSet(File instructionPath) {
        loadIn(instructionPath);
        System.out.println("Loaded " + instructions.size() + " instructions.");
    }

    public String getInstruction(Integer key) {
        return instructions.get(key);
    }

    public void loadIn(File instructionPath) {

        instructions = new HashMap<Integer, String>();

        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(instructionPath))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] instruction = line.split(",");

                //System.out.println("Instruction [code= " + instruction[0] + " , command=" + instruction[1] + "]");
                instructions.put(Integer.valueOf(instruction[0]), instruction[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}