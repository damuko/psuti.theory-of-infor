package com.toi.generator.utils;

public class ConfigurationUtils {

    public static boolean validateProbabilityMatrix(float[][] probMatrix) {
        if(isItSquareMatrix(probMatrix)) {
            return isRowsSumEquals1(probMatrix);
        }
        else {
            if(isItVector(probMatrix)) {
                return isVectorSumEquals1(probMatrix);
            }
        }
        return false;
    }

    public static boolean isVectorSumEquals1(float[][] probMatrix) {
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
}
