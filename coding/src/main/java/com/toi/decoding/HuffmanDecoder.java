package com.toi.decoding;

import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

//todo: add method for decoding by only fileName
public class HuffmanDecoder {

    public static Map readHeader(ByteArrayInputStream bis) {
        Map restoredHeader = null;

        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object restoredObj = ois.readObject();
            if (restoredObj instanceof Map)
                restoredHeader = (Map) restoredObj;
            else throw new ClassCastException();
        } catch (ClassNotFoundException | IOException | ClassCastException e) {
            e.printStackTrace();
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

    //todo : find a normal way to get object size
    // we should remove reading all bytes to array. We need synchronous read object and after read all other bytes
    public static long getObjectSize(Object o) throws IOException {
        // Serializable ser =
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return baos.size();
    }

    public static byte[] getBytesWithSequence(byte[] allBytes, int headerSize) {
        int sequenceLength = allBytes.length - headerSize;
        byte[] bytesWithSequence = new byte[sequenceLength];
        System.arraycopy(allBytes, headerSize, bytesWithSequence, 0, sequenceLength);
        return bytesWithSequence;
    }

    public static StringBuilder getSequenceFromBytes(byte[] bytes) {
        ByteArrayInputStream encodedBytes = new ByteArrayInputStream(bytes);
        int bytesQuantity = encodedBytes.available();
        StringBuilder encodedSequence = new StringBuilder();
        for (int i = 0; i < bytesQuantity - 1; i++) {
            encodedSequence.append(String.format("%8s", Integer.toBinaryString(encodedBytes.read())).replace(' ', '0'));
        }
        encodedSequence.delete((encodedSequence.length() - bytes[bytes.length - 1]), encodedSequence.length());
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
    public static StringBuilder decodeSequence (StringBuilder encodedSequence, Map <Character, String> map)
            throws Exception{
        StringBuilder prefix = new StringBuilder();
        StringBuilder decodedSequence = new StringBuilder();

        Map invertedMap = getInvertedMap(map);

        int maxPrefixLength = getMaxPrefixLength(map);
        boolean check;
        for (int i=0; i< encodedSequence.length(); i++) {
            prefix.append(encodedSequence.charAt(i));
            check = map.containsValue(prefix.toString());
            if(check) {
                decodedSequence.append(invertedMap.get(prefix.toString()));
                prefix.setLength(0);
            }
            else if ( prefix.length()> maxPrefixLength ) {
                throw new Exception("Header does not match encoded sequence!");
            }
        }
        return decodedSequence;
    }
}

