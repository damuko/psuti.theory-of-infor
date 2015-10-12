package com.toi.coding.huffman;

import com.sun.istack.internal.NotNull;

public abstract class HuffmanTree implements Comparable<HuffmanTree> {
    //its using to cast float probability to int
    public final static int HUNDRED_PERCENTS = 1000;
    public final int probability; // the probability of this tree
    public HuffmanTree(int prob) { probability = prob; }

    // compares on the probability
    public int  compareTo(HuffmanTree tree) {
        return (int) probability *HUNDRED_PERCENTS - (int)tree.probability *HUNDRED_PERCENTS;
    }
}