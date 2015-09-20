import com.toi.generator.ConfigUtil;
import com.toi.generator.Configuration;
import com.toi.generator.Generator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class App {
    public static void main(String[] args){


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
