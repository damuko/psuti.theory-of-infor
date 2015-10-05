package com.toi.coding;

import com.toi.coding.huffman.HuffmanCode;
import com.toi.coding.huffman.HuffmanTree;

public class HuffmanCoder {

    public static void main(String[] args) {
        String test = "";

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
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        HuffmanCode.printCodes(tree, new StringBuffer());
    }
}
