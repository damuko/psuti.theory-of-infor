package com.toi.gui;

import com.sun.media.sound.InvalidFormatException;
import com.toi.generator.ConfigUtil;
import com.toi.generator.Configuration;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.Arrays;

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
            setInitSymbols();

            float[][] parsedMatrix = parseMatrixFromTextArea(probabilityInputArea.getText());

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

            if (resultMatrix != null) {
                for (int i =0; i != resultMatrix.length; i++){
                    for (int j = 0; j != resultMatrix.length; j++) {
                        generatedSequenceTextArea.appendText(Float.toString(resultMatrix[i][j] ) + ',');
                    }
                    generatedSequenceTextArea.appendText("\n");
                }
            }
            writeToDefaultFile(generatedText);
        }
        catch (Exception e) {
            Alert validationMessage = new Alert(Alert.AlertType.ERROR,
                    e.getMessage(), ButtonType.OK);
            validationMessage.showAndWait();
        }
    }

    private void writeToDefaultFile(String text){
        final String DEF_FILE_NAME = "generated_sequence.txt";
        FileWriter fw = null;
        try (PrintWriter pw = new PrintWriter(new FileWriter(DEF_FILE_NAME))) {
//            fw = new FileWriter(DEF_FILE_NAME);
            pw.println(text);
        } catch (IOException ex) {
            //TODO: modify exception handling
            Alert validationMsg = new Alert(Alert.AlertType.ERROR,
                    ex.getMessage(), ButtonType.OK);
            validationMsg.showAndWait();
        }
//            fw.close();

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

    private float[][] parseMatrixFromTextArea(String textToParse) throws InvalidFormatException {
        float[][] resultMatrix = null;
        if (textToParse.length() == 0) return null;

        String[] lines = textToParse.split("\n");
        resultMatrix = new float[lines.length][];

        for (int i = 0; i!= lines.length; i++) {
            String[] values = lines[i].split(",");

            if (values.length != lines.length || values.length != allSymbols.length)
//                TODO:add alert
                throw new InvalidFormatException("Input size is incorrect!");


            resultMatrix[i] = new float[values.length];
            for (int j = 0; j != values.length; j++){
                try {
                    resultMatrix[i][j] = Float.parseFloat(values[j]);
                } catch (NumberFormatException nfe){
                    Alert textAreaValidation = new Alert(Alert.AlertType.ERROR,
                            "Use only numeric values to enter!", ButtonType.OK);
                    textAreaValidation.showAndWait();

                    return null;
                }
            }

        }
        return resultMatrix;
    }
    private void initTableView() {
        //init empty values for table observable collection
        float[][] tmpProbabilityMatrix = new float[allSymbols.length][];

        for (int i = 0; i != allSymbols.length; i++) {
            tmpProbabilityMatrix[i] = new float[allSymbols.length];
        }

//            probabilityMatrix.clear();
        probabilityMatrix = FXCollections.observableArrayList();
        probabilityMatrix.addAll(Arrays.asList(tmpProbabilityMatrix));

        probabilityTable.getColumns().clear();
        probabilityTable.setEditable(true);

        //add columns
        for (int i = 0; i != probabilityMatrix.size(); i++){
            String columnName = Character.toString(allSymbols[i]);
            TableColumn tc = new TableColumn<>(columnName);
//                tc.setCellValueFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
            tc.setCellValueFactory(new PropertyValueFactory<>(columnName));
            probabilityTable.getColumns().add(tc);
        }

        probabilityTable.setItems(probabilityMatrix);
    }

    private void addColumn(String key, String name) {
        TableColumn<String, Integer> newColumn = new TableColumn<>(name);
        newColumn.setCellValueFactory(new PropertyValueFactory<String, Integer>(key));
//        probabilityTable.getColumns().add(newColumn);
    }

    private void setInitSymbols() throws Exception {
        String insertedText = insertedSymbolsTextArea.getText().trim();
        allSymbols = ConfigUtil.getSymbolsFromString(insertedText);
    }

    public void confirmProbButtonClicked()
    {
    }


}
