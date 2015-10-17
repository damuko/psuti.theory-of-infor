package com.toi.decoding;

import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.lang.instrument.Instrumentation;


/**
 * Created by 284 on 16.10.2015.
 */
public class HuffmanDecoder {
    public static void main(String[] args) throws IOException {

        final String TO_DECODE_FILE_NAME = "res2.bin";
        String decodingText ="";
        byte [] bytes = readBytesArray(TO_DECODE_FILE_NAME);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Map restoredMap = readHeader(bis);
    }
    public static Map readHeader(ByteArrayInputStream bis) {
        Map restoredHeader = null;

        try(ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object restoredObj = ois.readObject();
            if (restoredObj instanceof Map)
                restoredHeader = (Map)restoredObj;
            else throw new ClassCastException();
        } catch (ClassNotFoundException | IOException | ClassCastException e) {

        }
        return restoredHeader;
    }
    public static byte[] readBytesArray(String path) throws IOException{
        ByteArrayOutputStream out = null;
        InputStream input = null;
        try{
            out = new ByteArrayOutputStream();
            input = new BufferedInputStream(new FileInputStream(path));
            int data = 0;
            while ((data = input.read()) != -1){
                out.write(data);
            }
        }
        finally{
            if (null != input){
                input.close();
            }
            if (null != out){
                out.close();
            }
        }
        return out.toByteArray();
    }
}

