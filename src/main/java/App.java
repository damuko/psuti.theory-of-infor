import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class App {
    public static void main(String[] args){
//        demonstrateMatrixValidation();
        saveTestConfiguration();
        Configuration cfg = ConfigUtil.loadConfiguration("file.xml");
        if (cfg != null){
            String testTxt = new Generator(cfg).getRandomText(256);
            ConfigUtil.isTextValid(testTxt,cfg);
        }
    }

    private static void demonstrateMatrixValidation() {
        float[][] testUtilMatrix1 = new float[][] {
                new float[] {0.3f, 0.4f, 0.3f},
                new float[] {0.5f, 0.3f, 0.2f},
                new float[] {0.2f, 0.3f, 0.5f}};

        boolean check = ConfigUtil.checkMatrixProb(testUtilMatrix1);
        System.out.println(check);

        float[][] testUtilMatrix2 = new float[][] {
                new float[] {0.1f, 0.1f, 0.1f},
                new float[] {0.1f, 0.1f, 0.1f},
                new float[] {0.1f, 0.1f, 0.1f}};

        check = ConfigUtil.checkMatrixProb(testUtilMatrix2);
        System.out.println(check);

        float[][] testUtilMatrix3 = new float[][] {
                new float[] {0.1f, 0.2f, 0.7f},
                new float[] {0.4f, 0.5f, 0.1f},
                new float[] {0.3f,0.3f, 0.4f}};

        check = ConfigUtil.checkMatrixProb(testUtilMatrix3);
        System.out.println(check);
    }

    private static void saveTestConfiguration() {
        Configuration testCfg = new Configuration();

        testCfg.setSymbols(new char[]{'A', 'B', 'C'});
        testCfg.setMatrixProb(
                new float[][] {new float[]{0.1f, 0.2f, 0.7f},
                        new float[] {0.4f, 0.5f, 0.1f},
                        new float[] {0.3f,0.3f, 0.4f}
                }
//                new float[][] {new float[] {0.1f, 0.4f, 0.5f}}
        );

        try {

            File file = new File("file.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(testCfg, file);
            jaxbMarshaller.marshal(testCfg, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
