package edu.psuti.toi.coding;

import edu.psuti.toi.huffman.HuffmanCode;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HuffmanEncoder {
    private final static Logger logger = Logger.getLogger(HuffmanEncoder.class);

    /**
     * Encode %text% to the stream using Huffman prefixes
     *
     * @param bos         Output Stream
     * @param encodedText encoded text
     */
    public static void encode(ByteArrayOutputStream bos, final String encodedText) {
        Map<Character, String> header = HuffmanCode.createHeader(
                HuffmanCode.buildTree(HuffmanEncoder.getSymbolsProbability(encodedText))
                , new StringBuilder()
                , new HashMap<>()
        );

        writeHeader(bos, header);
        encodeSymbols(bos, header, encodedText);

        double inSize = encodedText.toCharArray().length;
        int outSize = bos.size();

        double compressing = (double)outSize / inSize * 100; // 100%
        logger.debug("InStream Size: " + inSize);
        logger.debug("OutStream Size: " + outSize);

        logger.info("Compression rate: " + compressing);
        logger.info("Bits on symbol: " + getBitsOnSymbol(outSize, encodedText.length()));
    }


    public static double getBitsOnSymbol(int streamSize, int textLength) {
        return ((double)streamSize * 8) / (double) textLength;
    }

    public static void encodeSymbols(ByteArrayOutputStream bos, Map<Character, String> header, final String text) {
        if (text.isEmpty())
            return;

        try {
            BufferedOutputStream buffStream = new BufferedOutputStream(bos);
            logger.debug("Write bit sequence to the stream");
            byte currPosition = 0;
            short currByte = 0;
            for (int i = 0; i != text.length(); i++) {
                Character c = text.charAt(i);
                String prefix = header.get(c);
                //handle incorrect headers
                if (prefix != null) {
                    for (char currBit : prefix.toCharArray()) {
                        currByte <<= 1;

                        if (currBit == '1') {
                            currByte |= 1;
                        }
                        currPosition++;

                        if (currPosition == 8) {

                            byte converted = (byte) currByte;
                            buffStream.write(converted);
                            currPosition = 0;
                            currByte = 0;
                        }
                    }
                } else {
                    logger.error("Header does not contain prefix for this symbol: " + (int) c);
                    return;
                }
            }
            //writing quantity of empty bits to the last byte
            writeEmptyBitsQuantity(buffStream, currPosition, currByte);

        } catch (ClassCastException cce) {
            logger.error("Incorrect header value. Are you sure that header " +
                    "contains <Character,String> entries?", cce);
        } catch (IOException e) {
            logger.error("An error occurred during writing to output stream.");
        }
    }

    private static void writeEmptyBitsQuantity(
            BufferedOutputStream buffStream, byte currPosition, short currByte
    ) throws IOException {
        int lastBits = 0;
        if (currPosition != 0) {
            lastBits = 8 - currPosition;
            currByte <<= lastBits;
        }
        logger.debug(String.format("Last bits value: %d", lastBits));
        buffStream.write(currByte);
        buffStream.write(lastBits);

        buffStream.flush();
        buffStream.close();
    }

    public static void writeHeader(ByteArrayOutputStream bos, Map<Character, String> header) {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bos);
        logger.debug("Write header");
        try (ObjectOutputStream os = new ObjectOutputStream(bufferedOutputStream)) {
            os.writeObject(header);

            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("An error occurred during writing an file header.", e);
        }
    }

    public static Map<Character, Float> getSymbolsProbability(String sequence) {
        Map<Character, Float> symbolsMap = new HashMap<>();
        int seqSize = sequence.length();
        for (int i = 0; i < seqSize; i++) {
            char c = sequence.charAt(i);
            Float currProb = symbolsMap.get(c);
            if (currProb == null) {
                currProb = 0.0f;
            }
            symbolsMap.put(c, ++currProb);
        }
        for (Map.Entry<Character, Float> c : symbolsMap.entrySet()) {
            symbolsMap.put(c.getKey(), c.getValue() / seqSize);
        }

        return symbolsMap;
    }


}
