package MsgGUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class DraftPanel extends BorderPane {
    private TextArea draftTextArea;
    private ListView<EmailEntity> draftListView;
    private TextField recipientTextField;
    private TextField subjectTextField;

    public DraftPanel() {
        setPadding(new Insets(10));

        draftTextArea = new TextArea();
        draftListView = new ListView<>();
        draftListView.setPrefWidth(200); // Set the preferred width of the draft list view

        VBox leftVBox = new VBox(10);
        leftVBox.getChildren().addAll(draftListView);

        // Create recipient and subject input fields on separate lines
        recipientTextField = new TextField();
        subjectTextField = new TextField();
        VBox inputFields = new VBox(10);
        inputFields.getChildren().addAll(
                new HBox(10, new Label("收件人:"), recipientTextField),
                new HBox(10, new Label("主题:"), subjectTextField)
        );

        leftVBox.getChildren().addAll(inputFields);
        setLeft(leftVBox);

        Button saveButton = new Button("保存草稿");
        saveButton.setOnAction(e -> saveDraft());

        Button newDraftButton = new Button("清空当前内容");
        newDraftButton.setOnAction(e -> createNewDraft());

        Button deleteDraftButton = new Button("删除草稿文件");
        deleteDraftButton.setOnAction(e -> deleteDraft());

        HBox buttonBar = new HBox(10);
        buttonBar.getChildren().addAll(saveButton, newDraftButton, deleteDraftButton);

        setCenter(draftTextArea);
        setBottom(buttonBar);

        // Load drafts when the application starts
        loadDrafts();

        // Add listener to load selected draft when a draft is selected in the list view
        draftListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadDraftContent(newValue);
            }
        });
    }

    private void saveDraft() {
        try {
            Path draftsDirectory = Paths.get("EmailAppBuffer", "drafts");
            Files.createDirectories(draftsDirectory);

            // Get the file name using time stamp
            String fileName = System.currentTimeMillis() + ".dat";

            // Save the draft entity
            EmailEntity draftEntity = new EmailEntity(
                    recipientTextField.getText(),
                    subjectTextField.getText(),
                    draftTextArea.getText()
            );

            Path draftFile = Paths.get("EmailAppBuffer", "drafts", fileName);
            try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(draftFile))) {
                outputStream.writeObject(draftEntity);
            }

            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("草稿保存成功");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // After saving, reload drafts
        loadDrafts();
    }

    private void loadDrafts() {
        try {
            Path draftsDirectory = Paths.get("EmailAppBuffer", "drafts");
            Files.createDirectories(draftsDirectory);

            // Load all draft files
            List<EmailEntity> draftEntities = new ArrayList<>();
            Files.walkFileTree(draftsDirectory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(file))) {
                        EmailEntity draftEntity = (EmailEntity) inputStream.readObject();
                        draftEntities.add(draftEntity);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            // Display draft entities in the list view
            ObservableList<EmailEntity> items = FXCollections.observableArrayList(draftEntities);
            draftListView.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDraftContent(EmailEntity draftEntity) {
        draftTextArea.setText(draftEntity.getContent());
        recipientTextField.setText(draftEntity.getRecipient());
        subjectTextField.setText(draftEntity.getSubject());
    }

    private void createNewDraft() {
        // Clear recipient, subject, and content fields to create a new blank draft
        recipientTextField.clear();
        subjectTextField.clear();
        draftTextArea.clear();
    }

    private void deleteDraft() {
        EmailEntity selectedDraft = draftListView.getSelectionModel().getSelectedItem();
        if (selectedDraft != null) {
            try {
                Path draftFile = Paths.get("EmailAppBuffer", "drafts", selectedDraft.hashCode() + ".dat");

                Files.deleteIfExists(draftFile);

                // Display a confirmation dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("草稿删除成功");
                alert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // After deleting, reload drafts
            loadDrafts();
        } else {
            // No draft selected, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请选择要删除的草稿");
            alert.showAndWait();
        }
    }
}
