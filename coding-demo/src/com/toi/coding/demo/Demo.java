package com.toi.coding.demo;

import com.toi.decoding.HuffmanDecoder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Demo {
    public static void main(String[] args) throws IOException {
        decodingDemo();
    }
    private static void decodingDemo() throws IOException {

        final String FILE_WITH_SEQUENCE = "generated_sequence.txt";
        final String TO_DECODE_FILE_NAME = "res2.bin";

        String sequence = new BufferedReader(new FileReader(FILE_WITH_SEQUENCE)).readLine();
        System.out.println("Последовательность до кодирования: \t"+ sequence);

        //see comments for method getObjectSize
        byte[] allBytesFromFile = HuffmanDecoder.readBytesArray(TO_DECODE_FILE_NAME);
        Map restoredMap = HuffmanDecoder.readHeader(new ByteArrayInputStream(allBytesFromFile));

        long headerSize = HuffmanDecoder.getObjectSize(restoredMap);
        byte[] bytesWithSequence = HuffmanDecoder.getBytesWithSequence(allBytesFromFile, (int) headerSize);
        StringBuilder encodedSequence = HuffmanDecoder.getSequenceFromBytes(bytesWithSequence);

        try {
            StringBuilder decodedSequence = HuffmanDecoder.decodeSequence(encodedSequence, restoredMap);
            System.out.println("Раскодированная последовательность: " + decodedSequence);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Закодированная последовательность: \t" +encodedSequence);

    }
}
