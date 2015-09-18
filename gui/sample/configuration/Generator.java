package sample.configuration;
import java.util.Random;

public class Generator {
    private Configuration cfg;
    private Random rnd = new Random();

    public Generator(Configuration cfg) {
        this.cfg = cfg;
    }

    public String getRandomText(int length){
        float probability = rnd.nextFloat();


        StringBuilder resultBuilder = new StringBuilder();

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

    //первый символ, с которого начинается генерация
    private char getFirstSymbol(){
        int nextPosition = rnd.nextInt(cfg.getSymbols().length);
        return cfg.getSymbols()[nextPosition];
    }

    //? получаем столбец, с которым будем работать, по предыдущему символу
    private float[] getNextRow(Character firstSymbol) {
        int charPosition = cfg.getSymbolPosition(firstSymbol);
        return cfg.getMatrixProb()[charPosition];
    }

    //получаем случайный сивол по рабочему столбцу
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
