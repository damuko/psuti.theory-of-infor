package com.toi.decoding;

import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;


public class HuffmanDecoder {
    public static void main(String[] args) throws IOException {

        final String FILE_WITH_SEQUENCE = "generated_sequence.txt";
        final String TO_DECODE_FILE_NAME = "res2.bin";
        String sequence = new BufferedReader(new FileReader(FILE_WITH_SEQUENCE)).readLine();
        System.out.println("Последовательность до кодирования: "+ sequence);
        byte[] allBytesFromFile = readBytesArray(TO_DECODE_FILE_NAME);
        Map restoredMap = readHeader(new ByteArrayInputStream(allBytesFromFile));
        long headerSize = getObjectSize(restoredMap);
        byte[] bytesWithSequence = getBytesWithSequence(allBytesFromFile, (int) headerSize);
        StringBuilder encodedSequence = getSequenceFromBytes(bytesWithSequence);
        System.out.println("Закодированная последовательность: " +encodedSequence);
        try {
            StringBuilder decodedSequence = decodeSequence(encodedSequence, restoredMap);
            System.out.println("Раскодированная последовательность: " + decodedSequence);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Map readHeader(ByteArrayInputStream bis) {
        Map restoredHeader = null;

        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object restoredObj = ois.readObject();
            if (restoredObj instanceof Map)
                restoredHeader = (Map) restoredObj;
            else throw new ClassCastException();
        } catch (ClassNotFoundException | IOException | ClassCastException e) {

        } finally {

        }
        return restoredHeader;
    }

    private static byte[] readBytesArray(String path) throws IOException {
        ByteArrayOutputStream out = null;
        InputStream input = null;
        try {
            out = new ByteArrayOutputStream();
            input = new BufferedInputStream(new FileInputStream(path));
            int data = 0;
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
    private static long getObjectSize(Object o) throws IOException {
        // Serializable ser =
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return baos.size();
    }

    private static byte[] getBytesWithSequence(byte[] allBytes, int headerSize) {
        int sequenceLength = allBytes.length - headerSize;
        byte[] bytesWithSequence = new byte[sequenceLength];
        for (int i = 0; i < sequenceLength; i++) {
            bytesWithSequence[i] = allBytes[i + headerSize];
        }
        return bytesWithSequence;
    }

    private static StringBuilder getSequenceFromBytes(byte[] bytes) {
        ByteArrayInputStream encodedBytes = new ByteArrayInputStream(bytes);
        int bytesQuantity = encodedBytes.available();
        StringBuilder encodedSequence = new StringBuilder();
        for (int i = 0; i < bytesQuantity - 1; i++) {
            encodedSequence.append(String.format("%8s", Integer.toBinaryString(encodedBytes.read())).replace(' ', '0'));
        }
        //System.out.println ( "Неиспользовано : " + unusedBitsQuantity);
        encodedSequence.delete((encodedSequence.length() - bytes[bytes.length - 1]), encodedSequence.length());
        return encodedSequence;
    }

    private static int getMaxPrefixLength(Map<Character, String> map) {
        int  maxLength=0;
        for(Map.Entry<Character, String> entry: map.entrySet()) {
            if ( maxLength< (entry.getValue()).length()) {
                maxLength=(entry.getValue()).length();
            }
        }
        return maxLength;
    }

    private static Character getKeyByValue (Map <Character, String > map, String value) {
        for (Map.Entry <Character, String> entry: map.entrySet()) {
            if (value.equals(entry.getValue()))
                return entry.getKey();
        }
        return null;
    }
    //todo : create reversed map or find another way to get key by value
    private static StringBuilder decodeSequence (StringBuilder encodedSequence, Map <Character, String> map) throws Exception{
        StringBuilder prefix = new StringBuilder();
        StringBuilder decodedSequence = new StringBuilder();
        int maxPrefixLength = getMaxPrefixLength(map);
        boolean check;
        for (int i=0; i< encodedSequence.length(); i++) {
            prefix.append(encodedSequence.charAt(i));
            check = map.containsValue(prefix.toString());
            if(check) {
                decodedSequence.append(getKeyByValue(map, prefix.toString()));
                prefix.setLength(0);
            }
            else if ( prefix.length()> maxPrefixLength ) {
                throw new Exception("Header does not match encoded sequence!");
            }
        }
        return decodedSequence;
    }
}

