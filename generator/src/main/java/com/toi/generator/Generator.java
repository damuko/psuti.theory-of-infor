package com.toi.generator;

import com.toi.generator.utils.ConfigurationUtils;

import java.util.Random;

public class Generator {
    private final Configuration cfg;

    private final float[][] symbolsBorders;
    private final Random rnd = new Random();
    public Generator(Configuration cfg) {
        this.cfg = cfg;

       symbolsBorders = initSymbolsBorders();
    }
    public Configuration getCfg() {
        return cfg;
    }

    public String getRandomText(int length){
        if(ConfigurationUtils.isItSquareMatrix(cfg.getMatrixProb())) return getRandomTextFromProbabilityMatrix(length);
        else return getRandomTextFromVector(length);
    }


    private String getRandomTextFromVector(int length){
        StringBuilder resultBuilder = new StringBuilder();

        Character rndCharacter;
        for (int j = 0; j != length; j++) {
            rndCharacter = getRandomSymbolFromVector();
            resultBuilder.append(rndCharacter);
        }

        return resultBuilder.toString();
    }

    private String getRandomTextFromProbabilityMatrix(int length) {
        StringBuilder resultBuilder = new StringBuilder();
        char firstSymbol = getFirstSymbol();

        int symbolPosition = cfg.getSymbolPosition(firstSymbol);
        Character rndCharacter;
        for (int j = 0; j != length; j++) {
            rndCharacter = getRandomSymbol(symbolPosition);
            symbolPosition = cfg.getSymbolPosition(rndCharacter);

            resultBuilder.append(rndCharacter);
        }

        return resultBuilder.toString();
    }

    private float [][] initSymbolsBorders () {
        if (ConfigurationUtils.isItSquareMatrix(cfg.getMatrixProb())) return initMatrixSymbolsBorders();
        else return initVectorSymbolsBorders();

    }
    private float[][] initMatrixSymbolsBorders(){
        int matrixSize = cfg.getSymbols().length;
        float[][] symbolsBorders = new float[matrixSize][matrixSize];
        float previousBorder;
        for (int i = 0; i != matrixSize; i++){
            previousBorder = 0;
            for (int j = 0; j != matrixSize; j++){
                symbolsBorders[i][j] = cfg.getMatrixProb()[i][j] + previousBorder;
                previousBorder = symbolsBorders[i][j];
            }
        }
        return symbolsBorders;
    }
    private float[][] initVectorSymbolsBorders(){
        int matrixRows = cfg.getSymbols().length;
        float[][] symbolsBorders = new float[matrixRows] [1];
        float previousBorder=0;
        for (int i = 0; i != symbolsBorders.length; i++){
            symbolsBorders[i][0] = cfg.getMatrixProb()[i][0] + previousBorder;
            previousBorder = symbolsBorders[i][0];
        }
        return symbolsBorders;
    }
    private char getFirstSymbol(){
        int nextPosition = rnd.nextInt(cfg.getSymbols().length);
        return cfg.getSymbols()[nextPosition];
    }

    private float[] getNextRow(Character firstSymbol) {
        int charPosition = cfg.getSymbolPosition(firstSymbol);
        return cfg.getMatrixProb()[charPosition];
    }

    private Character getRandomSymbolFromVector() {
        float curRandom = rnd.nextFloat();
        Character randomChar = null;
        for (int i = 0; i != cfg.getSymbols().length; i++){
           if (curRandom <= symbolsBorders[i][0]) {
                randomChar = cfg.getSymbols()[i];
                break;
            }
        }

        return randomChar;
    }
    private Character getRandomSymbol(int symbolPosition){

        float currRandom = rnd.nextFloat();
        Character randomCharacter = null;
        for (int i = 0; i != cfg.getSymbols().length; i++){
            if (currRandom <= symbolsBorders[symbolPosition][i]) {
                randomCharacter = cfg.getSymbols()[i];
                break;
            }
        }
        return randomCharacter;
    }
}
