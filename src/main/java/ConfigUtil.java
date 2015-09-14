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
}
