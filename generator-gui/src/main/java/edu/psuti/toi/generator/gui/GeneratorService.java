package edu.psuti.toi.generator.gui;


import edu.psuti.toi.generator.Configuration;
import edu.psuti.toi.generator.Generator;
import edu.psuti.toi.generator.utils.GeneratorUtils;
import edu.psuti.toi.generator.utils.IOUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GeneratorService extends Service<GeneratorService.GenerationResult> {
    private Configuration configuration;
    private int sequenceSize;
    private String outputFilePath;

    @Override
    protected Task<GenerationResult> createTask() {
        return new Task<GenerationResult>() {
            @Override
            protected GenerationResult call() throws Exception {
                if (configuration == null || sequenceSize == 0 || outputFilePath == null ||
                        outputFilePath.isEmpty())
                    throw new ExceptionInInitializerError("Please set configuration and sequenceSize properties.");

                StringBuilder resultBuilder = new StringBuilder();
                String generatedText = new Generator(configuration).getRandomText(sequenceSize);
                float[][] resultMatrix = GeneratorUtils.calcResultProbability(generatedText, configuration);
                IOUtils.outputResults(configuration, generatedText, outputFilePath, resultBuilder);


                return new GenerationResult(resultBuilder,resultMatrix, generatedText);
            }
        };
    }

    class GenerationResult {
        final private StringBuilder resultBuilder;
        final private float[][] resultMatrix;
        final private String generatedText;

        public GenerationResult(StringBuilder resultBuilder, float[][] resultMatrix, String generatedText) {
            this.resultBuilder = resultBuilder;
            this.resultMatrix = resultMatrix;
            this.generatedText = generatedText;
        }

        public String getGeneratedText(){
            return this.generatedText;
        }

        public StringBuilder getResultBuilder() {
            return resultBuilder;
        }

        public float[][] getResultMatrix() {
            return resultMatrix;
        }
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setSequenceSize(int sequenceSize) {
        this.sequenceSize = sequenceSize;
    }
}
