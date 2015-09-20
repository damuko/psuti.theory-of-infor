package com.toi.gui;

import com.toi.generator.ConfigUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class GeneratorController {
    @FXML
    private TextField insertedSymbolsTextArea;

    @FXML
    private TableView<Character> probabilityTable;

    @FXML
    private  TextField symbolsQuantityField;

    @FXML
    private  Button confirmProbButton;

    @FXML
    private Button confirmSymbolsButton;

    @FXML
    private TextArea generatedSequenceTextArea;

    @FXML
    private TableColumn<Character, Integer> id;

    @FXML
    private TableColumn<Character, Integer> probability;

    private char [] allSymbols;
    public void confirmSymbolsButtonClicked ()
    {
        try {
            String insertedText = insertedSymbolsTextArea.getText();
            allSymbols = ConfigUtil.getSymbolsFromString(insertedText);
            //for tests only
            for (char c : allSymbols){
                generatedSequenceTextArea.appendText(c + "\r\n");
            }
        }
        catch (Exception e) {
            Alert validationMessage = new Alert(Alert.AlertType.ERROR,
                    e.getMessage(), ButtonType.OK);
            validationMessage.showAndWait();
        }


    }
    public void confirmProbButtonClicked()
    {
    }


}
