package Util;

import Exceptions.LLCLIException;

import java.io.*;

public class LLPropsUtil {
    public static String propsPath = "./labs.properties";
    public static String productKey;

    /**
     * grab the product key from the properties file
     * @return
     * @throws LLCLIException
     */
    public static String getPkey() throws LLCLIException {
        if(productKey != null){
            return productKey;
        }
        try {
            BufferedReader propsReader = new BufferedReader(new FileReader(propsPath));
            String line = propsReader.readLine();
            String key = line.split("=")[1];
            return key;

        } catch (FileNotFoundException e) {
            throw new LLCLIException("Error locating properties file");
        } catch (IOException e) {
            throw new LLCLIException("Error reading properties file");
        }
    }

    /**
     * grab the current lab from the properties file
     * @return
     * @throws LLCLIException
     */
    public static String getCurrentLab() throws LLCLIException {
        try {
            BufferedReader propsReader = new BufferedReader(new FileReader(propsPath));
            String line = propsReader.readLine();
            line = propsReader.readLine();
            String lab = line.split("=")[1];
            return line;
        } catch (FileNotFoundException e) {
            throw new LLCLIException("Error locating properties file");
        } catch (IOException e) {
            throw new LLCLIException("Error reading properties file");
        }
    }
    /**
     * set the current lab in the properties file
     * @param labName
     * @return
     * @throws LLCLIException
     */
    public static String setCurrentLab(String labName) throws LLCLIException {
//        in case the pkey hasn't been set yet, set it.
        productKey = getPkey();
        try {
            BufferedWriter propsWriter = new BufferedWriter(new FileWriter(propsPath, false));
            propsWriter.write("product_key="+productKey+"\n");
            propsWriter.write("current_lab="+labName);
            propsWriter.close();
            return labName;
        } catch (IOException e) {
            throw new LLCLIException("There was some issue writing to your properties file.");
        }
    }
}
