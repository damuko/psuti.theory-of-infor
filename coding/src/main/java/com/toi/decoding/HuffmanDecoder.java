package com.toi.decoding;

import org.apache.log4j.Logger;

import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

//todo: add method for decoding by stream
public class HuffmanDecoder {
    private final static Logger logger = Logger.getLogger(HuffmanDecoder.class);

    public static void decode(ByteArrayInputStream bais, ByteArrayOutputStream decodedStream) {
        Map<Character,String> header = readHeader(bais);
        StringBuilder seq = getSequence(bais);
        try {
            decodedStream.write(String.valueOf(decodeSequence(seq,header)).getBytes());
        } catch (IOException ie){
            logger.error("An error occurred while writing encoded value");
        }
    }

    public static Map<Character,String> readHeader(ByteArrayInputStream bis) {
        Map<Character,String> restoredHeader = null;
        logger.debug("Read header");
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object restoredObj = ois.readObject();
            if (restoredObj instanceof Map)
                //noinspection unchecked
                restoredHeader = (Map<Character,String>) restoredObj;
            else throw new ClassCastException();
        } catch (ClassNotFoundException | IOException | ClassCastException e) {
            logger.error("An error occured during reading huffman header from file:", e);
        }
        return restoredHeader;
    }

    public static byte[] readBytesArray(String path) throws IOException {
        ByteArrayOutputStream out = null;
        InputStream input = null;
        try {
            out = new ByteArrayOutputStream();
            input = new BufferedInputStream(new FileInputStream(path));
            int data;
            while ((data = input.read()) != -1) {
                out.write(data);
            }
        } finally {
            if (null != input) {
                input.close();
            }
            if (null != out) {
                out.close();
            }
        }
        return out.toByteArray();
    }

    public static StringBuilder getSequence(ByteArrayInputStream bais) {
        int bytesQuantity = bais.available();
        StringBuilder encodedSequence = new StringBuilder();
        for (int i = 0; i < bytesQuantity - 1; i++) {
            encodedSequence.append(String.format("%8s", Integer.toBinaryString(bais.read())).replace(' ', '0'));
        }
        int lastBits = bais.read();
        encodedSequence.delete((encodedSequence.length() - lastBits), encodedSequence.length());
        return encodedSequence;
    }

    private static int getMaxPrefixLength(Map<Character, String> map) {
        int  maxLength = 0;
        for(Map.Entry<Character, String> entry: map.entrySet()) {
            if ( maxLength< (entry.getValue()).length()) {
                maxLength=(entry.getValue()).length();
            }
        }
        return maxLength;
    }

    @SuppressWarnings("unchecked")
    private static Map getInvertedMap(Map m) {
        Map invertedMap = new HashMap<>();
        for(Object entry : m.entrySet()) {
            Map.Entry<Object,Object> objectEntry = ((Map.Entry<Object, Object>)entry);
            invertedMap.put(objectEntry.getValue(), objectEntry.getKey());
        }
        return invertedMap;
    }

    public static StringBuilder decodeSequence (StringBuilder encodedSequence, Map <Character, String> header)
          throws IllegalArgumentException {
        logger.debug("Sequence before encoding: " + encodedSequence.toString());
        logger.debug("Start decoding bit sequence");
        StringBuilder prefix = new StringBuilder();
        StringBuilder decodedSequence = new StringBuilder();

        Map invertedMap = getInvertedMap(header);

        int maxPrefixLength = getMaxPrefixLength(header);
        boolean check;
        for (int i=0; i< encodedSequence.length(); i++) {
            prefix.append(encodedSequence.charAt(i));
            check = header.containsValue(prefix.toString());
            if(check) {
                decodedSequence.append(invertedMap.get(prefix.toString()));
                prefix.setLength(0);
            }
            else if ( prefix.length()> maxPrefixLength ) {
                throw new IllegalArgumentException("Header does not match to encoded sequence!");
            }
        }
        return decodedSequence;
    }
}

