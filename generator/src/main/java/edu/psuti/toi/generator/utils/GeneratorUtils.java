package edu.psuti.toi.generator.utils;


import edu.psuti.toi.generator.Configuration;

public class GeneratorUtils {
    public static float[][] calcResultProbability(String text, Configuration cfg) {
        if (ConfigurationUtils.isItSquareMatrix(cfg.getMatrixProb())) return calcResultProbabilityMatrix(text, cfg);
        else return calcResultProbabilityVector(text, cfg);
    }

    public static float calculateRedundancy(Configuration cfg, float conditionalEntropy, float unconditionalEntropy) {
        float redundancy;
        if (ConfigurationUtils.isItSquareMatrix(cfg.getMatrixProb())) {
            redundancy = calcRedundancy(conditionalEntropy, cfg.getSymbols().length);
        }
        else {
            redundancy = calcRedundancy(unconditionalEntropy, cfg.getSymbols().length);
        }
        return redundancy;
    }
    public static float calculateConditionalEntropy(float[][] probMatrix) {
        float entropy = 0;
        float probability = 0;
        for (int i = 0; i < probMatrix.length; i++) {
            for (int j = 0; j < probMatrix[i].length; j++) {
                for (int k = 0; k < probMatrix.length; k++) {
                    probability += probMatrix[k][j];
                }
                entropy += probability + probMatrix[i][j] * Math.log(probMatrix[i][j]);
                probability = 0;
            }
        }
        return -entropy;
    }

    public static float calcRedundancy(double entropy, int alphabetSize) {
        return (float)(1.0f - (entropy / (Math.log(alphabetSize) / Math.log(2))));
    }

    public static float[][] calcResultProbabilityVector(String text, Configuration cfg) {
        if (text.length() <= 1) return null;

        int textLength = text.length();
        int matrixSize = cfg.getMatrixProb().length;
        float[][] resultMatrix = new float[matrixSize][1];
        char first;
        int row;

        for (int i = 0; i != textLength - 1; i++) {
            first = text.charAt(i);

            row = cfg.getSymbolPosition(first);

            resultMatrix[row][0]++;
        }

        for (int i = 0; i != matrixSize; i++) {
            resultMatrix[i][0] /= text.length();

        }

        return resultMatrix;
    }

    public static float calculateUnconditionalEntropy(float[][] probMatrix) {
        float entropy = 0;
        float probability = 0;
        for (int i = 0; i < probMatrix.length; i++) {
            for (int j = 0; j < probMatrix[i].length; j++) {
                probability += probMatrix[i][j];
            }
            entropy += probability * Math.log(probability) / Math.log(2);
            probability = 0;
        }
        return entropy;
    }

    public static boolean isTextValid(String text, Configuration cfg) {
        //TODO: add comparing between result matrix & probability matrix
        return true;
    }

    public static float[][] calcResultProbabilityMatrix(String text, Configuration cfg) {
        if (text.length() <= 1) return null;

        int textLength = text.length();
        int matrixSize = cfg.getMatrixProb().length;
        float[][] resultMatrix = new float[matrixSize][matrixSize];
        float[] resultSymbols = new float[matrixSize];
        char first, second;
        int row, column;

        for (int i = 0; i != textLength - 1; i++) {
            first = text.charAt(i);
            second = text.charAt(i + 1);

            row = cfg.getSymbolPosition(first);
            resultSymbols[row]++;
            column = cfg.getSymbolPosition(second);

            resultMatrix[row][column]++;
        }

        for (int i = 0; i != matrixSize; i++) {
            for (int j = 0; j != matrixSize; j++) {
                resultMatrix[i][j] /= resultSymbols[i];
            }
        }

        return resultMatrix;
    }
}
