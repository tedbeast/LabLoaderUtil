import Util.LLCLIParser;

import java.io.*;
import java.net.URISyntaxException;

public class LabLoaderApplication {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        LLCLIParser LLCLIParser = new LLCLIParser(args);
        LLCLIParser.parseArgs();
    }
}