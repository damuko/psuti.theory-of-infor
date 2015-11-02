package edu.psuti.toi.generator;

import edu.psuti.toi.generator.utils.ConfigurationUtils;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

public class Configuration {
    private char[] symbols;
    private Map<Character,Integer> symbolsMap;
    private float[][] matrixProb;

    public Configuration (char[] symbols, float[][] matrixProb) throws IllegalFormatException {
        this.symbols = symbols;
        initSymbolsMap(symbols);
        validateProbMatrix(matrixProb);
        this.matrixProb = matrixProb;
    }

    private void validateProbMatrix(float[][] matrixProb) throws IllegalArgumentException{
        if (!ConfigurationUtils.validateProbabilityMatrix(matrixProb))
            throw new IllegalArgumentException("Matrix has incorrect format!");
    }
    public char[] getSymbols() {
        return symbols;
    }

    private void initSymbolsMap(char[] symbols) {
        this.symbolsMap = new HashMap<>();
        for (int i = 0; i != symbols.length; i++){
            symbolsMap.put(symbols[i],i);
        }
    }

    public int getSymbolPosition(char symbol) {
        Integer position = symbolsMap.get(symbol);
        return position == null ? -1 : position;
    }
    public float[][] getMatrixProb() {
        return matrixProb;
    }

}
