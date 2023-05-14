package Util;

import Exceptions.LLCLIException;

import java.util.Scanner;

public class LLCLIParser {
    String[] args;
    public LLCLIParser(String[] args){
        for(int i = 0; i < args.length; i++){
//            this.args[i] = args[i].toLowerCase().trim();
        }
    }
    public void parseArgs(){
        args = new Scanner(System.in).nextLine().split(" ");
        try {
            if (args.length < 1 || args[0].equals("info") || args[0].equals("help")) {
                System.out.println(helpOutput());
            } else if (args[0].equals("save")) {
                System.out.println("save functionality not done yet - sorry");
            } else if (args[0].equals("reset")) {
                LLLabProcessor LLLabProcessor = new LLLabProcessor(LLPropsUtil.getCurrentLab());
                LLLabProcessor.processCanonical();
            } else if (args[0].equals("open")) {
                if (args.length < 2) {
                    throw new LLCLIException("Your second argument must include the lab name.");
                }
                LLLabProcessor LLLabProcessor = new LLLabProcessor(args[1]);
                LLLabProcessor.processSaved();
            }else if (args[0].equals("clear")) {
                LLLabProcessor LLLabProcessor = new LLLabProcessor("scratch");
                LLLabProcessor.clearWorkspace();
            }
        }catch(LLCLIException e){
            System.out.println("An issue occurred: ");
            System.out.println(e.getMessage());
            System.out.println("You can send this to Ted Balashov: ");
            e.printStackTrace();
        }
    }
    public String helpOutput(){
        String str = "Welcome to the LabLoader CLI. Here are the commands you can use:" +
                "java -jar Labs open lab-name-here"+
                "Open a specific lab, using a lab name from the Labs.md file. Opening a new lab will auto-save the current lab."+
                "java -jar Labs save"+
                "Manually save your current lab."+
                "java -jar Labs help"+
                "See this message again"+
                "authored by Ted Balashov 2023";
        return str;
    }
}