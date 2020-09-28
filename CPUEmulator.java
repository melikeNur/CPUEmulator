//cpuemulator;

import java.io.*;
import java.util.*;


public class CPUEmulator {
    public int AX;
    public int AC;
    public int X;
    public int PC;
    public int [] M =new int [256];
    public int fFlag=0;
    Stack<Integer> stack = new Stack<>();
    public volatile Boolean halted = false;
   
    public Boolean debug;
    
    public HashMap<Integer, Instruction> instructionMemory;
    public HashMap<String, Runnable> instructionTypes;
    
    public static class Instruction {
        public String command;
        public int parameter;
        public Instruction(String c, int p) {
            command = c;
            parameter = p;
        }
    }
    
    public CPUEmulator() {
        X = 0;
        AC = 0;
        AX = 0;
        PC = 0;
        fFlag = 0;
        M = new int[256];
        stack = new Stack<>();
        instructionMemory = new HashMap<Integer, Instruction>();
        debug = false;
    }
    
    public void run() {
        while(!halted) {
            Instruction ins = instructionMemory.get(PC);
            if(ins != null && debug) System.out.println(ins.command + " " + ins.parameter);            
            if(ins != null) {
                X = ins.parameter;
				switch(ins.command) {
					case "START":
						START();
						break;
					case "PUSH":
						PUSH();
						break;	
					case "POP":
						POP();
						break;
				    case "RETURN":
						RETURN();
						break;
					case "LOADI":
						LOADI();
						break;
					case "STOREI":
						STOREI();
						break;
					case "SWAP":
						SWAP();
						break;
					case "LOAD":
						LOAD();
						break;
					case "LOADM":
						LOADM();
						break;
					case "STORE":
						STORE();
						break;
					case "CMPM":
						CMPM();
						break;
					case "CJMP":
						CJMP();
						break;
					case "JMP":
						JMP();
						break;
					case "ADD":
						ADD();
						break;
					case "ADDMM":
						ADDMM();
						break;
					case "SUBMM":
						SUBMM();
						break;
					case "SUB":
						SUB();
						break;
					case "MULN":
						MULN();
						break;
					case "MULMN":
						MULMN();
						break;
					case "DISP":
						DISP();
						break;
					case "DASC":
						DASC();
						break;
					case "HALT":
						HALT();
						break;
					default:
						break;
				};
            }
            if(debug)
            System.out.println("PC: " + PC + " AC: " + AC + " AX: " + AX + " M[1]:" + M[1] + " M[2]:" + M[2] + " M[3]:" 
                    + M[3] + " M[4]:" + M[4] + " M[5]:" + M[5] + " M[6]:" + M[6] + " M[7]:" + M[7] + " M[8]:" + M[8]);
        }
    }
    
    public void START() {
        PC++;
    }
    
    public void PUSH(){
        stack.push(X);
        PC++;
    }
	
    public void POP(){
        AC = (int)stack.pop();
        PC++;
    }
	
    public void RETURN(){
        PC = stack.pop();
    }
	
    public void LOADI(){
        AC=M[AX];
        PC++;
    }
	
    public void STOREI(){
        M[AX] = AC;
        PC++;
    }
	
    public void SWAP(){
        int temp = AX;
        AX = AC;
        AC = temp;
        PC++;
    }
	
    public void LOAD(){
        AC = X;
        PC++;
    }
	
    public void LOADM(){
        AC = M[X];
        PC++;
    }
	
    public void STORE(){
        M[X] = AC;
        PC++;
    }
	
    public void CMPM(){
        if(AC > M[X]){
            fFlag = 1;
        }else if(AC<M[X]){
            fFlag = -1; 
        }else{
            fFlag = 0;
        }
        PC++;
    }
	
    public void CJMP(){
        if(fFlag>0){         
            PC = X;
        } else {
            PC++;
        }
    }
	
    public void JMP(){         
        PC = X;
    }
	
    public void ADD(){         
        AC = AC + X;
        PC++;
    }
	
    public void ADDMM(){         
        AC = AC + M[X];
        PC++;
    }
	
    public void SUBMM(){
        AC = AC - M[X];
        PC++;
    }
	
    public void SUB(){         
        AC = AC - X;
        PC++;
    }
	
    public void MULN(){         
        AC = AC * X;
        PC++;
    }
	
    public void MULMN(){         
        AC = AC * M[X];
        PC++;
    }
	
    public void DISP(){         
        System.out.print(AC);
        PC++;
    }
	
    public void HALT(){
        halted = true;
    }
	
    public void DASC() {
        System.out.print((char)AC);
        PC++;
    }
      
    public static void main(String[] args) {
        CPUEmulator emu = new CPUEmulator();
        try {
            File myObj = new File(args[0]);
            BufferedReader myReader = new BufferedReader (new FileReader(myObj));
            String line;
            while((line = myReader.readLine()) != null){
                String[] list = line.split(" ");
                if(list.length > 1) {
                    int counter = Integer.parseInt(list[0]);
                    String command = list[1];
                    int parameter = list.length > 2 ? Integer.parseInt(list[2]) : 0;
                    Instruction newInstruction = new Instruction(command, parameter);
                    emu.instructionMemory.put(counter, newInstruction);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("An error occurred.");
        }
        emu.run();
    }
}