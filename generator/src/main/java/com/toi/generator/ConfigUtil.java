package com.toi.generator;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ConfigUtil {
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

    public static boolean validateMatrixProb(float[][] probMatrix) {
        return isItSquareMatrix(probMatrix) && isRowsSumEquals1(probMatrix) /*&& isColumnsSumEquals1(probMatrix)*/;
    }

    private static boolean isColumnsSumEquals1(float[][] probMatrix) {
        float sum;
        int size = probMatrix.length;
        for (int i = 0; i != size; i++){
            sum = 0;
            for (int j = 0; j != size; j++){
                sum += probMatrix[j][i];
            }
            if (sum != 1.0f) return false;
        }
        return true;
    }

    private static boolean isRowsSumEquals1(float[][] probMatrix) {
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

    private static boolean isItSquareMatrix(float[][] probMatrix){
        int rows = probMatrix.length;
        boolean check = true;
        for (int i = 0; i != rows && check; i++){
            if (probMatrix[i].length != rows){
                check = false;
            }
        }
        return check;
    }

    public static boolean isTextValid(String text, Configuration cfg) {
        //TODO: add comparing between result matrix & probability matrix
        return true;
    }
    public static float[][] calcResultProbabilityMatrix(String text, Configuration cfg){
        if (text.length() <= 1) return null;

        int textLength = text.length();
        int matrixSize = cfg.getMatrixProb().length;
        float[][] resultMatrix = new float[matrixSize][matrixSize];
        float[] resultSymbols = new float[matrixSize];

        for (int i = 0; i != textLength - 1; i++){
            char first, second;

            first = text.charAt(i);
            second = text.charAt(i + 1);

            int row = cfg.getSymbolPosition(first);
            resultSymbols[row]++;
            int column = cfg.getSymbolPosition(second);

            resultMatrix[row][column]++;
        }

        for (int i = 0; i != matrixSize; i++){
            for(int j = 0; j != matrixSize; j++){
                resultMatrix[i][j] /= resultSymbols[i];
            }
        }

        return resultMatrix;
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

    private static float calculateConditionalEntropy(float [][] probMatrix) {
        float entropy =0;
        float probability =0;
        for(int i=0; i<probMatrix.length; i++) {
            for(int j=0; j< probMatrix[i].length; j++) {
                for (int k=0; k< probMatrix.length; k++) {
                    probability+=probMatrix[k] [j]=0;
                }
                entropy+=probability+probMatrix[i][j]*Math.log(probMatrix[i][j]);
                probability=0;
            }
        }
        return -entropy;
    }

    private static float calculateUnconditionalEntropy(float [][] probMatrix) {
        float entropy =0;
        float probability =0;
        for(int i =0; i< probMatrix.length; i++) {
            for (int j=0; j<probMatrix[i].length; j++) {
                probability+=probMatrix[i][j];
            }
            entropy+=probability*Math.log(probability)/Math.log(2);
            probability=0;
        }
        return -entropy;
    }
}
