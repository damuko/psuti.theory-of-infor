package com.toi.generator;

import java.util.Random;

public class Generator {
    private Configuration cfg;
    private Random rnd = new Random();

    public Generator(Configuration cfg) {
        this.cfg = cfg;
    }

    public String getRandomText(int length){
        StringBuilder resultBuilder = new StringBuilder();
// TODO: add handling for vectors
//        Character firstSymbol = getRandomSymbol(cfg.getMatrixProb()[0]);
        char firstSymbol = getFirstSymbol();

        float[] currentRow = getNextRow(firstSymbol);

        for (int j = 0; j != length; j++) {
            Character rndCharacter = getRandomSymbol(currentRow);

            currentRow = getNextRow(rndCharacter);
            resultBuilder.append(rndCharacter);
        }

        return resultBuilder.toString();
    }

    private char getFirstSymbol(){
        int nextPosition = rnd.nextInt(cfg.getSymbols().length);
        return cfg.getSymbols()[nextPosition];
    }

    private float[] getNextRow(Character firstSymbol) {
        int charPosition = cfg.getSymbolPosition(firstSymbol);
        return cfg.getMatrixProb()[charPosition];
    }

    private Character getRandomSymbol(float[] row){
        float right = 0;
        float currRandom = rnd.nextFloat();
        Character randomCharacter = null;
        for (int i = 0; i != row.length; i++){
            right += cfg.getMatrixProb()[0][i];

            if (currRandom <= right) {
                randomCharacter = cfg.getSymbols()[i];
                break;
            }
        }
        return randomCharacter;
    }
}
