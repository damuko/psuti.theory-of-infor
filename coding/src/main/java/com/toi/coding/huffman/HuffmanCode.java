package com.toi.coding.huffman;

import java.util.*;



//leaf - node without children nodes
class HuffmanLeaf extends HuffmanTree {
    public final char value; // the character this leaf represents

    public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
    @Override
    public String toString(){
        return String.format("[%s - %f]", value,probability);
    }
}

// node with children
class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees

    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.probability + r.probability);
        left = l;
        right = r;
    }
}

public class HuffmanCode {
    // input is an array of frequencies, indexed by character code
    public static HuffmanTree buildTree(char [] charArray, float [][] prob) {
        //create queue beginning with least element
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<>();

        float probCount=0;
        // initially, we have a forest of leaves
        // one for each non-empty character
        for(int i=0; i< prob.length; i++) {
            for(int j=0; j<prob[0].length; j++) {
                probCount+=prob[i][j];
            }
            trees.offer(new HuffmanLeaf ((int)(probCount*HuffmanTree.HUNDRED_PERCENTS), charArray[i]));
            probCount=0;
        }

        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least probability
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();

            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }
    public static Map<Character,String> createHeader(HuffmanTree tree, StringBuilder prefix, Map<Character, String> dictionary){
//        Map<Character, String> dictionary = new HashMap<>();
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) tree;

            dictionary.put(leaf.value, prefix.toString());
            // print out character, probability, and code for this leaf (which is just the prefix)
//            System.out.println(leaf.value + "\t\t" + leaf.probability + "\t\t" + prefix);

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

            // traverse left
            prefix.append('0');
            createHeader(node.left, prefix, dictionary);
            prefix.deleteCharAt(prefix.length() - 1);

            // traverse right
            prefix.append('1');
            createHeader(node.right, prefix, dictionary);
            prefix.deleteCharAt(prefix.length() - 1);
        }


        return dictionary;
    }
    public static void printCodes(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf) tree;

            // print out character, probability, and code for this leaf (which is just the prefix)
            System.out.println(leaf.value + "\t\t" + leaf.probability + "\t\t" + prefix);

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode) tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length() - 1);

            // traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }
}
