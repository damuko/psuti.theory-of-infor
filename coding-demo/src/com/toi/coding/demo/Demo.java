package com.toi.coding.demo;

import com.toi.coding.HuffmanEncoder;
import org.apache.log4j.Logger;

import com.toi.decoding.HuffmanDecoder;
import com.toi.huffman.HuffmanCode;
import com.toi.huffman.HuffmanTree;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Demo {
    private final static Logger logger = Logger.getLogger(Demo.class);

    public static void main(String[] args) throws IOException {
        encoderDemo();
        decoderDemo();
    }
    private static void decoderDemo() throws IOException {


        //todo: re-write this demo using encoder+decoder
        final String FILE_WITH_SEQUENCE = "generated_sequence.txt";
        final String TO_DECODE_FILE_NAME = "res2.bin";

        String sequence = new BufferedReader(new FileReader(FILE_WITH_SEQUENCE)).readLine();

        byte[] allBytesFromFile = HuffmanDecoder.readBytesArray(TO_DECODE_FILE_NAME);

        logger.info("\t\t  " + sequence);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HuffmanDecoder.decode(new ByteArrayInputStream(allBytesFromFile), baos);

        System.out.write(baos.toByteArray());
    }

    private static void encoderDemo() throws IOException {
        final String TO_ENCODE_FILE_NAME = "generated_sequence.txt";
        String  text = readSequence(TO_ENCODE_FILE_NAME);
        Map<Character, Float> symbolsMap = HuffmanEncoder.getSymbolsProbability(text);

        HuffmanTree tree = HuffmanCode.buildTree(symbolsMap);
        writeEncodedSequence(tree, text);
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

    private static void writeEncodedSequence(HuffmanTree tree, String text) throws IOException {
        Map<Character,String> header = HuffmanCode.createHeader(tree, new StringBuilder(), new HashMap<>());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        HuffmanEncoder.encode(byteArrayOutputStream, header, text);

        File resFile = new File("res2.bin");
        if (!resFile.exists() && resFile.createNewFile())
            logger.info("Create a new file for the encoded data: "
                    + resFile.getName());

        FileOutputStream fos = new FileOutputStream(resFile);

        fos.write(byteArrayOutputStream.toByteArray());

    }
}
