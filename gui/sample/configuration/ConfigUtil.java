package sample.configuration;
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
        return isItSquareMatrix(probMatrix) && !isRowsSumGreater1(probMatrix) && !isColumnsSumGreater1(probMatrix);

    }

    private static boolean isColumnsSumGreater1(float[][] probMatrix) {
        float sum;
        int size = probMatrix.length;
        for (int i = 0; i != size; i++){
            sum = 0;
            for (int j = 0; j != size; j++){
                sum += probMatrix[j][i];
            }
            if (sum != 1.0f) return true;
        }
        return false;
    }

    private static boolean isRowsSumGreater1(float[][] probMatrix) {
        float sum;
        int size = probMatrix.length;
        for (int i = 0; i != size; i++){
            sum = 0;
            for (int j = 0; j != size; j++){
                sum += probMatrix[i][j];
            }
            if (sum != 1.0f) return true;
        }
        return false;
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
    public  static char [] getSymbolsFromString (String str) throws Exception{
        String [] substrings = str.split(",");
        char [] charArray = new char[substrings.length];
        for(int i=0; i< substrings.length; i++) {
            if (substrings[i].length()==1) charArray [i] = substrings[i].charAt(0);
            else throw new Exception("Введите символы через запятую!");
        }
        return charArray;

    }

}
