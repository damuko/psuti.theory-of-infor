package com.toi.coding;

import com.toi.coding.huffman.HuffmanCode;
import com.toi.coding.huffman.HuffmanTree;
import com.toi.decoding.HuffmanDecoder;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HuffmanCoder {
    private final static Logger logger = Logger.getLogger(HuffmanCoder.class);

    //TODO: make some refactoring
    @SuppressWarnings("unchecked")
    private static void encodeSymbols(ByteArrayOutputStream bos, Map header, final String text) {
        if (text.isEmpty())
            return;

        try {
            BufferedOutputStream buffStream = new BufferedOutputStream(bos); //TODO: investigate what buff size is the best
            Map<Character,String> cHeader = (Map<Character,String>)header;

            byte currPosition = 0;
            short currByte = 0;
            for (int i = 0; i != text.length(); i++){
                Character c = text.charAt(i);
                String prefix = cHeader.get(c);
                //handle incorrect headers
                if (prefix != null) {
                    for (char currBit : prefix.toCharArray()){
                        currByte <<= 1;

                        if (currBit == '1'){
                            currByte |=  1;
                        }
                        currPosition++;

                        //TODO: add handling for 256> symbols in prefix
                        if (currPosition == 8) {

                            byte converted = (byte)currByte;
                            buffStream.write(converted);
                            currPosition = 0;
                            currByte = 0;
                        }
                    }


                } else {
                    logger.error("Header does not contain prefix for this symbol: " + c);
                    return;
                }
            }
            if (currPosition != 0) { //TODO: modify to support byte[] buffer
                int lastBits = 8 - currPosition;
                currByte <<= lastBits;
                buffStream.write(currByte);
                buffStream.write(lastBits);
            }
            buffStream.flush();
            buffStream.close();
        } catch (ClassCastException cce){
            logger.error("Incorrect header value. Are you sure that header " +
                    "contains <Character,String> entries?",cce);
        } catch (IOException e) {
            logger.error("An error occured during writing to output stream.");
        }
    }
//todo: add calculating probability from text
    private static void writeHeader(ByteArrayOutputStream bos, Map header) {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bos);
        try (ObjectOutputStream os = new ObjectOutputStream(bufferedOutputStream)){
            os.writeObject(header);

            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("An error occurred during writing an file header.", e);
        }

    }

    /*private static Map readHeader(ByteArrayInputStream bis) {
        Map restoredHeader = null;

        try(ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object restoredObj = ois.readObject();
            if (restoredObj instanceof Map)
                restoredHeader = (Map)restoredObj;
            else throw new ClassCastException();
        } catch (ClassNotFoundException | IOException | ClassCastException e) {
            logger.error("An error occurred during reading an file header.", e);
        }
        return restoredHeader;
    }*/

    public static void main(String[] args) throws IOException {
        demo();
    }

    private static void demo() throws IOException {
        //init
//        float [][] prob = new float[][] {
//                new float[] {0.1f},
//                new float[] {0.2f},
//                new float [] {0.135f},
//                new float [] {0.265f},
//                new float [] {0.18f},
//                new float[] {0.12f}};
//        char [] charArray = {'a', 'b', 'c', 'd', 'f', 'e'};
        final String TO_ENCODE_FILE_NAME = "generated_sequence.txt";
        String  text = readSequence(TO_ENCODE_FILE_NAME);
//        float [] prob = getProbabilityVector(text, getSymbolsProbability(readSequence(TO_ENCODE_FILE_NAME)));
        Map<Character, Float> symbolsMap = getSymbolsProbability(text);
        // build tree
        HuffmanTree tree = HuffmanCode.buildTree(symbolsMap);

        // print out results
        logger.info("SYMBOL\tHUFFMAN CODE");
       // showWriteReadHeader(tree);
        writeEncodedSequence(tree, text);
    }

    private  static  String readSequence(String filePath) {
        String sequence= null;
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filePath));
            sequence=bf.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sequence;
    }

    private static Map <Character, Float> getSymbolsProbability(String sequence){
        Map <Character, Float> symbolsMap = new HashMap<>();
        int seqSize = sequence.length();
        for (int i=0; i< seqSize; i++) {
            char c = sequence.charAt(i);
            Float currProb = symbolsMap.get(c);
            if (currProb == null) {
                currProb = 0.0f;
            }
            symbolsMap.put(c, ++currProb);
        }
        for (Map.Entry<Character, Float> c : symbolsMap.entrySet()){
            symbolsMap.put(c.getKey(),c.getValue() / seqSize);
        }

        return symbolsMap;
    }

    private static void writeEncodedSequence(HuffmanTree tree, String text) throws IOException {
        Map dictionary = HuffmanCode.createHeader(tree, new StringBuilder(), new HashMap<>());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        writeHeader(byteArrayOutputStream, dictionary);
        encodeSymbols(byteArrayOutputStream, dictionary, text);

        File resFile = new File("res2.bin");
        if (!resFile.exists())
            resFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(resFile);

        fos.write(byteArrayOutputStream.toByteArray());

    }
    private static void showWriteReadHeader(HuffmanTree tree) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //write our header to stream
        Map dictionary = HuffmanCode.createHeader(tree, new StringBuilder(), new HashMap<>());
        writeHeader(bos, dictionary);

        //add some garbage to stream
        String dummyStuff = "Some dummy stuff";
        logger.info("Dummy data size in bytes is " + dummyStuff.getBytes().length);
        bos.write(dummyStuff.getBytes());

        //restore header from stream
        ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
        Map restoredMap = HuffmanDecoder.readHeader(bais);

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
        logger.info("Restored string is \"" + restoredString + "\"");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

//        encodeSymbols(byteArrayOutputStream, dictionary, "ddddaabf");
        writeHeader(byteArrayOutputStream, dictionary);
        encodeSymbols(byteArrayOutputStream, dictionary, "bbbc");
        ByteArrayInputStream encodedStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        File resFile = new File("res.bin");
        if (!resFile.exists())
            resFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(resFile);

        fos.write(byteArrayOutputStream.toByteArray());

        StringBuilder resStr = new StringBuilder();
        logger.info("Available bytes: " + encodedStream.available());
        int av = encodedStream.available();
        int res;
        for (int i = 0; i != av; i++) {
            res = encodedStream.read();
            resStr.append(String.format("%8s",Integer.toBinaryString(res)).replace(' ','0'));
        }
        logger.info("Result: " + resStr);
    }
}
