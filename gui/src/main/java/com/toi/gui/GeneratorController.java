package com.toi.gui;

import com.toi.generator.ConfigUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class GeneratorController {
    @FXML
    private TextField insertedSymbolsTextArea;

    @FXML
    private TableView<String> probabilityTable;

    @FXML
    private TextField symbolsQuantityField;

    @FXML
    private Button confirmProbButton;

    @FXML
    private Button confirmSymbolsButton;

    @FXML
    private TextArea generatedSequenceTextArea;

    @FXML
    private TableColumn<Character, Integer> id;

    @FXML
    private TableColumn<Character, Integer> probability;

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

    private char [] allSymbols;
    public void confirmSymbolsButtonClicked ()
    {
        try {
            setInitSymbols();

            probabilityTable.getColumns().clear();
            probabilityTable.setEditable(true);

            addColumn("rowsNames","");
            for (char c : allSymbols){
                addColumn("symbol" + c, Character.toString(c));
            }

            for (char c : allSymbols){
                ObservableList<String> allData = probabilityTable.getItems();
                int offset = allData.size();

                allData.add(Character.toString(c));
            }
        }
        catch (Exception e) {
            Alert validationMessage = new Alert(Alert.AlertType.ERROR,
                    e.getMessage(), ButtonType.OK);
            validationMessage.showAndWait();
        }


    }

    private void addColumn(String key, String name) {
        TableColumn<String, Integer> newColumn = new TableColumn<>(name);
        newColumn.setCellValueFactory(new PropertyValueFactory<String, Integer>(key));
        probabilityTable.getColumns().add(newColumn);
    }

    private void setInitSymbols() throws Exception {
        String insertedText = insertedSymbolsTextArea.getText().trim();
        allSymbols = ConfigUtil.getSymbolsFromString(insertedText);
    }

    public void confirmProbButtonClicked()
    {
    }


}
