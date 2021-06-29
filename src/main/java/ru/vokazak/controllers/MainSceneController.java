package ru.vokazak.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import ru.vokazak.TextAnalyzer;
import ru.vokazak.alertWindows.ErrorAlertWindow;
import ru.vokazak.exceptions.TextAnalyzerException;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import static javafx.collections.FXCollections.observableList;

public class MainSceneController implements Initializable {

    private FileChooser fileChooser;
    private final TextAnalyzer textAnalyzer = new TextAnalyzer();

    private Set<Entry<String, Integer>> wordFrequencySet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select document");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("DOC", "*.dox"),
                new FileChooser.ExtensionFilter("DOCX", "*.docx")
        );

        textArea.setText("No data");
        textArea.setEditable(false);

        TableColumn<Entry<String, Integer>, String> wordColumn = new TableColumn<>();
        wordColumn.setText("Слово");
        wordColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getKey()));

        TableColumn<Entry<String, Integer>, Integer> frequencyColumn = new TableColumn<>();
        frequencyColumn.setText("Количество повторений");
        frequencyColumn.setCellValueFactory(e -> new SimpleIntegerProperty(e.getValue().getValue()).asObject());

        tableView.getColumns().setAll(wordColumn, frequencyColumn);

        wordFrequencySet = Collections.emptySet();

    }

    @FXML
    private TableView<Entry<String, Integer>> tableView;

    @FXML
    private TextArea textArea;

    @FXML
    private void textAreaClicked() {
        String selectedText = textArea.getSelectedText();

        for (Entry<String, Integer> e : wordFrequencySet) {
            if (e.getKey().equals(selectedText.trim())) {
                tableView.getSelectionModel().select(e);
                return;
            }
        }
    }

    @FXML
    private void openFileButtonClicked() {

        File file = fileChooser.showOpenDialog(Window.getWindows().get(0));

        if (file == null) {
            new ErrorAlertWindow("No file was selected").show();
            clearFields();
            return;
        }

        try {
            String text = textAnalyzer.readDataFromFile(file);
            textArea.setText(text);

            wordFrequencySet = textAnalyzer
                    .countWordFrequency(
                            textAnalyzer.extractWordsFromString(text)
                    ).entrySet();

            tableView.setItems(
                    observableList(
                            new ArrayList<>(wordFrequencySet)
                    )
            );

        } catch (TextAnalyzerException e) {
            new ErrorAlertWindow(e.getMessage());
            clearFields();
        }

    }

    private void clearFields() {
        textArea.setText("No data");
        tableView.setItems(FXCollections.emptyObservableList());
        wordFrequencySet.clear();
    }

}
