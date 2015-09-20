import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
    public static boolean checkMatrixProb(float[][] probMatrix) {
        return isItSquareMatrix(probMatrix) && isRowsSumEquals1(probMatrix) && isColumnsSumEquals1(probMatrix);
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

    public static boolean isTextValid(String text, Configuration cfg){
        if (text.length() <= 1) return false;

        int textLength = text.length();
        int matrixSize = cfg.getMatrixProb().length;
        float[][] resultMatrix = new float[matrixSize][matrixSize];

        for (int i = 0; i != textLength - 1; i++){
            char first, second;

            first = text.charAt(i);
            second = text.charAt(i + 1);

            int row = cfg.getSymbolPosition(first);
            int column = cfg.getSymbolPosition(second);

            resultMatrix[row][column]++;
        }

        for (int i = 0; i != matrixSize; i++){
            for(int j = 0; j != matrixSize; j++){
                resultMatrix[i][j] /= textLength;
            }
        }
        //TODO: add comparing between result matrix & probability matrix
        return true;
    }
}
