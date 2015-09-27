package com.toi.generator;

import com.toi.generator.utils.ConfigurationUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

@XmlRootElement
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

    public Configuration() {
    }

    private void validateProbMatrix(float[][] matrixProb) throws IllegalArgumentException{
        if (!ConfigurationUtils.validateMatrixProb(matrixProb))
            throw new IllegalArgumentException("Matrix has incorrect format!");
    }
    public char[] getSymbols() {
        return symbols;
    }

    @XmlElement(name = "alphabetSymbols")
    public void setSymbols(char[] symbols) {
        this.symbols = symbols;
        initSymbolsMap(symbols);
    }

    private void initSymbolsMap(char[] symbols) {
        this.symbolsMap = new HashMap<>();
        for (int i = 0; i != symbols.length; i++){
            symbolsMap.put(symbols[i],i);
        }
    }

    public int getSymbolPosition(char symbol) {
//        TODO:add handling for non-existing symbols
        return  symbolsMap.get(symbol);
    }
    public float[][] getMatrixProb() {
        return matrixProb;
    }

    @XmlElement(name = "probabilityMatrix")
    public void setMatrixProb(float[][] matrixProb) {
        validateProbMatrix(matrixProb);
        this.matrixProb = matrixProb;
    }
}
