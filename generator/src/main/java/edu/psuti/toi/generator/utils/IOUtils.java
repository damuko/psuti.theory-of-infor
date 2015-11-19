package edu.psuti.toi.generator.utils;

import edu.psuti.toi.generator.Configuration;

import static edu.psuti.toi.generator.utils.GeneratorUtils.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOUtils {
    private static final String INPUT_SIZE_EXCEPTION_TEXT = "Input size is incorrect!";
    private static final String NUMBER_FORMAT_EXCEPTION_TEXT = "Use only numeric values to enter!";
    private static final String PROBABILITY_MATRIX_FORMAT_EXCEPTION_TEXT = "Probability matrix should be square. " +
            "Rows sum should be equals to 1";

    public static String readFile(String path, Charset encoding) throws IOException {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        } catch (IOException io) {
            throw new IOException("An error occurred when attempting to read a file:" + path, io);

        }
    }

    //parsing matrix from string
    public static float[][] parseMatrix(String textToParse, int symbolsQuantity) throws IllegalArgumentException {
        float[][] resultMatrix;
        if (textToParse.length() == 0) {
            throw new IllegalArgumentException(INPUT_SIZE_EXCEPTION_TEXT);
        }

        String[] lines = textToParse.split("\n");
        resultMatrix = new float[lines.length][];

        for (int i = 0; i != lines.length; i++) {
            String[] values = lines[i].split(",");

            //validation for vector
            if (values.length == 1) {
                if (lines.length != symbolsQuantity)
                    throw new IllegalArgumentException(INPUT_SIZE_EXCEPTION_TEXT);
            }
            //validation for square matrix
            else {
                if (values.length != lines.length || values.length != symbolsQuantity)
                    throw new IllegalArgumentException(INPUT_SIZE_EXCEPTION_TEXT);
            }

            resultMatrix[i] = new float[values.length];
            for (int j = 0; j != values.length; j++) {
                try {
                    resultMatrix[i][j] = Float.parseFloat(values[j]);
                } catch (NumberFormatException nfe) {
                    throw new NumberFormatException(NUMBER_FORMAT_EXCEPTION_TEXT);
                }
            }
        }
        if (!ConfigurationUtils.validateProbabilityMatrix(resultMatrix))
            throw new IllegalArgumentException(PROBABILITY_MATRIX_FORMAT_EXCEPTION_TEXT);
        return resultMatrix;
    }

    public static void printMatrix(float[][] resultProbMatrix, StringBuilder result) {
        if (resultProbMatrix != null) {
            for (int i = 0; i != resultProbMatrix.length; i++) {
                for (int j = 0; j != resultProbMatrix[i].length; j++) {
                    result.append(Float.toString(resultProbMatrix[i][j])).append(',');
                }
                result.append('\n');
            }
        }
    }

    public static void writeToFile(String text, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println(text);
        } catch (IOException ex) {
            throw new IOException("An error occurred when attempting to save " +
                    "generated sequence to file!", ex);
        }
    }

    public static void outputResults(
            Configuration cfg
            , String generatedText
            , String outputFilePath
            , StringBuilder resultMatrixBuilder) throws IOException {

        float[][] resProbabilityMatrix = calcResultProbability(generatedText, cfg);

        float conditionalEntropy = calculateConditionalEntropy(resProbabilityMatrix);
        float unconditionalEntropy = calculateUnconditionalEntropy(resProbabilityMatrix);
        float redundancy;

        redundancy = calculateRedundancy(cfg, conditionalEntropy, unconditionalEntropy);

        printMatrix(resProbabilityMatrix, resultMatrixBuilder);

        resultMatrixBuilder
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("Conditional Entropy: ")
                .append(conditionalEntropy)
                .append(System.lineSeparator())
                .append("Unconditional Entropy: ")
                .append(unconditionalEntropy)
                .append(System.lineSeparator())
                .append("Redundancy: ")
                .append(redundancy);


        writeToFile(generatedText, outputFilePath);

    }


}
