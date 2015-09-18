package sample.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
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

    @XmlElement
    public void setSymbols(char[] symbols) {
        this.symbols = symbols;
        this.symbolsMap = new HashMap<Character, Integer>();
        for (int i = 0; i != symbols.length; i++){
            symbolsMap.put(symbols[i],i);
        }
    }

    public void setSymbols (ArrayList<Character> symbols) {

    }

    public int getSymbolPosition(char symbol) {
        return  symbolsMap.get(symbol);
    }
    public float[][] getMatrixProb() {
        return matrixProb;
    }

    @XmlElement
    public void setMatrixProb(float[][] matrixProb) {
        this.matrixProb = matrixProb;
    }
}
