package com.toi.gui;

import com.sun.media.sound.InvalidFormatException;
import com.toi.generator.ConfigUtil;
import com.toi.generator.Configuration;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.io.*;

public class GeneratorController {
    @FXML
    private TextField insertedSymbolsTextArea;

    @FXML
    private TextArea probabilityInputArea;

    @FXML
    private TableView<float[]> probabilityTable;

    @FXML
    private TextField symbolsQuantityField;

    @FXML
    private Button confirmProbButton;

    @FXML
    private Button confirmSymbolsButton;

    @FXML
    private TextArea generatedSequenceTextArea;

//    @FXML
//    private TableColumn<Character, Integer> id;

//    @FXML
//    private TableColumn<Character, Integer> probability;

    private char [] allSymbols;

    private ObservableList<float[]> probabilityMatrix;

    public void setInsertedSymbolsText(String txt){
        insertedSymbolsTextArea.setText(txt);
    }

    public void setSymbolsQuantityField(int value){
        symbolsQuantityField.setText(Integer.toString(value));
    }

    public void clickConfirmProbButton() {
        this.confirmProbButton.fire();
    }

    public void clickConfirmSymbolsButton(){
        this.confirmSymbolsButton.fire();
    }

    public void confirmButtonClicked()
    {
        try {
            allSymbols = parseInitSymbols();

            float[][] parsedMatrix = parseMatrixFromText(probabilityInputArea.getText());

            Configuration cfg = new Configuration();
            cfg.setMatrixProb(parsedMatrix);
            cfg.setSymbols(allSymbols);
            int generatedSeqSize = getSequenceSize();

            String generatedText = new com.toi.generator.Generator(cfg).getRandomText(generatedSeqSize);
            generatedSequenceTextArea.clear();
//            if (generatedSeqSize < 1000000) {
//                generatedSequenceTextArea.setText(generatedText);
//            }
            float[][] resultMatrix = ConfigUtil.isTextValid(generatedText,cfg);

            printResultProbabilityMatrix(resultMatrix);
            writeToDefaultFile(generatedText);
        }
        catch (Exception e) {
            Alert validationMessage = new Alert(Alert.AlertType.ERROR,
                    e.getMessage(), ButtonType.OK);
            validationMessage.showAndWait();
        }
    }

    private void printResultProbabilityMatrix(float[][] resultMatrix) {
        if (resultMatrix != null) {
            for (int i =0; i != resultMatrix.length; i++){
                for (int j = 0; j != resultMatrix.length; j++) {
                    generatedSequenceTextArea.appendText(Float.toString(resultMatrix[i][j] ) + ',');
                }
                generatedSequenceTextArea.appendText("\n");
            }
        }
    }

    private void writeToDefaultFile(String text) throws IOException {
        final String DEF_FILE_NAME = "generated_sequence.txt";
        FileWriter fw = null;
        try (PrintWriter pw = new PrintWriter(new FileWriter(DEF_FILE_NAME))) {
            pw.println(text);
        } catch (IOException ex) {
            //TODO: modify exception handling
            throw new IOException("An error occurred when attempting to save " +
                    "generated sequence to file!");
        }

    }
    private int getSequenceSize(){
        int size = 0;
        try {
            size = Integer.parseInt(symbolsQuantityField.getText());
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Enter valid symbols quantity!");
        }

        return size;
    }

    private float[][] parseMatrixFromText(String textToParse) throws InvalidFormatException,
            NumberFormatException {
        float[][] resultMatrix = null;
        if (textToParse.length() == 0) return null;

        String[] lines = textToParse.split("\n");
        resultMatrix = new float[lines.length][];

        for (int i = 0; i!= lines.length; i++) {
            String[] values = lines[i].split(",");

            if (values.length != lines.length || values.length != allSymbols.length)
                throw new InvalidFormatException("Input size is incorrect!");


            resultMatrix[i] = new float[values.length];
            for (int j = 0; j != values.length; j++){
                try {
                    resultMatrix[i][j] = Float.parseFloat(values[j]);
                } catch (NumberFormatException nfe){
                    throw new NumberFormatException("Use only numeric values to enter!");
                }
            }

        }
        if (!ConfigUtil.validateMatrixProb(resultMatrix))
           throw new InvalidFormatException("Probability matrix should be square. Rows sum should be equals to 1");
        return resultMatrix;
    }

    private char[] parseInitSymbols() throws Exception {
        String insertedText = insertedSymbolsTextArea.getText().trim();
        return ConfigUtil.getSymbolsFromString(insertedText);
    }
}
