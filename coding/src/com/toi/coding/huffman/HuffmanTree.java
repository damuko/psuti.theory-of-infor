package com.toi.coding.huffman;

public abstract class HuffmanTree implements Comparable<HuffmanTree> {
    //its using to cast float probability to int
    public final static int HUNDRED_PERCENTS = 100;
    public final float probability; // the probability of this tree
    public HuffmanTree(float prob) { probability = prob; }

    // compares on the probability
    public int  compareTo(HuffmanTree tree) {
        return (int) probability *HUNDRED_PERCENTS - (int)tree.probability *HUNDRED_PERCENTS;
    }
}