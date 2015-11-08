package edu.psuti.toi.generator.gui;

import edu.psuti.toi.generator.Configuration;
import edu.psuti.toi.generator.utils.ConfigurationUtils;
import edu.psuti.toi.generator.utils.GeneratorUtils;
import edu.psuti.toi.generator.utils.IOUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    private Label statusValueLbl;

    @FXML
    private ProgressIndicator generationIndicator;

    private Configuration cfg;
    private int sequenceSize;
    private final GeneratorService.GenerationResult[] generatedObject = {null};
    private GeneratorService generatorService;

    final String DEF_FILE_NAME = "generated_sequence.txt";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initGeneratorService();
    }

    private void initGeneratorService() {
        generatorService = new GeneratorService();

        generatorService.setOutputFilePath(DEF_FILE_NAME);
        generationIndicator.visibleProperty().bind(generatorService.runningProperty());

        //GeneratorService does all work
        generatorService.setOnSucceeded(workerStateEvent -> {
            confirmProbButton.setDisable(false);

            generatedObject[0] = generatorService.getValue();

            displayGenerationResults();
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

    private void displayGenerationResults() {
        generatedSequenceTextArea.clear();

        generatedSequenceTextArea.appendText(generatedObject[0].getResultBuilder().toString());

        statusValueLbl.setText("Text generation is finished.");
    }

    public void confirmButtonClicked() {
        executeGeneration();
    }

    private void executeGeneration() {
        try {
            char[] allSymbols = parseInitSymbols();

            float[][] parsedMatrix = IOUtils.parseMatrix(probabilityInputArea.getText()
                    , allSymbols.length);

            cfg = new Configuration(allSymbols, parsedMatrix);
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

    private int getSequenceSize() {
        int size;
        try {
            size = Integer.parseInt(symbolsQuantityField.getText());
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Sequence size should contain only numbers.");
        }

        return size;
    }


    private char[] parseInitSymbols() {
        return ConfigurationUtils.getSymbols(
                insertedSymbolsTextArea.getText().trim());
    }

    //these methods will be used for unit testing
    public void setInsertedSymbolsText(String txt){
        insertedSymbolsTextArea.setText(txt);
    }

    public void setSymbolsQuantityField(int value){
        symbolsQuantityField.setText(Integer.toString(value));
    }

    public void clickConfirmProbButton() {
        this.confirmProbButton.fire();
    }
}
