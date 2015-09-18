package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import sample.configuration.*;

public class Controller {
    @FXML
    private TextArea generatedSymbols;

    @FXML
    private TableView<Character> probabilityTable;

    @FXML
    private  TextArea symbolsQuantity;

    @FXML
    private  Button confirmProbButton;

    @FXML
    private Button confirmSymbolsButton;

    @FXML
    private TextField generatedSequence;

    @FXML
    private TableColumn<Character, Integer> id;

    @FXML
    private TableColumn<Character, Integer> probability;

    private char [] allSymbols;
    public void confirmSymbolsButtonClicked ()
    {
        try {
            allSymbols = ConfigUtil.getSymbolsFromString(generatedSequence.getText());
        }
        catch (Exception e) {}


    }
    public void confirmProbButtonClicked()
    {
    }


}
