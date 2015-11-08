package edu.psuti.toi.coding.demo;

import edu.psuti.toi.coding.HuffmanEncoder;
import edu.psuti.toi.decoding.HuffmanDecoder;
import org.apache.log4j.Logger;

import java.io.*;

public class Demo {
    private final static Logger logger = Logger.getLogger(Demo.class);

    public static void main(String[] args) throws IOException {
        encoderDemo();
        decoderDemo();
    }
    private static void decoderDemo() throws IOException {
        final String FILE_WITH_SEQUENCE = "generated_sequence.txt";
        final String TO_DECODE_FILE_NAME = "res2.bin";

        String sequence = new BufferedReader(new FileReader(FILE_WITH_SEQUENCE)).readLine();

        byte[] allBytesFromFile = HuffmanDecoder.readBytesArray(TO_DECODE_FILE_NAME);

        logger.info("Initial seq: " + sequence);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HuffmanDecoder.decode(new ByteArrayInputStream(allBytesFromFile), baos);

        logger.info("Decoded seq: " + new String(baos.toByteArray()));
    }

    private static void encoderDemo() throws IOException {
        final String TO_ENCODE_FILE_NAME = "generated_sequence.txt";
        String  text = readSequence(TO_ENCODE_FILE_NAME);

        writeEncodedSequence(text);
    }

    private static String readSequence(String filePath) {
        String sequence= null;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filePath));
            sequence=bf.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sequence;
    }

    private static void writeEncodedSequence(String text) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        HuffmanEncoder.encode(byteArrayOutputStream, text);

        File resFile = new File("res2.bin");
        if (!resFile.exists() && resFile.createNewFile())
            logger.info("Create a new file for the encoded data: "
                    + resFile.getName());

        FileOutputStream fos = new FileOutputStream(resFile);

        fos.write(byteArrayOutputStream.toByteArray());

    }
}
