package com.toi.coding;

import com.toi.coding.huffman.HuffmanCode;
import com.toi.coding.huffman.HuffmanTree;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HuffmanCoder {
    private final static Logger logger = Logger.getLogger(HuffmanCoder.class);


    private static void writeHeader(ByteArrayOutputStream bos, HuffmanTree tree) {

        Map<Character,String> headerDictionary =
                HuffmanCode.createHeader(tree,new StringBuilder(), new HashMap<>());

        try (ObjectOutputStream os = new ObjectOutputStream(bos)){
            os.writeObject(headerDictionary);

            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("An error occurred during writing an encoded file header.", e);
        }

    }

    private static Map readHeader(ByteArrayInputStream bis) {
        Map restoredHeader = null;

        try(ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object restoredObj = ois.readObject();
            if (restoredObj instanceof Map)
                restoredHeader = (Map)restoredObj;
            else throw new ClassCastException();
        } catch (ClassNotFoundException | IOException | ClassCastException e) {
            logger.error("An error occurred during reading an encoded file header.", e);
        }
        return restoredHeader;
    }

    public static void main(String[] args) throws IOException {
        //init
        float [][] prob = new float[][] {
                new float[] {0.1f},
                new float[] {0.2f},
                new float [] {0.135f},
                new float [] {0.265f},
                new float [] {0.18f},
                new float[] {0.12f}};
        char [] charArray = {'a', 'b', 'c', 'd', 'f', 'e'};

        // build tree
        HuffmanTree tree = HuffmanCode.buildTree(charArray, prob);

        // print out results
        logger.info("SYMBOL\tHUFFMAN CODE");

        showWriteReadHeader(tree);
    }

    private static void showWriteReadHeader(HuffmanTree tree) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //write our header to stream
        writeHeader(bos, tree);

        //add some garbage to stream
        String dummyStuff = "Some dummy stuff";
        logger.info("Dummy data size in bytes is " + dummyStuff.getBytes().length);
        bos.write(dummyStuff.getBytes());

        //restore header from stream
        ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
        Map restoredMap = readHeader(bais);

        for (Object o : restoredMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;

            logger.info(String.format("%s\t\t%s"
                    , entry.getKey()
                    , entry.getValue())
            );

        }

        //restore garbage string
        byte[] dummyStringBytes = new byte[bais.available()];
        int length = bais.read(dummyStringBytes);
        String restoredString = new String(dummyStringBytes);

        logger.info("Garbage size in bytes is " + length);
        logger.info("Restored string is \"" + restoredString +"\"");
    }
}
