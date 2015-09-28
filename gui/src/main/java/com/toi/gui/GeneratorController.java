package com.toi.gui;

import com.toi.generator.Configuration;
import com.toi.generator.utils.ConfigurationUtils;
import com.toi.generator.utils.GeneratorUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class GeneratorController implements Initializable{
    @FXML
    private TextField insertedSymbolsTextArea;

    @FXML
    private TextArea probabilityInputArea;

    @FXML
    private TextField symbolsQuantityField;

    @FXML
    private Button confirmProbButton;

    @FXML
    private TextArea generatedSequenceTextArea;

    @FXML
    private Label condEntrValueLbl;

    @FXML
    private Label uncondEntrValueLbl;

    @FXML
    private Label statusValueLbl;

    @FXML
    private ProgressIndicator generationIndicator;

    @FXML
    private char [] allSymbols;

    private static final String INPUT_SIZE_EXCEPTION_TEXT ="Input size is incorrect!";
    private static final String NUMBER_FORMAT_EXCEPTION_TEXT = "Use only numeric values to enter!";
    private static final String PROBABILITY_MATRIX_FORMAT_EXCEPTION_TEXT = "Probability matrix should be square. " +
            "Rows sum should be equals to 1";

    private Configuration cfg;
    private int sequenceSize;
    private final String[] generatedText = {""};
    private GeneratorService generatorService;
    private float[][] parsedMatrix;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGeneratorService();
    }

    private void initGeneratorService() {
        generatorService = new GeneratorService();

        generationIndicator.visibleProperty().bind(generatorService.runningProperty());

        generatorService.setOnSucceeded(workerStateEvent -> {
            confirmProbButton.setDisable(false);
            generatedText[0] = generatorService.getValue();
            try {
                outputResults(cfg, generatedText[0], parsedMatrix);
                statusValueLbl.setText("Text generation is finished.");
            } catch (IOException e) {
                statusValueLbl.setText(e.getMessage());
            }
        });
        generatorService.setOnFailed(workerStateEvent -> {
            confirmProbButton.setDisable(false);
            statusValueLbl.setText(workerStateEvent.toString());
        });
    }
    private void startGeneratorService(){
        generatorService.setConfiguration(cfg);
        generatorService.setSequenceSize(sequenceSize);

        generatorService.restart();
    }

    public void setInsertedSymbolsText(String txt){
        insertedSymbolsTextArea.setText(txt);
    }

    public void setSymbolsQuantityField(int value){
        symbolsQuantityField.setText(Integer.toString(value));
    }


    public void clickConfirmProbButton() {
        this.confirmProbButton.fire();
    }

    public void confirmButtonClicked()
    {
        executeGeneration();
    }

    private void executeGeneration() {
        try {
            allSymbols = parseInitSymbols();

            parsedMatrix = parseMatrixFromText(probabilityInputArea.getText());

            cfg = new Configuration(allSymbols,parsedMatrix);
            sequenceSize = getSequenceSize();

            confirmProbButton.setDisable(true);
            startGeneratorService();
        }
        catch (Exception e) {
            Alert validationMessage = new Alert(Alert.AlertType.ERROR,
                    e.getMessage(), ButtonType.OK);
            validationMessage.showAndWait();
            statusValueLbl.setText(e.getMessage());
            confirmProbButton.setDisable(false);
        }
    }

    private void outputResults(Configuration cfg, String generatedText, float [][] probMatrix) throws IOException {
        generatedSequenceTextArea.clear();

        float[][] resultMatrix = GeneratorUtils.calcResultProbability(generatedText, cfg);
        printResultProbabilityMatrix(resultMatrix);

        writeToDefaultFile(generatedText);

        condEntrValueLbl.setText(Float.toString(GeneratorUtils.calculateConditionalEntropy(probMatrix)));
        uncondEntrValueLbl.setText(Float.toString(GeneratorUtils.calculateUnconditionalEntropy(probMatrix)));
    }

    private void printResultProbabilityMatrix(float[][] resultMatrix) {
        if (resultMatrix != null) {
            for (int i =0; i != resultMatrix.length; i++){
                for (int j = 0; j != resultMatrix[i].length; j++) {
                    generatedSequenceTextArea.appendText(Float.toString(resultMatrix[i][j] ) + ',');
                }
                generatedSequenceTextArea.appendText("\n");
            }
        }
    }
    private void writeToDefaultFile(String text) throws IOException {
        final String DEF_FILE_NAME = "generated_sequence.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(DEF_FILE_NAME))) {
            pw.println(text);
        } catch (IOException ex) {
            throw new IOException("An error occurred when attempting to save " +
                    "generated sequence to file!");
        }

    }

    private int getSequenceSize(){
        int size;
        try {
            size = Integer.parseInt(symbolsQuantityField.getText());
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException();
        }

        return size;
    }

    private float[][] parseMatrixFromText(String textToParse) throws IllegalArgumentException {
        float[][] resultMatrix;
        if (textToParse.length() == 0) {
            throw new IllegalArgumentException(INPUT_SIZE_EXCEPTION_TEXT);
        }

        String[] lines = textToParse.split("\n");
        resultMatrix = new float[lines.length][];

        for (int i = 0; i!= lines.length; i++) {
            String[] values = lines[i].split(",");

            //validation for vector
            if(values.length==1) {
                if (lines.length!=allSymbols.length)
                    throw new IllegalArgumentException(INPUT_SIZE_EXCEPTION_TEXT);
            }
            //validation for square matrix
            else {
                if (values.length != lines.length || values.length != allSymbols.length)
                    throw new IllegalArgumentException(INPUT_SIZE_EXCEPTION_TEXT);
            }

            resultMatrix[i] = new float[values.length];
            for (int j = 0; j != values.length; j++){
                try {
                        resultMatrix [i][j]= Float.parseFloat(values[j]);
                } catch (NumberFormatException nfe){
                    throw new NumberFormatException(NUMBER_FORMAT_EXCEPTION_TEXT);
                }
            }
        }
        if (!ConfigurationUtils.validateMatrixProb(resultMatrix))
           throw new IllegalArgumentException(PROBABILITY_MATRIX_FORMAT_EXCEPTION_TEXT);
        return resultMatrix;
    }

    private char[] parseInitSymbols() throws Exception {
        String insertedText = insertedSymbolsTextArea.getText().trim();
        return ConfigurationUtils.getSymbolsFromString(insertedText);
    }
}
