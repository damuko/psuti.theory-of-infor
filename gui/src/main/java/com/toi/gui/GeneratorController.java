package com.toi.gui;

import com.toi.generator.Configuration;
import com.toi.generator.utils.ConfigurationUtils;
import com.toi.generator.utils.GeneratorUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GeneratorController {
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
        confirmProbButton.setDisable(true);
        executeGeneration();
    }

    private void executeGeneration() {
        try {
            allSymbols = parseInitSymbols();

            float[][] parsedMatrix = parseMatrixFromText(probabilityInputArea.getText());

            Configuration cfg = new Configuration(allSymbols,parsedMatrix);
            int generatedSeqSize = getSequenceSize();
            final GeneratorService backgroundService = new GeneratorService(cfg,generatedSeqSize);

            generationIndicator.visibleProperty().bind(backgroundService.runningProperty());
            final String[] generatedText = {""};
            backgroundService.setOnSucceeded(workerStateEvent -> {
                confirmProbButton.setDisable(false);
                generatedText[0] = backgroundService.getValue();
                try {
                    outputResults(cfg, generatedText[0], parsedMatrix);
                } catch (IOException e) {
                    statusValueLbl.setText(e.getMessage());
                }
            });

            backgroundService.setOnFailed(workerStateEvent -> {
                confirmProbButton.setDisable(false);
                statusValueLbl.setText(workerStateEvent.toString());
            });
            backgroundService.restart();
        }
        catch (Exception e) {
            Alert validationMessage = new Alert(Alert.AlertType.ERROR,
                    e.getMessage(), ButtonType.OK);
            validationMessage.showAndWait();
            statusValueLbl.setText(e.getMessage());
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
            //TODO: modify exception handling
            throw new IOException("An error occurred when attempting to save " +
                    "generated sequence to file!");
        }

    }
    private int getSequenceSize(){
        int size;
        try {
            size = Integer.parseInt(symbolsQuantityField.getText());
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Enter valid symbols quantity!");
        }

        return size;
    }

    private float[][] parseMatrixFromText(String textToParse) throws IllegalArgumentException {
        float[][] resultMatrix;
        if (textToParse.length() == 0) return null;

        String[] lines = textToParse.split("\n");
        resultMatrix = new float[lines.length][];

        for (int i = 0; i!= lines.length; i++) {
            String[] values = lines[i].split(",");

            if(values.length==1) {
                if (lines.length!=allSymbols.length)
                    throw new IllegalArgumentException("Input size is incorrect!");
            }
            else {
                if (values.length != lines.length || values.length != allSymbols.length)
                    throw new IllegalArgumentException("Input size is incorrect!");
            }

            resultMatrix[i] = new float[values.length];
            for (int j = 0; j != values.length; j++){
                try {
                        resultMatrix [i][j]= Float.parseFloat(values[j]);
                } catch (NumberFormatException nfe){
                    throw new NumberFormatException("Use only numeric values to enter!");
                }
            }
        }
        if (!ConfigurationUtils.validateMatrixProb(resultMatrix))
           throw new IllegalArgumentException("Probability matrix should be square. Rows sum should be equals to 1");
        return resultMatrix;
    }

    private char[] parseInitSymbols() throws Exception {
        String insertedText = insertedSymbolsTextArea.getText().trim();
        return ConfigurationUtils.getSymbolsFromString(insertedText);
    }
}
