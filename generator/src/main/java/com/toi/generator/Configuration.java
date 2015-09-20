package com.toi.generator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class Configuration {
    private char[] symbols;
    private Map<Character,Integer> symbolsMap;
    private float[][] matrixProb;

    public char[] getSymbols() {
        return symbols;
    }

    @XmlElement(name = "alphabetSymbols")
    public void setSymbols(char[] symbols) {
        this.symbols = symbols;
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
        this.matrixProb = matrixProb;
    }
}
