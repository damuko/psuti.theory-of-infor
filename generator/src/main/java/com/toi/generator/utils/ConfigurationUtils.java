package com.toi.generator.utils;


import com.toi.generator.Configuration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ConfigurationUtils {
    public static Configuration loadConfiguration(String filePath){
        Configuration configuration = null;
        try {

            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            configuration = (Configuration) jaxbUnmarshaller.unmarshal(file);
            System.out.println(configuration);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    public static boolean validateProbabilityMatrix(float[][] probMatrix) {
        if(isItSquareMatrix(probMatrix)) {
            return isRowsSumEquals1(probMatrix);
        }
        else {
            if(isItVector(probMatrix)) {
                return isColumnsSumEquals1(probMatrix);
            }
        }
        return false;
    }

    public static boolean isColumnsSumEquals1(float[][] probMatrix) {
        float sum=0;
        int size = probMatrix.length;
        for (int i = 0; i != size; i++){
            sum+=probMatrix[i][0];
        }
        return sum == 1.0f;
    }

    public static boolean isRowsSumEquals1(float[][] probMatrix) {
        float sum;
        int size = probMatrix.length;
        for (int i = 0; i != size; i++){
            sum = 0;
            for (int j = 0; j != size; j++){
                sum += probMatrix[i][j];
            }
            if (sum != 1.0f) return false;
        }
        return true;
    }

    public static boolean isItSquareMatrix(float[][] probMatrix){
        int rows = probMatrix.length;
        boolean check = true;
        for (int i = 0; i != rows && check; i++){
            if (probMatrix[i].length != rows){
                check = false;
            }
        }
        return check;
    }

    public  static boolean isItVector (float[][] probMatrix) {
        boolean check = true;
        for (int i = 0; i != probMatrix.length && check; i++){
            if (probMatrix[i].length != 1){
                check = false;
            }
        }
        return check;
    }

    public  static char [] getSymbolsFromString (String str) throws IllegalArgumentException  {
        String [] substrings = str.split(",");
        char [] charArray = new char[substrings.length];
        for(int i=0; i< substrings.length; i++) {
            String currentStr = substrings[i];
            if (currentStr.length() == 1
                    && Character.isLetterOrDigit(currentStr.charAt(0)))
                charArray [i] = currentStr.charAt(0);
            else throw new IllegalArgumentException("Please enter symbols and divide them by comma!");
        }
        return charArray;

    }
    private static void saveTestConfiguration() {
        Configuration testCfg = new Configuration();

        testCfg.setSymbols(new char[]{'A', 'B', 'C'});
        testCfg.setMatrixProb(
                new float[][] {new float[]{0.1f, 0.2f, 0.7f},
                        new float[] {0.4f, 0.5f, 0.1f},
                        new float[] {0.3f,0.3f, 0.4f}
                }
//                new float[][] {new float[] {0.1f, 0.4f, 0.5f}}
        );

        try {

            File file = new File("file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(testCfg, file);
            jaxbMarshaller.marshal(testCfg, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


}
